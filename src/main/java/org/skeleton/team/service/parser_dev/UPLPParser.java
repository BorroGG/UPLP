package org.skeleton.team.service.parser_dev;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.mapper.UplpDocMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Парсер для документов ГПЗУ.
 */
@Component
@RequiredArgsConstructor
public class UPLPParser {

    private final UplpDocMapper uplpDocMapper;

    private static final String ISSUE_DATE_ACTUAL = "Действует";
    private static final String ISSUE_DATE_EXPIRED = "Срок действия истёк";

    private static final String SECRET = "Секретно";
    public static final String SUT_NOT_LIVING = "Не жилая";
    public static final String SUT_MIXED = "Смешанная";
    public static final String SUT_LIVING = "Жилая";

    /**
     * Производит последовательный парсинг документа согласно требуемому регламенту и возвращает сущность документа с заполненными полями.
     * @param file - pdf-документ ГПЗУ для парсинга
     * @return документ с парсенными полями
     * @throws IOException при ошибке работы с файлом
     */
    public List<UplpDoc> parsingUPLPFile(File file, StringBuilder parseErrors) throws IOException {
        List<UplpDoc> docs = new ArrayList<>();

        UplpDoc uplpDoc = new UplpDoc();
//        UplpDoc uplpDoc2 = uplpDocMapper.copyDocData(uplpDoc); TODO так копируем данные в новый объект

        docs.add(uplpDoc);

        //Подготавливаем документ и получаем строку с текстом документа
        String parsedText = prepareFile(file);

        //Разбиваем строку на массив отдельных строк в зависимости от ОС (на Windows и UNIX системах разные коды для переноса строки)
        String[] words = parsedText.split("\n|\r\n");

        //Парсинг первых пяти атрибутов
        parse1to5Attributes(uplpDoc, words, parseErrors);

        //TODO достать библиотеку склонений

        uplpDoc.setUplpRecipient(parseRecipient(words));
        if (uplpDoc.getUplpRecipient().equals("-")) {
            parseErrors.append("Не найден Правообладатель или иной получатель ГПЗУ.\n");
        }

        uplpDoc.setRecipientType(checkRecipientType(uplpDoc.getUplpRecipient()));
        if (uplpDoc.getRecipientType().equals("-")) {
            parseErrors.append("Не найден Тип правообладателя или получателя ГПЗУ.\n");
        }

        //TODO парсинг округа и района

        String s = parseBuildingAddress(words);

        uplpDoc.setBuildingAddress(s);
        if (uplpDoc.getBuildingAddress().equals("-")) {
            parseErrors.append("Не найден Строительный адрес.\n");
        }

        parseCadastralNumber(uplpDoc, words);

        String availability = parseAvailability(words);

        uplpDoc.setTlpProjectAvailability(availability);
        if (uplpDoc.getTlpProjectAvailability().equals("-")) {
            parseErrors.append("Не найдено Наличие проекта планоровки территории (ППТ) в границах ГПЗУ.\n");
        }

        //TODO парсинг Реквизиты документа ППТ

        //TODO парсинг Реквизиты межевания

        String codesVRI = parseSutCodes(uplpDoc, parsedText);

        uplpDoc.setSutCodes(codesVRI);
        if (uplpDoc.getSutCodes().equals("-")) {
            parseErrors.append("Не найдены Коды основных видов разрешенного использования (ВРИ) зумельного участка (ЗУ).\n");
        }

        parsePlotArea(uplpDoc, words, parseErrors);

        String subzones = parseSubzones(uplpDoc, words);

        uplpDoc.setSubzonesAvailability(subzones);

        parse58Attr(uplpDoc, parsedText);
        System.out.println("=============================");

        System.out.println();

        System.out.println(uplpDoc.toString());

        return docs;
    }

    private void parse58Attr(UplpDoc uplpDoc, String parsedText) {
        if (parsedText.contains("3.2. Объекты, включенные в единый государственный реестр объектов культурного наследия (памятников \r\nистории и культуры) народов Российской Федерации")) {
            String[] innerData = parsedText.split("3.2. Объекты, включенные в единый государственный реестр объектов культурного наследия \\(памятников \r\nистории и культуры\\) народов Российской Федерации");
            String afterMathches = innerData[1];
            if (afterMathches.toLowerCase().startsWith("\r\nне имеются") ||
                    afterMathches.toLowerCase().startsWith("\r\nинформация отсутствует")) {
                uplpDoc.setCloAvailability("Отсутствуют");
            } else {
                uplpDoc.setCloAvailability("Присутствуют");
            }
        } else {
            uplpDoc.setCloAvailability("Отсутствуют");
        }
    }

    /**
     * Парсинг подзон и атрибутов:<br>
     * - Наличие подзон ЗУ, номера<br>
     * - Площади подзон ЗУ, кв.м (пока только для одной подзоны)
     * @param words текстовый массив строк документов
     * @return текст с номерами подзон
     */
    private String parseSubzones(UplpDoc uplpDoc, String[] words) {
        String subzones = "";

        String areaSquare = "";

        String metrics = "";

        for(String word : words){
            if (word.matches("Подзона №.\\d+.+")){
                subzones +=
                        (subzones.length() > 0 ? "\n" : "")+
                        "№ "+word.split(" ")[2];
                areaSquare = word.split(" на чертеже ")[1];
                System.out.println(word);

                metrics = areaSquare.split(" ")[1];

                metrics = metrics.substring(0, metrics.length()-2);

                System.out.println(metrics);

                areaSquare = areaSquare.split(" ")[0].replace('(',' ').strip();

                float area = 0f;

                switch (metrics.toLowerCase()){
                    case "га":{
                        area = Float.parseFloat(areaSquare) * 10000;
                        uplpDoc.setSubzonesArea((int) area);
                        break;
                    }
                    case "кв.м":{
                        uplpDoc.setSubzonesArea(Integer.parseInt(areaSquare));
                        break;
                    }
                }

                System.out.println(""+area);

                System.out.println(areaSquare);
            }
        }
        if(subzones.equals("")){
              subzones = "нет";
          }
        return subzones;
    }

    /**
     * Парсинг атрибута "Площадь земельного  участка (ЗУ), кв.м"
     * @param words текстовый массив документа
     */
    private void parsePlotArea(UplpDoc uplpDoc, String[] words, StringBuilder parseErrors) {
        for(String word : words){
            if(word.matches("\\d+ ± \\d.+|\\d+. кв.м")){
                String plotArea = word.split(" ")[0];
                System.out.println(plotArea);
                uplpDoc.setPlotArea(Integer.parseInt(plotArea));
            }
        }
        if (uplpDoc.getPlotArea() == null) {
            parseErrors.append("Не найдена Площадь земельного участка (ЗУ), кв.м.\n");
        }
    }

    /**
     * Открывает файл и обрабатывает структуру документа с помощью внешней библиотеки парсинга.
     * Возвращает полученный текст в формате одной переменной
     * @param file pdf-документ ГПЗУ для парсинга
     * @return текст документа в формате строки с переносами
     * @throws IOException
     */
    private static String prepareFile(File file) throws IOException {
        String parsedText;

        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();

        COSDocument cosDoc = parser.getDocument();

        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        parsedText = pdfStripper.getText(pdDoc);
        return parsedText;
    }

    /**
     * Парсинг атрибута "Наименование условной группы использования ЗУ по ВРИ"
     * Заполняет соответствующее поле в документе и возвращает набор кодов.
     * @param parsedText "сырая" строка с текстом документа
     * @return атрибут "Коды основных видов разрешенного использования (ВРИ) земельного  участка (ЗУ)"
     */
    private String parseSutCodes(UplpDoc uplpDoc, String parsedText) {
        String[] words;
        words = parsedText.split(" ");

        String codeVRI = "";

        String codesVRI = "";

        for(String word : words){
            //System.out.println(word);
            if(word.matches("\\d.\\d.\\d+,")
                    || word.matches("\\(\\d.\\d.\\d\\)")){
                System.out.println(word.split(",")[0] + " is matched");
                codesVRI += (codesVRI.length() > 0 ? " " : "") + word.split(",")[0].strip() + ";";
            }
            if(word.matches("\\(\\d.\\d.\\d\\)\r\n.+|\\(\\d.\\d.\\d\\)\n.+")){
                codeVRI = word.split("\n")[0].trim().strip();
                codesVRI += (codesVRI.length() > 0 ? " " : "") +
                            codeVRI
                            .replace('(',' ')
                            .replace(')',' ')
                            .strip() + ";";
                System.out.println(codeVRI);
                System.out.println(codesVRI);
            }
            if(word.matches("Действие градостроительного регламента не распространяется")){
                codesVRI = word;
                break;
            }
        }

        uplpDoc.setSutGroupName(checkLivingPlace(codesVRI));

        return codesVRI.substring(0, codesVRI.length()-1);
    }

    private String checkLivingPlace(String codesVRI) {

        String sutStatus = "";

        for(String code : codesVRI.split("; |;")){
            if(sutStatus.equals(SUT_MIXED)){
                break;
            }
            if(code.matches("2\\.\\d|2.[0-7].\\d|13.2")){
                sutStatus = sutStatus.equals(SUT_NOT_LIVING) ? SUT_MIXED : SUT_LIVING;
                System.out.println(sutStatus);
            } else {
                sutStatus = sutStatus.equals(SUT_LIVING) ? SUT_MIXED : SUT_NOT_LIVING;
                System.out.println(sutStatus);
            }
        }
        return sutStatus;
    }

    /**
     * Парсинг атрибута "Наличие проекта планировки территории (ППТ) в границах ГПЗУ реквизиты документа"
     * @param words текстовый массив документа
     * @return строка наличия проекта планировки
     */
    private String parseAvailability(String[] words) {
        String availability = "";

        for(String word : words){
            if(word.startsWith("Проект планировки территории")){
               availability = word.split("Проект планировки территории ")[1];
               break;
            }

            if(word.startsWith("Проект планировки ")){
                availability = word.split("Проект планировки ")[1];
                break;
            }
        }

        availability = (availability.split("\\. |, ").length > 0 ? availability.split("\\. |, ")[0] : availability);

        System.out.println(availability);
        return availability;
    }

    /**
     * Парсинг атрибута "Кадастровый номер земельного участка (ЗУ) или условный номер"
     * @param words текстовый массив документа
     */
    private void parseCadastralNumber(UplpDoc uplpDoc, String[] words) {
        for(String word : words){//77:06:0003002:70 //02/01/10179
            if(word.matches("\\d{2}:\\d{2}:\\d{7}:\\d+")
                    || word.matches("\\d{2}/\\d{2}/\\d{5}")
                || word.matches("участок №\\d+|участок \\d+")){
                System.out.println(word);
                uplpDoc.setCadastralNo(word);
                break;
            } else {
                uplpDoc.setCadastralNo("Не определено");
            }
        }
    }

    /**
     * Парсинг атрибута "Строительный адрес"
     * @param words массив строк документа
     * @return строительный адрес
     */
    private String parseBuildingAddress(String[] words) {
        String line = "";

        for(int i = 0; i < words.length/2; i++){
            if(words[i].startsWith("г. Москва")){
                int j = i;
                while (!words[j].startsWith("Описание границ")){
                    line += words[j];
                    j++;
                }
            }
        }

        System.out.println(line);

        String[] lines = line.split(", ");

        Stream<String> sLines = Arrays.stream(lines).dropWhile(this::checkRegion);

        String s = sLines.collect(Collectors.joining(", "));


        System.out.println(s);
        return s;
    }


    /**
     * Производит парсинг первых пяти атрибутов:<br>
     * - Уникальный номер записи <br>
     * - Номер документа ГПЗУ<br>
     * - Дата выдачи ГПЗУ<br>
     * - Статус ГПЗУ <br>
     * - Срок действия ГПЗУ<br>
     * @param words набор строк из документа
     */
    private void parse1to5Attributes(UplpDoc uplpDoc, String[] words, StringBuilder parseErrors){
        for(String word : words) {
            if (word.startsWith("№ RU")) {
                uplpDoc.setUplpNo(parseNumber(word));

                System.out.println(uplpDoc.getUplpNo());

                continue;
            }

            if (word.matches("Дата выдачи (\\d{2}.\\d{2}.\\d{4})|(\\d{2}.\\d{2}.\\d{4}\r)")) {

                System.out.println(word);

                uplpDoc.setDateOfIssue(parseIssueDate(word));

//                LocalDate issueEndDate = parseExpiryDate(uplpDoc.getDateOfIssue());
                LocalDate issueEndDate = parseExpiryDate(new SimpleDateFormat("dd.MM.yyyy").format(uplpDoc.getDateOfIssue()));

//                uplpDoc.setExpiryDate(issueEndDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

                uplpDoc.setExpiryDate(Date.from(issueEndDate.atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()));

                uplpDoc.setUplpStatus(checkActual(issueEndDate));

                continue;
            }

            if (word.contains("обращения")) {
                System.out.println(word);
            }
        }
        if (uplpDoc.getUplpNo().equals("-")) {
            parseErrors.append("Не найден номер ГПЗУ.\n");
        }
        if (uplpDoc.getDateOfIssue() == null) {
            parseErrors.append("Не найдена Дата выдачи ГПЗУ.\n");
        }
        if (uplpDoc.getExpiryDate() == null) {
            parseErrors.append("Не найден Срок действия ГПЗУ.\n");
        }
        if (uplpDoc.getUplpStatus().equals("-")) {
            parseErrors.append("Не найден Статус ГПЗУ.\n");
        }
    }

    private String parseNumber(String word){
        String uplpNum = word.split("\n")[0].substring(2);

        return uplpNum;
    }

//    private static String parseIssueDate(String word) {
//        String issueStartDateRaw = word.split(" ")[2];
//
//        return issueStartDateRaw;
//    }

    private Date parseIssueDate(String word) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        try {
            return formatter.parse(word.split(" ")[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private LocalDate parseExpiryDate(String issueStartDateRaw) {
        String[] issueStartDateRawSplit = issueStartDateRaw.split("\\.");

        LocalDate issueDate = LocalDate.parse(issueStartDateRawSplit[2] + issueStartDateRawSplit[1] + issueStartDateRawSplit[0], DateTimeFormatter.BASIC_ISO_DATE);

        LocalDate issueEndDate = issueDate.plusYears(3);

        if (isEndInFirstPeriod(issueEndDate) || isEndInSecondPeriod(issueEndDate)) {
            issueEndDate = issueEndDate.plusYears(1);
        }

        return issueEndDate;
    }

    private String checkActual(LocalDate issueEndDate){
        LocalDate today = LocalDate.now();

        if(issueEndDate.isAfter(today)){
            return ISSUE_DATE_ACTUAL;
        } else {
            return ISSUE_DATE_EXPIRED;
        }
    }

    private boolean isEndInFirstPeriod(LocalDate issueEndDate){
        LocalDate firstPeriodStart = LocalDate.of(2020, 4,6);

        LocalDate firstPeriodEnd = LocalDate.of(2021, 1,1);

        return issueEndDate.isAfter(firstPeriodStart.minusDays(1)) && issueEndDate.isBefore(firstPeriodEnd.plusDays(1));
    }

    private boolean isEndInSecondPeriod(LocalDate issueEndDate){
        LocalDate secondPeriodStart = LocalDate.of(2022, 4,13);

        LocalDate secondPeriodEnd = LocalDate.of(2023, 1,1);

        return issueEndDate.isAfter(secondPeriodStart.minusDays(1)) && issueEndDate.isBefore(secondPeriodEnd.plusDays(1));
    }

    /**
     * Парсинг атрибута "Правообладатель или иной получатель ГПЗУ"
     * @param words массив строк
     * @return атрибут правообладателя
     */
    private String parseRecipient(String[] words){
        int i = 0;

        int start = 0;

        int end = 0;

        for(String word : words){
            if(word.contains("обращения")){
                start = i;
            }
            i++;
        }
        for(int j = start; j < 10; j++){
            if(words[j].contains(" от ")){
                end = j;
            }
        }

        String line = words[start].split("обращения ")[1];
        for(int k = start+1; k <= end; k++){
            line += words[k];
        }

        line = line.split(" от ")[0];

        System.out.println(line);

        System.out.println(words[start]);

        System.out.println(words[end]);

        return line;
    }

    /**
     * Проверка типа правообладателя. Возвращает атрибут "Тип правообладателя или получателя ГПЗУ"
     * @param recipient имя правообладателя
     * @return тип правообладателя
     */
    private String checkRecipientType(String recipient){
        recipient = recipient.toLowerCase();
            if(recipient.contains("фонд") ||
                    recipient.contains("фонда") ||
                recipient.contains("общество") ||
                    recipient.contains("общества") ||
                recipient.contains("государственное") ||
                recipient.contains("муниципальное") ||
                    recipient.contains("департамента")||
                recipient.contains("организации")){
                return "ЮЛ";
            } else {
                return "ФЛ";
            }
    }

    private boolean checkRegion(String s){
        return !(s.startsWith("ул.")|| s.endsWith(" ул."))
                && !s.startsWith("проезд")
                && !s.startsWith("посёлок")
                && !s.startsWith("шоссе")
                && !s.startsWith("проспект")
                && !s.startsWith("наб.");
    }
}