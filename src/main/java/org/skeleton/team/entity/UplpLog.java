package org.skeleton.team.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

//Класс для представления логов обработки документа ГПЗУ
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Логи обработки документа ГПЗУ")
@Table(name = "uplp_log")
@Entity
public class UplpLog {

    @Id
    @Schema(description = "ИД документа")
    @Column(name = "uplp_log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uplpLogId;

    @Schema(description = "Наименование загруженного файла")
    @Column(name = "file_name")
    private String fileName;

    @Schema(description = "Дата и время загрузки файла")
    @Column(name = "load_dttm")
    private Date loadDttm;

    @Schema(description = "Дата и время обработки файла")
    @Column(name = "process_dttm")
    private Date processDttm;

    @Schema(description = "Результат обработки Успешно/Неуспешно")
    @Column(name = "process_result")
    private String processResult;

    @Schema(description = "Ошибки при обработке")
    @Column(name = "errors")
    private String errors;
}
