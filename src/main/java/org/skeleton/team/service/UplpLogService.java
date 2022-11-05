package org.skeleton.team.service;

import org.skeleton.team.entity.UplpLog;

import java.util.List;

//Интерфейс для работы с логами обработки документов ГПЗУ
public interface UplpLogService {

    UplpLog getUplpLogByDocId(Long uplpDocId);

    List<UplpLog> getUplpLogsByDocIds(List<Long> uplpDocIds);
}
