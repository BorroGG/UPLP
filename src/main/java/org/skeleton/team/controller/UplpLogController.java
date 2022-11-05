package org.skeleton.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skeleton.team.entity.UplpLog;
import org.skeleton.team.service.UplpLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Класс контроллера для работы с логами обработки файлов.
 */
@Tag(name = "Логи по документам ГПЗУ", description = "Взаимодействие с логами документов ГПЗУ")
@RestController
@RequestMapping("log")
@RequiredArgsConstructor
public class UplpLogController {

    private final UplpLogService uplpLogService;

    /**
     * Операция получения логов обработки документа ГПЗУ по ИД.
     * @param uplpId Идентификаторы документов ГПЗУ
     * @return лог обработки документа
     */
    @GetMapping("/{uplpId}")
    @Operation(summary = "Получение логов обработки документа ГПЗУ по ИД документа")
    public ResponseEntity<UplpLog> getUplpLogByDocId(
            @Parameter(description = "Идентификатор документа ГПЗУ", required = true)
            @PathVariable final Long uplpId
    ) {
        UplpLog log = uplpLogService.getUplpLogByDocId(uplpId);
        if (log != null) {
            return ResponseEntity.ok(log);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Операция получения логов обработки нескольких документов ГПЗУ по ИД.
     * @param uplpIds Идентификаторы документов ГПЗУ
     * @return логи обработки документов
     */
    @GetMapping("/")
    @Operation(summary = "Получение логов обработки документов ГПЗУ по ИД документов")
    public ResponseEntity<List<UplpLog>> getUplpLogsByDocIds(
            @Parameter(description = "Идентификаторы документов ГПЗУ")
            @RequestParam final List<Long> uplpIds
    ) {
        List<UplpLog> logs = uplpLogService.getUplpLogsByDocIds(uplpIds);
        if (logs != null && !logs.isEmpty()) {
            return ResponseEntity.ok(logs);
        }
        return ResponseEntity.notFound().build();
    }
}
