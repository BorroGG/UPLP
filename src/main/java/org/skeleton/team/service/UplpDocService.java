package org.skeleton.team.service;

import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpSimpleDoc;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * Интерфейс для работы с документами ГПЗУ.
 */
public interface UplpDocService {

    /**
     * Получение документа ГПЗУ по ИД.
     * @param id идентификатор
     * @return документ ГПЗУ
     */
    UplpDoc getUplpDocById(Long id);

    /**
     * Получение нескольких документов ГПЗУ по ИД.
     * @param ids идентификаторы
     * @return документы ГПЗУ
     */
    List<UplpDoc> getUplpDocByIds(List<Long> ids);

    /**
     * Получение простых документов ГПЗУ по ИД.
     * @param ids идентификаторы
     * @return документы ГПЗУ в формате для отображения у пользователя
     */
    List<UplpSimpleDoc> getUplpSimpleDocByIds(List<Long> ids);

    /**
     * Удаление документа ГПЗУ по ИД.
     * @param id идентификатор
     * @return документ ГПЗУ
     */
    UplpDoc deleteUplpDocById(Long id);

    /**
     * Получение нескольких документов ГПЗУ из PDF-файлов.
     * @param files файлы в формате PDF
     * @return документы ГПЗУ
     */
    List<UplpDoc> createUplpDocs(List<MultipartFile> files);

    /**
     * Удаление старого и загрузка нового документа ГПЗУ из PDF-файла
     * @param multipartFile новый документ в формате PDF
     * @param id идентификатор
     * @return документ ГПЗУ
     */
    UplpDoc updateUplpDoc(MultipartFile multipartFile, Long id);

    /**
     * Добавление xlsx данных объектов в поток данных.
     * @param ids ИД документов ГПЗУ
     * @param outputStream поток данных
     */
    void setUplpDocsToXlsxResponse(List<Long> ids, OutputStream outputStream);

    /**
     * Добавление xml данных объектов в поток данных.
     * @param ids ИД документов ГПЗУ
     * @param outputStream поток данных
     */
    void setUplpDocsToXmlResponse(List<Long> ids, OutputStream outputStream);
}
