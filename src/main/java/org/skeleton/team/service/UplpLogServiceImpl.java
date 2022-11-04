package org.skeleton.team.service;

import lombok.RequiredArgsConstructor;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UplpLogServiceImpl implements UplpLogService {

    private final UplpDocService uplpDocService;

    @Override
    @Transactional(readOnly = true)
    public UplpLog getUplpLogByDocId(Long uplpDocId) {
        UplpDoc doc = uplpDocService.getUplpDocById(uplpDocId);
        return doc != null ? doc.getUplpLog() : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UplpLog> getUplpLogsByDocIds(List<Long> uplpDocIds) {
        List<UplpDoc> docs = uplpDocService.getUplpDocByIds(uplpDocIds);
        if (!docs.isEmpty()) {
            return docs.stream()
                    .map(UplpDoc::getUplpLog)
                    .collect(Collectors.toList());
        }
        return null;
    }


}
