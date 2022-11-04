package org.skeleton.team.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpSimpleDoc;
import org.skeleton.team.service.UplpDocService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "Документы ГПЗУ", description = "Взаимодействие с документами ГПЗУ")
@RestController
@RequestMapping("uplp")
@RequiredArgsConstructor
public class UplpDocController {

    private final UplpDocService uplpDocService;

    @GetMapping("/{id}")
    @Operation(summary = "Получение документа ГПЗУ по ИД")
    public ResponseEntity<UplpDoc> getUplpDocById(
            @Parameter(description = "Идентификатор документа ГПЗУ", required = true)
            @PathVariable final Long id
    ) {
        UplpDoc doc = uplpDocService.getUplpDocById(id);
        if (doc != null) {
            return ResponseEntity.ok(doc);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    @Operation(summary = "Получение документов ГПЗУ по ИД")
    public ResponseEntity<List<UplpDoc>> getUplpDocsByIds(
            @Parameter(description = "Идентификаторы документов ГПЗУ", required = true)
            @RequestParam final List<Long> ids
    ) {
        List<UplpDoc> docs = uplpDocService.getUplpDocByIds(ids);
        if (docs != null && !docs.isEmpty()) {
            return ResponseEntity.ok(docs);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/xlsx")
    @Operation(summary = "Получение документов ГПЗУ по ИД в формате xlsx")
    public void getXlsxUplpDocsByIds(
            @Parameter(description = "Идентификаторы документов ГПЗУ", required = true)
            @RequestParam final List<Long> ids,
            HttpServletResponse response
    ) {
        response.setContentType("application/xlsx");
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("Документы_ГПЗУ.xlsx", StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        try (OutputStream outputStream = response.getOutputStream()) {
            uplpDocService.setUplpDocsToXlsxResponse(ids, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Не получилось сконвертировать документы, ошибка: %s", e.getMessage()));
        }
    }

    @GetMapping("/xml")
    @Operation(summary = "Получение документов ГПЗУ по ИД в формате xml")
    public void getXmlUplpDocsByIds(
            @Parameter(description = "Идентификаторы документов ГПЗУ", required = true)
            @RequestParam final List<Long> ids,
            HttpServletResponse response
    ) {
        response.setContentType("application/xml");
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("Документы_ГПЗУ.xml", StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        try (OutputStream outputStream = response.getOutputStream()) {
            uplpDocService.setUplpDocsToXmlResponse(ids, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Не получилось сконвертировать документы, ошибка: %s", e.getMessage()));
        }
    }

    @GetMapping("/csv")
    @Operation(summary = "Получение документов ГПЗУ по ИД в формате csv")
    public void getCsvUplpDocsByIds(
            @Parameter(description = "Идентификаторы документов ГПЗУ", required = true)
            @RequestParam final List<Long> ids,
            HttpServletResponse response
    ) {
        response.setContentType("application/csv");
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("Документы_ГПЗУ.csv", StandardCharsets.UTF_8)
                .build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        try (OutputStream outputStream = response.getOutputStream()) {
            uplpDocService.setUplpDocsToXlsxResponse(ids, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Не получилось сконвертировать документы, ошибка: %s", e.getMessage()));
        }
    }

    @GetMapping("/json")
    @Operation(summary = "Получение документов ГПЗУ по ИД в формате json")
    public ResponseEntity<byte[]> getJsonUplpDocsByIds(
            @Parameter(description = "Идентификаторы документов ГПЗУ", required = true)
            @RequestParam final List<Long> ids
    ) {
        List<UplpSimpleDoc> docs = uplpDocService.getUplpSimpleDocByIds(ids);
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition
                                    .attachment()
                                    .filename("Документы_ГПЗУ.json", StandardCharsets.UTF_8)
                                    .build().toString())
                    .body(new ObjectMapper().writeValueAsBytes(docs));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Не получилось сконвертировать документы, ошибка: %s", e.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Создание документа(ов) ГПЗУ из ПДФ файла")
    public ResponseEntity<List<UplpDoc>> createUplpDoc(
            @Parameter(description = "Загружаемые файлы", required = true)
            @RequestParam("file") List<MultipartFile> files
    ) {
        if (files.size() > 1000) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }
        List<UplpDoc> docs = uplpDocService.createUplpDocs(files);
        if (docs != null && !docs.isEmpty()) {
            return ResponseEntity.ok(docs);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Удаление старого и загрузка нового документа ГПЗУ из ПДФ файла")
    public ResponseEntity<UplpDoc> updateUplpDoc(
            @Parameter(description = "Загружаемый файл", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Идентификатор документа ГПЗУ", required = true)
            @PathVariable final Long id
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
            @PathVariable("id") final Long id
    ) {
        UplpDoc doc = uplpDocService.deleteUplpDocById(id);
        if (doc != null) {
            return ResponseEntity.ok(doc);
        }
        return ResponseEntity.notFound().build();
    }
}
