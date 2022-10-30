package org.skeleton.team.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.service.UplpDocService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Документы ГПЗУ", description = "Взаимодействие с документами ГПЗУ")
@RestController
@RequestMapping("uplp")
@RequiredArgsConstructor
public class UplpDocController {

    private final UplpDocService uplpDocService;

    //TODO Результаты автоматической обработки должны включать в себя лог-файл с
    //TODO перечнем успешно распознанных электронных образов ГПЗУ и произошедших
    //TODO ошибок с описанием ошибок, а также оцифрованные данные в формате xls, csv и json
    //TODO или xml
    //TODO Для одного и нескольких файлов (для файлов, загруженных в последний раз пользователем):
    //TODO getUplpDocByIdLikeXml, getUplpDocByIdLikeXls, getUplpDocByIdLikeCsv, getUplpDocByIdLikeJson

    @GetMapping("/{id}")
    @Operation(summary = "Получение документа ГПЗУ по ИД")
    public ResponseEntity<UplpDoc> getUplpDocById(
            @Parameter(description = "Идентификатор документа ГПЗУ", required = true) @PathVariable final Integer id
    ) {
        UplpDoc doc = uplpDocService.getUplpDocById(id);
        if (doc != null) {
            return ResponseEntity.ok(doc);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Создание документа(ов) ГПЗУ из ПДФ файла")
    public ResponseEntity<Map<String, UplpDoc>> createUplpDoc(
            @Parameter(description = "Загружаемые файлы", required = true) @RequestParam("file") List<MultipartFile> files,
            @Parameter(description = "Тип документа") @RequestParam(name = "docType") String docType
    ) {
        if (files.size() > 1000) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }
        Map<String, UplpDoc> doc = uplpDocService.createUplpDocs(files, docType);
        if (doc != null) {
            return ResponseEntity.ok(doc);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление документа ГПЗУ из ПДФ файла")
    public ResponseEntity<UplpDoc> updateUplpDoc(
            @Parameter(description = "Загружаемый файл", required = true) @RequestParam("file") MultipartFile file,
            @Parameter(description = "Идентификатор документа ГПЗУ", required = true) @PathVariable final Integer id
    ) {
        UplpDoc doc = uplpDocService.updateUplpDoc(file, id);
        if (doc != null) {
            return ResponseEntity.ok(doc);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление документа ГПЗУ по ИД")
    public ResponseEntity<UplpDoc> deleteUplpDocById(
            @Parameter(description = "Идентификатор документа ГПЗУ для удаления", required = true)
            @PathVariable("id") final Integer id
    ) {
        UplpDoc doc = uplpDocService.deleteUplpDocById(id);
        if (doc != null) {
            return ResponseEntity.ok(doc);
        }
        return ResponseEntity.notFound().build();
    }
}
