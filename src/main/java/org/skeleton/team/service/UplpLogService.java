package org.skeleton.team.service;

import org.skeleton.team.entity.UplpLog;

import java.util.List;

/**
 * Интерфейс для работы с логами обработки документов ГПЗУ
 */
public interface UplpLogService {

    /**
     * Получение лога обработки документа ГПЗУ по ИД.
     * @param uplpDocId идентификатор
     * @return лог обработки
     */
    UplpLog getUplpLogByDocId(Long uplpDocId);

    /**
     * Получение логов обработки нескольких документов ГПЗУ по ИД.
     * @param uplpDocIds идентификаторы
     * @return логи обработки
     */
    List<UplpLog> getUplpLogsByDocIds(List<Long> uplpDocIds);
}
