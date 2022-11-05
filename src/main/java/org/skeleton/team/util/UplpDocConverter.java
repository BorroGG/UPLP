package org.skeleton.team.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.skeleton.team.entity.UplpDocCollection;
import org.skeleton.team.entity.UplpSimpleDoc;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Класс для преобразования документов ГПЗУ.
 */
@Component
public class UplpDocConverter {

    /**
     * Конвертирование в xml.
     * @param docs документы ГПЗУ для конвертации
     * @param outputStream поток данных ответа сервера
     */
    public void convertUplpDocsToXmlStream(List<UplpSimpleDoc> docs, OutputStream outputStream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(UplpSimpleDoc.class, UplpDocCollection.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            UplpDocCollection collection = new UplpDocCollection(docs);
            jaxbMarshaller.marshal(collection, outputStream);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Не получилось сконвертировать документы, ошибка: %s", e.getMessage()));
        }
    }

    /**
     * Конвертирование в xlsx.
     * @param docs документы ГПЗУ для конвертации
     * @param outputStream поток данных ответа сервера
     */
    public void convertUplpDocsToXlsxStream(List<UplpSimpleDoc> docs, OutputStream outputStream) {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Таблицы ГПЗУ");

        Row row_2 = sheet.createRow(1);
        row_2.createCell(0).setCellValue("Таблица обработки Градостроительных планов земельных участков (ГПЗУ)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A2:P2"));

        Row row_5 = sheet.createRow(4);
        row_5.createCell(0).setCellValue("№\nп/п\n");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A5:A8"));
        row_5.createCell(1).setCellValue("Паспорт (карточка) ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("B5:P5"));
        row_5.createCell(16).setCellValue("Виды использования земельного участка (ЗУ)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("Q5:R5"));
        row_5.createCell(18).setCellValue("Основные показатели ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("S5:Y5"));
        row_5.createCell(25).setCellValue("Параметры и площади строящихся и реконструируемых объектов капитального строительства (ОКС) на ЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("Z5:AX5"));
        row_5.createCell(50).setCellValue("Параметры и площади существующих на ЗУ объектов капитального строительства (ОКС)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AY5:BK5"));

        Row row_6 = sheet.createRow(5);
        row_6.createCell(1).setCellValue("Реквизиты ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("B6:H6"));
        row_6.createCell(8).setCellValue("Территория (Местоположение) земельного участка (ЗУ)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("I6:P6"));
        row_6.createCell(16).setCellValue("Наименование условной группы использования\nЗУ по ВРИ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("Q6:Q8"));
        row_6.createCell(17).setCellValue("Коды основных видов разрешенного использования (ВРИ)\nземельного  участка (ЗУ)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("R6:R8"));
        row_6.createCell(18).setCellValue("Площадь \nземельного\n участка (ЗУ),\nкв.м");
        sheet.addMergedRegion(CellRangeAddress.valueOf("S6:S8"));
        row_6.createCell(19).setCellValue("Наличие\nподзон ЗУ,\nномера");
        sheet.addMergedRegion(CellRangeAddress.valueOf("T6:T8"));
        row_6.createCell(20).setCellValue("Площади \nподзон ЗУ, \nкв.м");
        sheet.addMergedRegion(CellRangeAddress.valueOf("U6:U8"));
        row_6.createCell(21).setCellValue("Предельные параметры разрешенного строительства, реконструкции объектов капитального строительства (ОКС)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("V6:Y6"));
        row_6.createCell(25).setCellValue("Назначение ОКС");
        sheet.addMergedRegion(CellRangeAddress.valueOf("Z6:Z8"));
        row_6.createCell(26).setCellValue("Наименование, описание ОКС");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AA6:AA8"));
        row_6.createCell(27).setCellValue("Наличие объектов\nна которые действие градостроительного\nрегламента не распространяется или не устанавливаеться");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AB6:AB8"));
        row_6.createCell(28).setCellValue("Суммарная поэтажная площадь наземной части зданий и сооружений в габаритах наружных стен, кв.м");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AC6:AG6"));
        row_6.createCell(33).setCellValue("Общая площадь зданий и сооружений, кв.м");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AH6:AM6"));
        row_6.createCell(39).setCellValue("Суммарная площадь зданий и сооружений по всем подзонам, кв.м");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AN6:AX6"));
        row_6.createCell(50).setCellValue("Параметры и характеристики ОКС расположенных на ЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AY6:BK6"));

        int cellNum = 1;

        Row row_7 = sheet.createRow(6);
        Row row_8 = sheet.createRow(7);
        row_7.createCell(cellNum++).setCellValue("Уникальный номер\nзаписи");
        sheet.addMergedRegion(CellRangeAddress.valueOf("B7:B8"));
        row_7.createCell(cellNum++).setCellValue("Номер ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("C7:C8"));
        row_7.createCell(cellNum++).setCellValue("Дата выдачи ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("D7:D8"));
        row_7.createCell(cellNum++).setCellValue("Статус ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("E7:E8"));
        row_7.createCell(cellNum++).setCellValue("Срок действия ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("F7:F8"));
        row_7.createCell(cellNum++).setCellValue("Правообладатель или иной получатель ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("G7:G8"));
        row_7.createCell(cellNum++).setCellValue("Тип правообладателя или получателя ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("H7:H8"));
        row_7.createCell(cellNum++).setCellValue("Административный округ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("I7:I8"));
        row_7.createCell(cellNum++).setCellValue("Район (поселение)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("J7:J8"));
        row_7.createCell(cellNum++).setCellValue("Строительный адрес");
        sheet.addMergedRegion(CellRangeAddress.valueOf("K7:K8"));
        row_7.createCell(cellNum++).setCellValue("Кадастровый номер земельного участка (ЗУ)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("L7:L8"));
        row_7.createCell(cellNum++).setCellValue("Наличие проекта планировки территории (ППТ) \nв границах ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("M7:M8"));
        row_7.createCell(cellNum++).setCellValue("Реквизиты документа\nППТ\n");
        sheet.addMergedRegion(CellRangeAddress.valueOf("N7:N8"));
        row_7.createCell(cellNum++).setCellValue("Наличие отдельного проекта межевания территории в границах ГПЗУ");
        sheet.addMergedRegion(CellRangeAddress.valueOf("O7:O8"));
        row_7.createCell(cellNum++).setCellValue("Реквизиты документа\nпроекта межевания");
        sheet.addMergedRegion(CellRangeAddress.valueOf("P7:P8"));

        row_8.createCell(cellNum++).setCellValue("Наименование условной группы использования\nЗУ по ВРИ");
        row_8.createCell(cellNum++).setCellValue("Коды основных видов разрешенного использования (ВРИ)\nземельного  участка (ЗУ)");
        row_8.createCell(cellNum++).setCellValue("Площадь \nземельного\n участка (ЗУ),\nкв.м");
        row_8.createCell(cellNum++).setCellValue("Наличие\nподзон ЗУ,\nномера");
        row_8.createCell(cellNum++).setCellValue("Площади \nподзон ЗУ, \nкв.м");

        row_7.createCell(cellNum++).setCellValue("Высота застройки,\nм");
        sheet.addMergedRegion(CellRangeAddress.valueOf("V7:V8"));
        row_7.createCell(cellNum++).setCellValue("Количество этажей,\nшт");
        sheet.addMergedRegion(CellRangeAddress.valueOf("W7:W8"));
        row_7.createCell(cellNum++).setCellValue("Процент застроенности, \n%");
        sheet.addMergedRegion(CellRangeAddress.valueOf("X7:X8"));
        row_7.createCell(cellNum++).setCellValue("Плотность застройки,\nтыс. кв. м/га");
        sheet.addMergedRegion(CellRangeAddress.valueOf("Y7:Y8"));

        row_8.createCell(cellNum++).setCellValue("Назначение ОКС");
        row_8.createCell(cellNum++).setCellValue("Наименование, описание ОКС");
        row_8.createCell(cellNum++).setCellValue("Наличие объектов\nна которые действие градостроительного\nрегламента не распространяется или не устанавливаеться");

        row_7.createCell(cellNum++).setCellValue("Всего");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AC7:AC8"));
        row_7.createCell(cellNum++).setCellValue("Жилой застройки");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AD7:AD8"));
        row_7.createCell(cellNum++).setCellValue("Нежилой застройки");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AE7:AE8"));
        row_7.createCell(cellNum++).setCellValue("Жилых помещений");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AF7:AF8"));
        row_7.createCell(cellNum++).setCellValue("Встроенно-пристроенных, отдельно стоящих нежилых помещений");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AG7:AG8"));
        row_7.createCell(cellNum++).setCellValue("Всего");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AH7:AH8"));
        row_7.createCell(cellNum++).setCellValue("Жилой застройки");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AI7:AI8"));
        row_7.createCell(cellNum++).setCellValue("Нежилой застройки");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AJ7:AJ8"));
        row_7.createCell(cellNum++).setCellValue("Жилых помещений");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AK7:AK8"));
        row_7.createCell(cellNum++).setCellValue("Встроенно-пристроенных, отдельно стоящих нежилых помещений");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AL7:AL8"));
        row_7.createCell(cellNum++).setCellValue("Подземного пространства");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AM7:AM8"));

        row_7.createCell(cellNum).setCellValue("Суммарная поэтажная площадь наземной части в габаритах наружных стен");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AN7:AR7"));

        row_8.createCell(cellNum++).setCellValue("Всех объектов");
        row_8.createCell(cellNum++).setCellValue("Жилых объектов");
        row_8.createCell(cellNum++).setCellValue("Нежилых объектов");
        row_8.createCell(cellNum++).setCellValue("Жилых помещений");
        row_8.createCell(cellNum++).setCellValue("Встроенно-пристроенных, отдельно стоящих нежилых помещений");

        row_7.createCell(cellNum).setCellValue("Общая площадь");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AS7:AX7"));

        row_8.createCell(cellNum++).setCellValue("Всех объектов");
        row_8.createCell(cellNum++).setCellValue("Жилых объектов");
        row_8.createCell(cellNum++).setCellValue("Нежилых объектов");
        row_8.createCell(cellNum++).setCellValue("Жилых помещений");
        row_8.createCell(cellNum++).setCellValue("Встроенно-пристроенных, отдельно стоящих нежилых помещений");
        row_8.createCell(cellNum++).setCellValue("Подземного пространства");

        row_7.createCell(cellNum++).setCellValue("Наличие или отсутствие существующих на ЗУ ОКС");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AY7:AY8"));
        row_7.createCell(cellNum++).setCellValue("Общее число существующих \nОКС, \nединиц");
        sheet.addMergedRegion(CellRangeAddress.valueOf("AZ7:AZ8"));
        row_7.createCell(cellNum++).setCellValue("Назначение существующих ОКС");
        sheet.addMergedRegion(CellRangeAddress.valueOf("BA7:BA8"));
        row_7.createCell(cellNum++).setCellValue("Наименование, описание существующих ОКС");
        sheet.addMergedRegion(CellRangeAddress.valueOf("BB7:BB8"));
        row_7.createCell(cellNum++).setCellValue("Максимальное число наземных этажей существующих ОКС, шт");
        sheet.addMergedRegion(CellRangeAddress.valueOf("BC7:BC8"));

        row_7.createCell(cellNum).setCellValue("Общая площадь существующих ОКС, кв.м");
        sheet.addMergedRegion(CellRangeAddress.valueOf("BD7:BF7"));

        row_8.createCell(cellNum++).setCellValue("Общая");
        row_8.createCell(cellNum++).setCellValue("Жилых объектов");
        row_8.createCell(cellNum++).setCellValue("Нежилых объектов");

        row_7.createCell(cellNum).setCellValue("Объекты,включенные в единый государственный реестр объектов культурного наследия (ОКН)");
        sheet.addMergedRegion(CellRangeAddress.valueOf("BG7:BK7"));

        row_8.createCell(cellNum++).setCellValue("Наличие или отсутствие существующих на ЗУ ОКН");
        row_8.createCell(cellNum++).setCellValue("Общее число \nсуществующих на ЗУ ОКН");
        row_8.createCell(cellNum++).setCellValue("Наименование, описание ОКН");
        row_8.createCell(cellNum++).setCellValue("Идентификационный номер ОКН");
        row_8.createCell(cellNum++).setCellValue("Регистрационный номер ОКН");

        int rowNum = 8;

        Row row_9 = sheet.createRow(rowNum);
        for (int i = 1; i < cellNum; i++) {
            row_9.createCell(i).setCellValue(i);
        }

        for (int i = 0; i < docs.size(); i++) {
            addDataToFile(sheet, ++rowNum, docs.get(i), i);
        }

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        setRowStyle(row_2, cellStyle);
        setRowStyle(row_5, cellStyle);
        setRowStyle(row_6, cellStyle);
        setRowStyle(row_7, cellStyle);
        setRowStyle(row_8, cellStyle);
        setRowStyle(row_9, cellStyle);

        try {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format("Не получилось сконвертировать документы, ошибка: %s", e.getMessage()));
        }
    }

    /**
     * Задание стиля строк таблицы
     * @param row строка
     * @param cellStyle стиль
     */
    private void setRowStyle(Row row, CellStyle cellStyle) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            if (row.getCell(i) != null) {
                row.getCell(i).setCellStyle(cellStyle);
            }
        }
    }

    /**
     * Добавление данных в файл
     * @param sheet лист
     * @param rowNum номер строки
     * @param uplpDoc документ ГПЗУ
     * @param index индекс
     */
    private void addDataToFile(Sheet sheet, int rowNum, UplpSimpleDoc uplpDoc, int index) {
        int cellNum = 0;

        Row row = sheet.createRow(rowNum);

        row.createCell(cellNum++).setCellValue(index + 1);
        row.createCell(cellNum++).setCellValue(uplpDoc.getRecordNo());
        row.createCell(cellNum++).setCellValue(uplpDoc.getUplpNo());
        row.createCell(cellNum++).setCellValue(uplpDoc.getDateOfIssue() != null ? new SimpleDateFormat("dd.MM.yyyy").format(uplpDoc.getDateOfIssue()) : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getUplpStatus());
        row.createCell(cellNum++).setCellValue(uplpDoc.getExpiryDate() != null ? new SimpleDateFormat("dd.MM.yyyy").format(uplpDoc.getExpiryDate()) : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getUplpRecipient());
        row.createCell(cellNum++).setCellValue(uplpDoc.getRecipientType());
        row.createCell(cellNum++).setCellValue(uplpDoc.getAdministrativeArea());
        row.createCell(cellNum++).setCellValue(uplpDoc.getDistrict());
        row.createCell(cellNum++).setCellValue(uplpDoc.getBuildingAddress());
        row.createCell(cellNum++).setCellValue(uplpDoc.getCadastralNo());
        row.createCell(cellNum++).setCellValue(uplpDoc.getTlpProjectAvailability());
        row.createCell(cellNum++).setCellValue(uplpDoc.getTlpDocumentDetails());
        row.createCell(cellNum++).setCellValue(uplpDoc.getSurveyingProjectAvailability());
        row.createCell(cellNum++).setCellValue(uplpDoc.getSurveyingProjectDetails());
        row.createCell(cellNum++).setCellValue(uplpDoc.getSutGroupName());
        row.createCell(cellNum++).setCellValue(uplpDoc.getSutCodes());
        row.createCell(cellNum++).setCellValue(uplpDoc.getPlotArea() != null ? uplpDoc.getPlotArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesAvailability());
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesArea() != null ? uplpDoc.getSubzonesArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getBuildingMaxHeight() != null ? uplpDoc.getBuildingMaxHeight().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getBuildingMaxFloors() != null ? uplpDoc.getBuildingMaxFloors().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getBuiltUpAreaPercentage());
        row.createCell(cellNum++).setCellValue(uplpDoc.getBuildingDensity());
        row.createCell(cellNum++).setCellValue(uplpDoc.getCboPurpose());
        row.createCell(cellNum++).setCellValue(uplpDoc.getCboDescription());
        row.createCell(cellNum++).setCellValue(uplpDoc.getObjectsNotUnderConstructionStandarts());
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalFloorArea() != null ? uplpDoc.getTotalFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getResidentialDevelopmentFloorArea() != null ? uplpDoc.getResidentialDevelopmentFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getNonResidentialDevelopmentFloorArea() != null ? uplpDoc.getNonResidentialDevelopmentFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getResidentialPremisesFloorArea() != null ? uplpDoc.getResidentialPremisesFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getNonResidentialPremisesFloorArea() != null ? uplpDoc.getNonResidentialPremisesFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalBuildingArea() != null ? uplpDoc.getTotalBuildingArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalResidentialDevelopmentArea() != null ? uplpDoc.getTotalResidentialDevelopmentArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalNonResidentialDevelopmentArea() != null ? uplpDoc.getTotalNonResidentialDevelopmentArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalResidentialPremisesArea() != null ? uplpDoc.getTotalResidentialPremisesArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalNonResidentialPremisesArea() != null ? uplpDoc.getTotalNonResidentialPremisesArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getTotalUndergroundSpaceArea() != null ? uplpDoc.getTotalUndergroundSpaceArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalFloorArea() != null ? uplpDoc.getSubzonesTotalFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesResidentialObjectsFloorArea() != null ? uplpDoc.getSubzonesResidentialObjectsFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesNonResidentialObjectsFloorArea() != null ? uplpDoc.getSubzonesNonResidentialObjectsFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesResidentialPremisesFloorArea() != null ? uplpDoc.getSubzonesResidentialPremisesFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesNonResidentialPremisesFloorArea() != null ? uplpDoc.getSubzonesNonResidentialPremisesFloorArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalObjectsArea() != null ? uplpDoc.getSubzonesTotalObjectsArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalResidentialObjectsArea() != null ? uplpDoc.getSubzonesTotalResidentialObjectsArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalNonResidentialObjectsArea() != null ? uplpDoc.getSubzonesTotalNonResidentialObjectsArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalResidentialPremisesArea() != null ? uplpDoc.getSubzonesTotalResidentialPremisesArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalNonResidentialPremisesArea() != null ? uplpDoc.getSubzonesTotalNonResidentialPremisesArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getSubzonesTotalUndergroundSpaceArea() != null ? uplpDoc.getSubzonesTotalUndergroundSpaceArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboAvailability());
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboTotalCount());
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboPurpose());
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboDescription());
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboMaxFloorCount() != null ? uplpDoc.getExistingCboMaxFloorCount().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboTotalArea() != null ? uplpDoc.getExistingCboTotalArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboResidentialObjectsArea() != null ? uplpDoc.getExistingCboResidentialObjectsArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getExistingCboNonResidentialObjectsArea() != null ? uplpDoc.getExistingCboNonResidentialObjectsArea().toString() : "-");
        row.createCell(cellNum++).setCellValue(uplpDoc.getCloAvailability());
        row.createCell(cellNum++).setCellValue(uplpDoc.getCboTotalCount());
        row.createCell(cellNum++).setCellValue(uplpDoc.getCloDescription());
        row.createCell(cellNum++).setCellValue(uplpDoc.getCloIdentificationNo());
        row.createCell(cellNum).setCellValue(uplpDoc.getCloRegistrationNo());
    }
}
