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

/**
 * Класс контроллера для работы с документами ГПЗУ.
 */
@Tag(name = "Документы ГПЗУ", description = "Взаимодействие с документами ГПЗУ")
@RestController
@RequestMapping("uplp")
@RequiredArgsConstructor
public class UplpDocController {

    private final UplpDocService uplpDocService;

    /**
     * Получение документа ГПЗУ по ИД.
     * @param id Идентификатор документа ГПЗУ
     * @return документ ГПЗУ
     */
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

    /**
     * Получение нескольких документов ГПЗУ по ИД.
     * @param ids Идентификаторы документов ГПЗУ
     * @return документы ГПЗУ
     */
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

    /**
     * Получение всех документов ГПЗУ.
     * @return документы ГПЗУ
     */
    @GetMapping("/all")
    @Operation(summary = "Получение всех документов ГПЗУ")
    public ResponseEntity<List<UplpDoc>> getAllUplpDocs() {
        return ResponseEntity.ok(uplpDocService.getAllUplpDocs());
    }

    /**
     * Операция получения нескольких документов ГПЗУ по ИД в формате xlsx.
     * @param ids Идентификаторы документов ГПЗУ
     * @param response документы в формате xlsx
     */
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

    /**
     * Операция получения нескольких документов ГПЗУ по ИД в формате xml.
     * @param ids Идентификаторы документов ГПЗУ
     * @param response документы в формате xml
     */
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

    /**
     * Операция получения нескольких документов ГПЗУ по ИД в формате csv.
     * @param ids Идентификаторы документов ГПЗУ
     * @param response документы в формате csv
     */
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

    /**
     * Операция получения нескольких документов ГПЗУ по ИД в формате json.
     * @param ids Идентификаторы документов ГПЗУ
     * @return документы в формате json
     */
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

    /**
     * Операция получения одного или нескольких документов ГПЗУ из PDF-файлов.
     * @param file документы ГПЗУ в формате PDF
     * @return документы ГПЗУ
     */
    @PostMapping
    @Operation(summary = "Создание документа(ов) ГПЗУ из ПДФ файла")
    public ResponseEntity<List<UplpDoc>> createUplpDoc(
            @Parameter(description = "Загружаемые файлы", required = true)
            @RequestBody List<MultipartFile> file
    ) {
        if (file.size() > 1000) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
        }
        List<UplpDoc> docs = uplpDocService.createUplpDocs(file);
        if (docs != null && !docs.isEmpty()) {
            return ResponseEntity.ok(docs);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /**
     * Операция удаления старого и загрузки нового документа ГПЗУ из PDF-файла.
     * @param file новый документ в формате PDF
     * @param id Идентификатор документа ГПЗУ для обновления
     * @return новый документ ГПЗУ
     */
    @PutMapping("/{id}")
    @Operation(summary = "Удаление старого и загрузка нового документа ГПЗУ из ПДФ файла")
    public ResponseEntity<List<UplpDoc>> updateUplpDoc(
            @Parameter(description = "Загружаемый файл", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Идентификатор документа ГПЗУ", required = true)
            @PathVariable final Long id
    ) {
        List<UplpDoc> docs = uplpDocService.updateUplpDoc(file, id);
        if (docs != null && !docs.isEmpty()) {
            return ResponseEntity.ok(docs);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    /**
     * Операция удаления документа ГПЗУ по ИД.
     * @param id Идентификатор документа ГПЗУ для удаления
     * @return удаленный документ ГПЗУ
     */
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
