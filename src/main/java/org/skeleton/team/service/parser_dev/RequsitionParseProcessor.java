package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RequsitionParseProcessor {

    private static final String ISSUE_DATE_ACTUAL = "Действует";
    private static final String ISSUE_DATE_EXPIRED = "Срок действия истёк";

    private static final LocalDate FIRST_PERIOD_START = LocalDate.of(2020, 4,6);
    private static final LocalDate FIRST_PERIOD_END = LocalDate.of(2021, 1,1);

    private static final LocalDate SECOND_PERIOD_START = LocalDate.of(2022, 4,13);
    private static final LocalDate SECOND_PERIOD_END = LocalDate.of(2023, 1,1);

    /**
     * Обрезает и возвращает строку с номером документа вида RU1234567-12345 или <br>
     * РФ-12-34-56-7-89-1011-1213
     * @param line строка с номером
     * @return обрезанная строка
     */
    String parseNumber(String line){
        String uplpNum = line.split("\n")[0].substring(2);

        return uplpNum;
    }

    /**
     * Конвертирует входную строку с датой выдачи в формат класса Date
     * @param line
     * @return дата выдачи
     */
    Date parseIssueDate(String line) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        try {
            return formatter.parse(line.split(" ")[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Вычисляет дату окончания (срок действия) действия ГПЗУ и возвращает в формате LocalDate
     * @param issueStartDateRaw "сырая строка" с датой
     * @return срок действия ГПЗУ
     */
    LocalDate parseExpiryDate(String issueStartDateRaw) {
        String[] issueStartDateRawSplit = issueStartDateRaw.split("\\.");

        LocalDate issueDate = LocalDate.parse(issueStartDateRawSplit[2] + issueStartDateRawSplit[1] + issueStartDateRawSplit[0], DateTimeFormatter.BASIC_ISO_DATE);

        LocalDate issueEndDate = issueDate.plusYears(3);

        if (isEndInPeriod(issueEndDate,FIRST_PERIOD_START, FIRST_PERIOD_END) || isEndInPeriod(issueEndDate, SECOND_PERIOD_START, SECOND_PERIOD_END)) {
            issueEndDate = issueEndDate.plusYears(1);
        }

        return issueEndDate;


    }

    /**
     * Проверка попадания даты окончания действия документа на указанный период
     * @param issueEndDate дата окончания действия документа
     * @param startPeriod начало периода
     * @param endPeriod конец периода
     * @return true или false в зависимости от попадния даты в интервал
     */
    private boolean isEndInPeriod(LocalDate issueEndDate, LocalDate startPeriod, LocalDate endPeriod){
        return issueEndDate.isAfter(startPeriod.minusDays(1)) && issueEndDate.isBefore(endPeriod.plusDays(1));
    }

    /**
     * Проверка статуса ГПЗУ
     * @param issueEndDate
     * @return
     */
    String checkActual(LocalDate issueEndDate) {
        LocalDate today = LocalDate.now();

        if (issueEndDate.isAfter(today)) {
            return ISSUE_DATE_ACTUAL;
        } else {
            return ISSUE_DATE_EXPIRED;
        }
    }

    /**
     * Парсинг атрибута "Правообладатель или иной получатель ГПЗУ"
     * @param words массив строк
     * @return атрибут правообладателя
     */
    String parseRecipient(String[] words){
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

        return line;
    }

    /**
     * Проверка типа правообладателя. Возвращает атрибут "Тип правообладателя или получателя ГПЗУ"
     *
     * @param recipient имя правообладателя
     * @return тип правообладателя
     */
    String checkRecipientType(String recipient){
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

    void outputUplpRequisitionData(UplpDoc uplpDoc) {
        System.out.println("==================== ВЫВОД ДАННЫХ ДОКУМЕНТА НА ЭТАПЕ I ====================");

        System.out.println(
                uplpDoc.getUplpNo() + "\n" +
                        uplpDoc.getRecordNo() + "\n" +
                        uplpDoc.getDateOfIssue() + "\n" +
                        uplpDoc.getExpiryDate() + "\n" +
                        uplpDoc.getUplpStatus() + "\n" +
                        uplpDoc.getUplpRecipient() + "\n" +
                        uplpDoc.getRecipientType() + "\n"
        );

        System.out.println("===========================================================================");
    }

}
