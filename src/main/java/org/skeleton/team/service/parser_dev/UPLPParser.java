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

    private static UplpDoc uplpDoc;

    public static void main(String[] args) throws IOException {

        File file = new File("RU77106000-043720-GPZU.pdf");

        parsingUPLPFile(file);

/*
        File file = new File(".");

        for(String uplp : file.list()){
            if(uplp.endsWith(".pdf")) {
                File uplpFile = new File(uplp);
                parsingUPLPFile(uplpFile);
            }
        }
*/


    }

    private static void parsingUPLPFile(File file) throws IOException {
        String parsedText;

        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();

        COSDocument cosDoc = parser.getDocument();

        PDFTextStripper pdfStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        parsedText = pdfStripper.getText(pdDoc);

        String[] words = parsedText.split("\n|\r\n");

        uplpDoc = new UplpDoc();

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

        for(String word : words){
            if(word.matches("\\d+ ± \\d.+|\\d+. кв.м")){
                String plotArea = word.split(" ")[0];
                System.out.println(plotArea);
                uplpDoc.setPlotArea(Integer.parseInt(plotArea));
            }
        }

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

        uplpDoc.setSubzonesAvailability(subzones);

        /*for(String word : words){
            if(word.startsWith("г. Москва")){
                System.out.println(word);
            }
        }*/


        System.out.println("=============================");

        System.out.println();

        System.out.println(uplpDoc.toString());
    }

    private static String parseSutCodes(String parsedText) {
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

    private static String checkLivingPlace(String codesVRI) {

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

    private static String parseAvailability(String[] words) {
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

    private static void parseCadastralNumber(String[] words) {
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

    private static String parseBuildingAddress(String[] words) {
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

        Stream<String> sLines = Arrays.stream(lines).dropWhile(UPLPParser::checkRegion);

        String s = sLines.collect(Collectors.joining(", "));


        System.out.println(s);
        return s;
    }


    private static void parse1to5Attributes(String[] words){
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

    private static String parseNumber(String word){
        String uplpNum = word.split("\n")[0].substring(2);

        return uplpNum;
    }

//    private static String parseIssueDate(String word) {
//        String issueStartDateRaw = word.split(" ")[2];
//
//        return issueStartDateRaw;
//    }

    private static Date parseIssueDate(String word) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        try {
            return formatter.parse(word.split(" ")[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LocalDate parseExpiryDate(String issueStartDateRaw) {
        String[] issueStartDateRawSplit = issueStartDateRaw.split("\\.");

        LocalDate issueDate = LocalDate.parse(issueStartDateRawSplit[2] + issueStartDateRawSplit[1] + issueStartDateRawSplit[0], DateTimeFormatter.BASIC_ISO_DATE);

        LocalDate issueEndDate = issueDate.plusYears(3);

        if (isEndInFirstPeriod(issueEndDate) || isEndInSecondPeriod(issueEndDate)) {
            issueEndDate = issueEndDate.plusYears(1);
        }

        return issueEndDate;
    }

    private static String checkActual(LocalDate issueEndDate){
        LocalDate today = LocalDate.now();

        if(issueEndDate.isAfter(today)){
            return ISSUE_DATE_ACTUAL;
        } else {
            return ISSUE_DATE_EXPIRED;
        }
    }

    private static boolean isEndInFirstPeriod(LocalDate issueEndDate){
        LocalDate firstPeriodStart = LocalDate.of(2020, 4,6);

        LocalDate firstPeriodEnd = LocalDate.of(2021, 1,1);

        return issueEndDate.isAfter(firstPeriodStart.minusDays(1)) && issueEndDate.isBefore(firstPeriodEnd.plusDays(1));
    }

    private static boolean isEndInSecondPeriod(LocalDate issueEndDate){
        LocalDate secondPeriodStart = LocalDate.of(2022, 4,13);

        LocalDate secondPeriodEnd = LocalDate.of(2023, 1,1);

        return issueEndDate.isAfter(secondPeriodStart.minusDays(1)) && issueEndDate.isBefore(secondPeriodEnd.plusDays(1));
    }

    private static String parseRecipient(String[] words){
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

    private static String checkRecipientType(String recipient){
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

    private static boolean checkRegion(String s){
        return !(s.startsWith("ул.")|| s.endsWith(" ул."))
                && !s.startsWith("проезд")
                && !s.startsWith("посёлок")
                && !s.startsWith("шоссе")
                && !s.startsWith("проспект")
                && !s.startsWith("наб.");
    }
}