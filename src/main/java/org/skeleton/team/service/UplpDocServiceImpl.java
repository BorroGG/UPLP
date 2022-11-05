package org.skeleton.team.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpLog;
import org.skeleton.team.entity.UplpSimpleDoc;
import org.skeleton.team.mapper.UplpDocMapper;
import org.skeleton.team.repository.UplpDocRepository;
import org.skeleton.team.repository.UplpLogRepository;
import org.skeleton.team.util.UplpDocConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * Класс для работы с документами ГПЗУ.
 */
@Service
@RequiredArgsConstructor
public class UplpDocServiceImpl implements UplpDocService {

    private final UplpDocRepository uplpDocRepository;
    private final UplpLogRepository uplpLogRepository;
    private final HttpServletRequest request;
    private final UplpDocConverter uplpDocConverter;
    private final UplpDocMapper uplpDocMapper;

    /**
     * Получение документа ГПЗУ по ИД.
     * @param id идентификатор документа ГПЗУ
     * @return документ ГПЗУ
     */
    @Override
    @Transactional(readOnly = true)
    public UplpDoc getUplpDocById(Long id) {
        return uplpDocRepository.findById(id).orElse(null);
    }

    /**
     * Получение нескольких документов ГПЗУ по ИД.
     * @param ids идентификаторы документов ГПЗУ
     * @return документы ГПЗУ
     */
    @Override
    @Transactional(readOnly = true)
    public List<UplpDoc> getUplpDocByIds(List<Long> ids) {
        return uplpDocRepository.findAllById(ids);
    }

    /**
     * Получение простых документов ГПЗУ по ИД.
     * @param ids идентификаторы документов ГПЗУ
     * @return документы ГПЗУ в формате без системных полей
     */
    @Override
    @Transactional(readOnly = true)
    public List<UplpSimpleDoc> getUplpSimpleDocByIds(List<Long> ids) {
        return uplpDocMapper.toSimpleDoc(getUplpDocByIds(ids));
    }

    /**
     * Удаление документа ГПЗУ по ИД.
     * @param id идентификатор документа ГПЗУ
     * @return документ ГПЗУ
     */
    @Override
    @Transactional
    public UplpDoc deleteUplpDocById(Long id) {
        UplpDoc uplpDoc = uplpDocRepository.findById(id).orElse(null);
        if (uplpDoc != null) {
            String filePath = uplpDoc.getFileReference();
            File file = new File(filePath);
            file.delete();
            uplpDocRepository.delete(uplpDoc);
            return uplpDoc;
        }
        return null;
    }

    /**
     * Получение нескольких документов ГПЗУ из PDF-файлов.
     * @param multipartFiles файлы в формате PDF
     * @return документы ГПЗУ
     */
    @Override
    @Transactional
    public List<UplpDoc> createUplpDocs(List<MultipartFile> multipartFiles) {
        List<UplpDoc> result = new ArrayList<>();
        String realPathtoUploads = request.getServletContext().getRealPath("/files/");
        if(! new File(realPathtoUploads).exists()) {
            new File(realPathtoUploads).mkdir();
        }

        for (int i = 0; i < multipartFiles.size(); i++) {
            try {
                String orgName = multipartFiles.get(i).getOriginalFilename();
                Date loadTime = new Timestamp(System.currentTimeMillis());
                String filePath = realPathtoUploads + loadTime.getTime() + orgName;
                File file = new File(filePath);
                multipartFiles.get(i).transferTo(file);
                String parsedText;
                PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
                parser.parse();
                COSDocument cosDoc = parser.getDocument();
                PDFTextStripper pdfStripper = new PDFTextStripper();
                PDDocument pdDoc = new PDDocument(cosDoc);
                parsedText = pdfStripper.getText(pdDoc);
                String[] words = parsedText.split(" ");
                UplpDoc uplpDoc = new UplpDoc();
                StringBuilder parseErrors = new StringBuilder();
                for (String word : words) {
                    //TODO парсим
                }
                UplpLog uplpLog = new UplpLog();
                uplpLog.setFileName(orgName);
                uplpLog.setLoadDttm(loadTime);
                uplpLog.setProcessDttm(new Timestamp(System.currentTimeMillis()));
                uplpDoc.setFileReference(file.getAbsolutePath());
                if (parseErrors.length() == 0) {
                    uplpLog.setProcessResult("Успешно");
                } else {
                    uplpLog.setProcessResult("Неуспешно");
                    uplpLog.setErrors(parseErrors.toString());
                }
                uplpLog = uplpLogRepository.save(uplpLog);
                uplpDoc.setUplpLog(uplpLog);
                result.add(uplpDocRepository.save(uplpDoc));
            } catch (Exception e) {
                throw new RuntimeException("Критическая ошибка обработки файлов: " + e.getMessage());
            }
        }
        return result;
    }

    /**
     * Удаление старого и загрузка нового документа ГПЗУ из PDF-файла.
     * @param multipartFile новый документ в формате PDF
     * @param id идентификатор документа ГПЗУ
     * @return документ ГПЗУ
     */
    @Override
    @Transactional
    public UplpDoc updateUplpDoc(MultipartFile multipartFile, Long id) {
        //TODO поправить после изменений в создании документа
        String realPathtoUploads = request.getServletContext().getRealPath("/files/");
        if(! new File(realPathtoUploads).exists()) {
            new File(realPathtoUploads).mkdir();
        }
        try {
            deleteUplpDocById(id);
            String orgName = multipartFile.getOriginalFilename();
            Date loadTime = new Timestamp(System.currentTimeMillis());
            String filePath = realPathtoUploads + loadTime.getTime() + orgName;
            File file = new File(filePath);
            multipartFile.transferTo(file);
            String parsedText;
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            COSDocument cosDoc = parser.getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            PDDocument pdDoc = new PDDocument(cosDoc);
            parsedText = pdfStripper.getText(pdDoc);
            String[] words = parsedText.split(" ");
            UplpDoc uplpDoc = new UplpDoc();
            StringBuilder parseErrors = new StringBuilder();
            for (String word : words) {
                //TODO парсим
            }
            UplpLog uplpLog = new UplpLog();
            uplpLog.setFileName(orgName);
            uplpLog.setLoadDttm(loadTime);
            uplpLog.setProcessDttm(new Timestamp(System.currentTimeMillis()));
            uplpDoc.setFileReference(file.getAbsolutePath());
            if (parseErrors.length() == 0) {
                uplpLog.setProcessResult("Успешно");
            } else {
                uplpLog.setProcessResult("Неуспешно");
                uplpLog.setErrors(parseErrors.toString());
            }
            uplpLog = uplpLogRepository.save(uplpLog);
            uplpDoc.setUplpLog(uplpLog);
            return uplpDocRepository.save(uplpDoc);
        } catch (Exception e) {
            throw new RuntimeException("Критическая ошибка обработки файлов: " + e.getMessage());
        }
    }

    /**
     * Добавление xlsx данных объектов в поток данных ответа.
     * @param ids ИД документов ГПЗУ
     * @param outputStream поток данных для вывода ответа
     */
    @Override
    public void setUplpDocsToXlsxResponse(List<Long> ids, OutputStream outputStream) {
        List<UplpDoc> docs = getUplpDocByIds(ids);
        List<UplpSimpleDoc> simpleDocs = uplpDocMapper.toSimpleDoc(docs);
        uplpDocConverter.convertUplpDocsToXlsxStream(simpleDocs, outputStream);
    }

    /**
     * Добавление xml данных объектов в поток данных ответа.
     * @param ids ИД документов ГПЗУ
     * @param outputStream поток данных ответа
     */
    @Override
    public void setUplpDocsToXmlResponse(List<Long> ids, OutputStream outputStream) {
        List<UplpDoc> docs = getUplpDocByIds(ids);
        List<UplpSimpleDoc> simpleDocs = uplpDocMapper.toSimpleDoc(docs);
        uplpDocConverter.convertUplpDocsToXmlStream(simpleDocs, outputStream);
    }
}
