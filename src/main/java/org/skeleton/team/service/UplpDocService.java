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
     * @param id идентификтор
     * @return документ ГПЗУ
     */
    UplpDoc getUplpDocById(Long id);

    List<UplpDoc> getUplpDocByIds(List<Long> ids);

    List<UplpSimpleDoc> getUplpSimpleDocByIds(List<Long> ids);

    UplpDoc deleteUplpDocById(Long id);

    List<UplpDoc> createUplpDocs(List<MultipartFile> files);

    UplpDoc updateUplpDoc(MultipartFile file, Long id);

    void setUplpDocsToXlsxResponse(List<Long> ids, OutputStream outputStream);

    /**
     * Добавление xml данных объектов в поток данных.
     * @param ids ИД документов ГПЗУ
     * @param outputStream поток данных
     */
    void setUplpDocsToXmlResponse(List<Long> ids, OutputStream outputStream);
}
