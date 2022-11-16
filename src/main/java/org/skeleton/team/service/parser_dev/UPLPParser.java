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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Парсер для документов ГПЗУ.
 */
@Component
@RequiredArgsConstructor
public class UPLPParser {

    private final UplpDocMapper uplpDocMapper;

    private static final String SECRET = "Секретно";

    /**
     * Производит последовательный парсинг документа согласно требуемому регламенту и возвращает сущность документа с заполненными полями.
     *
     * @param document - pdf-документ ГПЗУ для парсинга
     * @return список полученных документов (каждый документ - отдельная подзона)
     * @throws IOException при ошибке работы с файлом
     */
    public List<UplpDoc> parsingUPLPFile(File document, StringBuilder parseErrorsLog) throws IOException {
        List<UplpDoc> resultDocList = new ArrayList<>();

        UplpDoc uplpDoc = new UplpDoc();

        String stringDocumentText = pdfToString(document);

        resultDocList.add(uplpDoc);

        //Если документ секретный, то вносим пометки и заканчиваем парсинг
        if(isSecret(stringDocumentText)) {
            uplpDoc.setUplpNo(document.getName().replace("RF","РФ"));

            uplpDoc.setUplpStatus(SECRET);

            return resultDocList;
        }

        //Этап 1 - Парсинг атрибутов реквизитов
        parseUplpRequisites(uplpDoc, stringDocumentText);

        //Этап 2 - Парсинг атрибутов территории
        parseUplpTerritoryAttributes(uplpDoc, stringDocumentText);

        //Этап 3 - Парсинг атрибутов видов разрешённого использования ЗУ
        parseUplpSutCodes(uplpDoc, stringDocumentText);

        //Этап 4 - Парсинг атрибутов территориальных показателей ГПЗУ
        parseUplpTerritory(uplpDoc, stringDocumentText);

        //Этап 5 - Парсинг атрибутов ОКС
        parseUplpCboAttributes(uplpDoc, stringDocumentText);

        //Этап 6 - Парсинг объектов, включенных в единый государственный реестр объектов культурного наследия (ОКН)
        parseUplpCloAttributes(uplpDoc, stringDocumentText);

        //Этап 7 - Парсинг подзон
        parseUplpSubzones(resultDocList, uplpDoc, stringDocumentText, uplpDocMapper);

        checkForErrors(uplpDoc, parseErrorsLog);

        for (String log : parseErrorsLog.toString().split("\n")){
            System.out.println(log);
        }

        return resultDocList;
    }

    /**
     * Открывает файл и обрабатывает структуру документа с помощью внешней библиотеки парсинга.
     * Возвращает полученный текст в формате одной переменной
     * @param document pdf-документ ГПЗУ для парсинга
     * @return текст документа в формате строки с переносами
     * @throws IOException
     */
    private static String pdfToString(File document) throws IOException {
        String parsedText;

        PDFParser parser = new PDFParser(new RandomAccessFile(document, "r"));

        parser.parse();

        COSDocument cosDoc = parser.getDocument();

        PDFTextStripper pdfStripper = new PDFTextStripper();

        PDDocument pdDoc = new PDDocument(cosDoc);

        parsedText = pdfStripper.getText(pdDoc);

        cosDoc.close();

        pdDoc.close();

        return parsedText;
    }

    /**
     * Проверка документа на секретность
     * @param documentText весь текст документа в виде строки
     * @return результат проверки
     */
    private boolean isSecret(String documentText){
        String[] docByLines = documentText.split("\n|\r\n");
        for (String line : docByLines){
            if (line.startsWith("документ находится в Первом отделе")){
                return true;
            }
        }
        return false;
    }

    /**
     * Производит парсинг группировки реквизитов документа:<br>
     * - Уникальный номер записи <br>
     * - Номер документа ГПЗУ<br>
     * - Дата выдачи ГПЗУ<br>
     * - Статус ГПЗУ <br>
     * - Срок действия ГПЗУ<br>
     * - Правообладатель ГПЗУ<br>
     * - Тип правообладателя или получателя ГПЗУ
     * @param documentText весь текст документа в виде строки
     */
    private void parseUplpRequisites(UplpDoc uplpDoc, String documentText){

        RequsitionParseProcessor parseProcessor = new RequsitionParseProcessor();

        String[] docByLines = documentText.split("\n|\r\n");

        for(String line : docByLines){

            //Уникальный номер записи и номер документа ГПЗУ
            if(line.startsWith("№ RU")||line.startsWith("№ РФ")){
                uplpDoc.setUplpNo(parseProcessor.parseNumber(line));

                uplpDoc.setRecordNo(uplpDoc.getUplpNo());

                continue;
            }

            //Дата выдачи ГПЗУ
            if(line.matches("Дата выдачи (\\d{2}.\\d{2}.\\d{4})|(\\d{2}.\\d{2}.\\d{4}\r)")){
                uplpDoc.setDateOfIssue(parseProcessor.parseIssueDate(line));

                LocalDate issueEndDate = parseProcessor.parseExpiryDate(new SimpleDateFormat("dd.MM.yyyy")
                                                                .format(uplpDoc.getDateOfIssue()));

                uplpDoc.setExpiryDate(Date.from(issueEndDate.atStartOfDay()
                                                .atZone(ZoneId.systemDefault())
                                                .toInstant()));

                uplpDoc.setUplpStatus(parseProcessor.checkActual(issueEndDate));

                continue;
            }

            //TODO достать библиотеку склонений
            //Правообладатель ГПЗУ
            uplpDoc.setUplpRecipient(parseProcessor.parseRecipient(docByLines));

            //Тип правообладателя или получателя ГПЗУ
            uplpDoc.setRecipientType(parseProcessor.checkRecipientType(uplpDoc.getUplpRecipient()));
        }

        parseProcessor.outputUplpRequisitionData(uplpDoc);
    }

    /**
     * Парсинг аттрибутов территории участка:<br>
     * - TODO Административный округ <br>
     * - TODO Район (поселение) <br>
     * - Строительный адрес <br>
     * - Кадастровый номер земельного участка (ЗУ) или условный номер <br>
     * - Наличие проекта планировки территории (ППТ) в границах ГПЗУ реквизиты документа <br>
     * - TODO Реквизиты документа ППТ <br>
     * - TODO Наличие отдельного проекта межевания территории в границах ГПЗУ <br>
     * - TODO Реквизиты проекта межевания территории <br>
     * @param uplpDoc документ ГПЗУ
     * @param documentText весь текст документа в виде строки
     */
    private void parseUplpTerritoryAttributes(UplpDoc uplpDoc, String documentText){

        TerritoryParseProcessor parseProcessor = new TerritoryParseProcessor();

        String[] docByLines = documentText.split("\n|\r\n");

        //TODO Парсинг админ. округ

        //TODO Парсинг района (поселения)

        uplpDoc.setBuildingAddress(parseProcessor.parseBuildingAddress(docByLines));

        uplpDoc.setCadastralNo(parseProcessor.parseCadastralNumber(docByLines));

        uplpDoc.setTlpProjectAvailability(parseProcessor.parseAvailability(docByLines));

        //TODO парсинг Реквизиты документа ППТ

        //TODO парсинг наличия отдельного проекта межевания

        //TODO парсинг Реквизиты межевания

        parseProcessor.outputUplpTerritoryData(uplpDoc);
    }

    /**
     * Парсинг атрибутов видов разрешённого пользования ЗУ:
     * - Наименование условной группы использования ЗУ по ВРИ <br>
     * - Коды основных видов разрешенного использования (ВРИ) земельного  участка (ЗУ) <br>
     * @param uplpDoc документ ГПЗУ
     * @param documentText весь текст документа в виде строки
     */
    private void parseUplpSutCodes(UplpDoc uplpDoc, String documentText){
        SutCodesParseProcessor parseProcessor = new SutCodesParseProcessor();

        String codesVRI = parseProcessor.parseSutCodes(documentText);

        uplpDoc.setSutCodes(codesVRI);

        uplpDoc.setSutGroupName(parseProcessor.checkLivingPlace(codesVRI));

        parseProcessor.outputUplpSutData(uplpDoc);
    }

    private void parseUplpTerritory(UplpDoc uplpDoc, String documentText){

        TerritoryParseProcessor parseProcessor = new TerritoryParseProcessor();

        String[] docByLines = documentText.split("\n|\r\n");

        uplpDoc.setPlotArea(parseProcessor.parsePlotArea(docByLines));
    }


    private void parseUplpSubzones(List<UplpDoc> docList,UplpDoc uplpDoc, String documentText, UplpDocMapper uplpDocMapper){
        SubzoneParseProcessor parseProcessor = new SubzoneParseProcessor();

        parseProcessor.parseSubzones(docList, uplpDoc, documentText, uplpDocMapper);
    }

    /**
     * Парсинг атрибутов ОКС на ЗУ <br>
     *  - Всех объектов <br>
     *  - Жилых объектов <br>
     * - Нежилых объектов <br>
     * - Жилых помещений <br>
     * - Встроенно-пристроенных, отдельно стоящих нежилых помещений <br>
     * @param uplpDoc документ ГПЗУ
     * @param documentText весь текст документа в виде строки
     */
    private void parseUplpCboAttributes(UplpDoc uplpDoc, String documentText) {

        CBOParseProcessor parseProcessor = new CBOParseProcessor();

        String[] docByLines = documentText.split("\n|\r\n");

        uplpDoc.setExistingCboAvailability(parseProcessor.getExistingCboAvailability(docByLines));
        uplpDoc.setExistingCboTotalCount(parseProcessor.getExistingCboTotalCount(docByLines));
        uplpDoc.setExistingCboPurpose(parseProcessor.getExistingCboPurpose(docByLines));
        uplpDoc.setExistingCboDescription(parseProcessor.getExistingCboDescription(docByLines));
        uplpDoc.setExistingCboMaxFloorCount(parseProcessor.getExistingCboMaxFloorCount(docByLines));
        uplpDoc.setExistingCboTotalArea(parseProcessor.getExistingCboTotalArea(docByLines));
        uplpDoc.setExistingCboResidentialObjectsArea(parseProcessor.getExistingCboResidentialObjectsArea(docByLines));
        uplpDoc.setExistingCboNonResidentialObjectsArea(parseProcessor.getExistingCboNonResidentialObjectsArea(docByLines));

    }

    /**
     * Парсинг атрибутов объектов, включённых в единый гос.реестр ОКН.
     * @param uplpDoc документ ГПЗУ
     * @param documentText весь текст документа в виде строки
     */
    private void parseUplpCloAttributes(UplpDoc uplpDoc, String documentText){
        CLOParseProcessor cloParseProcessor = new CLOParseProcessor();
        cloParseProcessor.parseCloAttributes(uplpDoc, documentText);
    }



    /**
     * Формирования лога ошибок парсинга
     * @param uplpDoc документ ГПЗУ
     * @param parseErrorsLog логгер ошибок
     */
    private void checkForErrors(UplpDoc uplpDoc, StringBuilder parseErrorsLog){
        if (uplpDoc.getUplpNo().equals("-")) {
            parseErrorsLog.append("Не найден номер ГПЗУ.\n");
        }
        if (uplpDoc.getDateOfIssue() == null) {
            parseErrorsLog.append("Не найдена Дата выдачи ГПЗУ.\n");
        }
        if (uplpDoc.getExpiryDate() == null) {
            parseErrorsLog.append("Не найден Срок действия ГПЗУ.\n");
        }
        if (uplpDoc.getUplpStatus().equals("-")) {
            parseErrorsLog.append("Не найден Статус ГПЗУ.\n");
        }
        if (uplpDoc.getUplpRecipient().equals("-")) {
            parseErrorsLog.append("Не найден Правообладатель или иной получатель ГПЗУ.\n");
        }
        if (uplpDoc.getRecipientType().equals("-")) {
            parseErrorsLog.append("Не найден Тип правообладателя или получателя ГПЗУ.\n");
        }
        if (uplpDoc.getBuildingAddress().equals("-")) {
            parseErrorsLog.append("Не найден Строительный адрес.\n");
        }
        if (uplpDoc.getTlpProjectAvailability().equals("-")) {
            parseErrorsLog.append("Не найдено Наличие проекта планоровки территории (ППТ) в границах ГПЗУ.\n");
        }
        if (uplpDoc.getSutCodes().equals("-")) {
            parseErrorsLog.append("Не найдены Коды основных видов разрешенного использования (ВРИ) зумельного участка (ЗУ).\n");
        }
        if (uplpDoc.getPlotArea() == null) {
            parseErrorsLog.append("Не найдена Площадь земельного участка (ЗУ), кв.м.\n");
        }
        if(uplpDoc.getSubzonesAvailability().equals("-")){
            parseErrorsLog.append("Не найдена Наличие подзон ЗУ, номера\n");
        }
        if (uplpDoc.getBuildingDensity().equals("-")){
            parseErrorsLog.append("Не найдено Максимальная плотность застройки, тыс. кв.м/га\n");
        }
        if(uplpDoc.getBuiltUpAreaPercentage().equals("-")){
            parseErrorsLog.append("Не найден Максимальный процент застроенности, %\n");
        }
        if(uplpDoc.getBuildingMaxHeight() == null){
            parseErrorsLog.append("Не найдена Максимальная высота застройки, м.\n");
        }
    }
}