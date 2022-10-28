package org.skeleton.team.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Документ ГПЗУ")
@Table(name = "uplp_doc")
@Entity
public class UplpDoc {

    @Id
    @Schema(description = "ИД документа") // TODO оставляем комментарии для всех переменных
    @Column(name = "uplp_doc_id") // TODO здесь имя в БД
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uplpDocId; // TODO здесь тоже самое только в camelCase

    @Schema(description = "Уникальный номер записи") // TODO оставляем комментарии для всех переменных
    @Column(name = "record_no")
    private String recordNo; // TODO обращай внимание на тип переменной, Строка

    @Schema(description = "Площадь земельного  участка (ЗУ), кв.м") // TODO оставляем комментарии для всех переменных
    @Column(name = "plot_square")
    private Integer plotSquare; // TODO число

    @Schema(description = "Площадь нежилой застройки") // TODO оставляем комментарии для всех переменных
    @Column(name = "non_residential_development_square")
    private BigDecimal nonResidentialDevelopmentSquare; // TODO число с запятой

    //TODO добавить остальные поля
}
