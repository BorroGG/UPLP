package org.skeleton.team.service;

import lombok.RequiredArgsConstructor;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpLog;
import org.skeleton.team.entity.UplpSimpleDoc;
import org.skeleton.team.mapper.UplpDocMapper;
import org.skeleton.team.repository.UplpDocRepository;
import org.skeleton.team.repository.UplpLogRepository;
import org.skeleton.team.service.parser_dev.UPLPParser;
import org.skeleton.team.util.UplpDocConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    private final UPLPParser uplpParser;

    /**
     * Получение документа ГПЗУ по ИД.
     *
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
     *
     * @param ids идентификаторы документов ГПЗУ
     * @return документы ГПЗУ
     */
    @Override
    @Transactional(readOnly = true)
    public List<UplpDoc> getUplpDocByIds(List<Long> ids) {
        return uplpDocRepository.findAllById(ids);
    }

    /**
     * Получение всех документов ГПЗУ.
     *
     * @return документы ГПЗУ
     */
    @Override
    @Transactional(readOnly = true)
    public List<UplpDoc> getAllUplpDocs() {
        return uplpDocRepository.findAll();
    }

    /**
     * Получение простых документов ГПЗУ по ИД.
     *
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
     *
     * @param id идентификатор документа ГПЗУ
     * @return документ ГПЗУ
     */
    @Override
    @Transactional
    public UplpDoc deleteUplpDocById(Long id) {
        UplpDoc uplpDoc = uplpDocRepository.findById(id).orElse(null);
        if (uplpDoc != null) {
            String filePath = uplpDoc.getFileReference();
            if (!new File(filePath).delete()) {
                throw new RuntimeException("Не удалось удалить файл из файловой системы");
            }
            uplpDocRepository.delete(uplpDoc);
            return uplpDoc;
        }
        return null;
    }

    /**
     * Получение нескольких документов ГПЗУ из PDF-файлов.
     *
     * @param multipartFiles файлы в формате PDF
     * @return документы ГПЗУ
     */
    @Override
    @Transactional
    public List<UplpDoc> createUplpDocs(List<MultipartFile> multipartFiles) {
        List<UplpDoc> result = new ArrayList<>();
        String realPathtoUploads = request.getServletContext().getRealPath("/files/");
        if (!new File(realPathtoUploads).exists()) {
            if (!new File(realPathtoUploads).mkdir()) {
                throw new RuntimeException("Не удалось создать директорию для хранения фалйов");
            }
        }

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String orgName = multipartFile.getOriginalFilename();
                Date loadTime = new Timestamp(System.currentTimeMillis());
                String filePath = realPathtoUploads + loadTime.getTime() + orgName;
                File file = new File(filePath);
                multipartFile.transferTo(file);

                List<UplpDoc> docs = uplpDocRepository.saveAll(parseFile(orgName, loadTime, file));
                Long docId = docs.get(0).getUplpDocId();
                docs.forEach(uplpDoc -> uplpDoc.setObjectZoneNo(docId + ":" + uplpDoc.getUplpDocId()));
                result.addAll(uplpDocRepository.saveAll(docs));
            } catch (Exception e) {
                throw new RuntimeException("Критическая ошибка обработки файлов: " + e.getMessage());
            }
        }
        return result;
    }

    /**
     * Удаление старого и загрузка нового документа ГПЗУ из PDF-файла.
     *
     * @param multipartFile новый документ в формате PDF
     * @param id            идентификатор документа ГПЗУ
     * @return документ ГПЗУ
     */
    @Override
    @Transactional
    public List<UplpDoc> updateUplpDoc(MultipartFile multipartFile, Long id) {
        String realPathtoUploads = request.getServletContext().getRealPath("/files/");
        if (!new File(realPathtoUploads).exists()) {
            if (!new File(realPathtoUploads).mkdir()) {
                throw new RuntimeException("Не удалось создать директорию для хранения фалйов");
            }
        }
        try {
            deleteUplpDocById(id);
            String orgName = multipartFile.getOriginalFilename();
            Date loadTime = new Timestamp(System.currentTimeMillis());
            String filePath = realPathtoUploads + loadTime.getTime() + orgName;
            File file = new File(filePath);
            multipartFile.transferTo(file);

            List<UplpDoc> docs = uplpDocRepository.saveAll(parseFile(orgName, loadTime, file));
            Long docId = docs.get(0).getUplpDocId();
            docs.forEach(uplpDoc -> uplpDoc.setObjectZoneNo(docId + ":" + uplpDoc.getUplpDocId()));

            return uplpDocRepository.saveAll(docs);
        } catch (Exception e) {
            throw new RuntimeException("Критическая ошибка обработки файлов: " + e.getMessage());
        }
    }

    /**
     * Добавление xlsx данных объектов в поток данных ответа.
     *
     * @param ids          ИД документов ГПЗУ
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
     *
     * @param ids          ИД документов ГПЗУ
     * @param outputStream поток данных ответа
     */
    @Override
    public void setUplpDocsToXmlResponse(List<Long> ids, OutputStream outputStream) {
        List<UplpDoc> docs = getUplpDocByIds(ids);
        List<UplpSimpleDoc> simpleDocs = uplpDocMapper.toSimpleDoc(docs);
        uplpDocConverter.convertUplpDocsToXmlStream(simpleDocs, outputStream);
    }

    private List<UplpDoc> parseFile(String fileName, Date loadTime, File file) throws IOException {
        StringBuilder parseErrors = new StringBuilder();

        List<UplpDoc> uplpDocs = uplpParser.parsingUPLPFile(file, parseErrors);

        for (UplpDoc uplpDoc : uplpDocs) {
            UplpLog uplpLog = new UplpLog();
            uplpLog.setFileName(fileName);
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
        }
        return uplpDocs;
    }
}
