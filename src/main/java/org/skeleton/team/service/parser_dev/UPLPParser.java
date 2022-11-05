package org.skeleton.team.service.parser_dev;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.skeleton.team.entity.UplpDoc;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UPLPParser {

    private static final String ISSUE_DATE_ACTUAL = "Действует";
    private static final String ISSUE_DATE_EXPIRED = "Срок действия истёк";

    private static final String SECRET = "Секретно";
    public static final String SUT_NOT_LIVING = "Не жилая";
    public static final String SUT_MIXED = "Смешанная";
    public static final String SUT_LIVING = "Жилая";

    private UplpDoc uplpDoc;

    /**
     * Производит последовательный парсинг документа согласно требуемому регламенту и возвращает сущность документа с заполненными полями.
     * @param file - pdf-документ ГПЗУ для парсинга
     * @return документ с парсенными полями
     * @throws IOException
     */
    public UplpDoc parsingUPLPFile(File file) throws IOException {

        uplpDoc = new UplpDoc();

        //Подготавливаем документ и получаем строку с текстом документа
        String parsedText = prepareFile(file);

        //Разбиваем строку на массив отдельных строк в зависимости от ОС (на Windows и UNIX системах разные коды для переноса строки)
        String[] words = parsedText.split("\n|\r\n");

        //Парсинг первых пяти атрибутов
        parse1to5Attributes(words);

        //TODO достать библиотеку склонений

        uplpDoc.setUplpRecipient(parseRecipient(words));

        uplpDoc.setRecipientType(checkRecipientType(uplpDoc.getUplpRecipient()));

        //TODO парсинг округа и района

        String s = parseBuildingAddress(words);

        uplpDoc.setBuildingAddress(s);

        parseCadastralNumber(words);

        String availability = parseAvailability(words);

        uplpDoc.setTlpProjectAvailability(availability);

        //TODO парсинг Реквизиты документа ППТ

        //TODO парсинг Реквизиты межевания

        String codesVRI = parseSutCodes(parsedText);

        uplpDoc.setSutCodes(codesVRI);

        parsePlotArea(words);

        String subzones = parseSubzones(words);

        uplpDoc.setSubzonesAvailability(subzones);

        System.out.println("=============================");

        System.out.println();

        System.out.println(uplpDoc.toString());

        return uplpDoc;
    }

    /**
     * Парсинг подзон и атрибутов:<br>
     * - Наличие подзон ЗУ, номера<br>
     * - Площади подзон ЗУ, кв.м (пока только для одной подзоны)
     * @param words текстовый массив строк документов
     * @return текст с номерами подзон
     */
    private String parseSubzones(String[] words) {
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
    private void parsePlotArea(String[] words) {
        for(String word : words){
            if(word.matches("\\d+ ± \\d.+|\\d+. кв.м")){
                String plotArea = word.split(" ")[0];
                System.out.println(plotArea);
                uplpDoc.setPlotArea(Integer.parseInt(plotArea));
            }
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
    private String parseSutCodes(String parsedText) {
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
    private void parseCadastralNumber(String[] words) {
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
    private void parse1to5Attributes(String[] words){
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