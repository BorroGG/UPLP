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
    @Schema(description = "ИД документа")
    @Column(name = "uplp_doc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uplpDocId;

    @Schema(description = "Уникальный номер записи")
    @Column(name = "record_no")
    private String recordNo;

    @Schema(description = "Номер ГПЗУ")
    @Column(name = "uplp_no")
    private String uplpNo;

    @Schema(description = "Дата выдачи ГПЗУ")
    @Column(name = "date_of_issue")
    private String dateOfIssue;

    @Schema(description = "Статус ГПЗУ")
    @Column(name = "uplp_status")
    private String uplpStatus;

    @Schema(description = "Срок действия ГПЗУ")
    @Column(name = "expiry_date")
    private String expiryDate;

    @Schema(description = "Правообладатель или иной получатель ГПЗУ")
    @Column(name = "uplp_recipient")
    private String uplpRecipient;

    @Schema(description = "Тип правообладателя или получателя ГПЗУ")
    @Column(name = "recipient_type")
    private String recipientType;

    @Schema(description = "Административный округ")
    @Column(name = "administrative_area")
    private String administrativeArea;

    @Schema(description = "Район (поселение)")
    @Column(name = "district")
    private String district;

    @Schema(description = "Строительный адрес")
    @Column(name = "building_address")
    private String buildingAddress;

    @Schema(description = "Кадастровый номер земельного участка (ЗУ)")
    @Column(name = "cadastral_no")
    private String cadastralNo;

    @Schema(description = "Наличие проекта планоровки территории (ППТ) в границах ГПЗУ")
    @Column(name = "tlp_project_availability")
    private String tlpProjectAvailability;

    @Schema(description = "Реквизиты документа ППТ")
    @Column(name = "tlp_document_details")
    private String tlpDocumentDetails;

    @Schema(description = "Наличие отдельного проекта межевания территории в границах ГПЗУ")
    @Column(name = "surveying_project_availability")
    private String surveyingProjectAvailability;

    @Schema(description = "Реквизиты документа проекта межевания")
    @Column(name = "surveying_project_details")
    private String surveyingProjectDetails;

    @Schema(description = "Наименование условной группы использования ЗУ по ВРИ")
    @Column(name = "sut_group_name")
    private String sutGroupName;

    @Schema(description = "Коды основных видов разрешенного использования (ВРИ) зумельного участка (ЗУ)")
    @Column(name = "sut_codes")
    private String sutCodes;

    @Schema(description = "Площадь земельного участка (ЗУ), кв.м")
    @Column(name = "plot_area")
    private Integer plotArea;

    @Schema(description = "Наличие подзон ЗУ, номера")
    @Column(name = "subzones_availability")
    private String subzonesAvailability;

    @Schema(description = "Площади подзон ЗУ, кв.м")
    @Column(name = "subzones_area")
    private Integer subzonesArea;

    @Schema(description = "Высота застройки, м")
    @Column(name = "building_max_height")
    private String buildingMaxHeight;

    @Schema(description = "Количество этажей, шт")
    @Column(name = "building_max_floors")
    private Integer buildingMaxFloors;

    @Schema(description = "Процент застроенности, %")
    @Column(name = "built_up_area_percentage")
    private String builtUpAreaPercentage;

    @Schema(description = "Плотность застройки, тыс. кв.м/га")
    @Column(name = "building_density")
    private String buildingDensity;

    @Schema(description = "Назначение ОКС")
    @Column(name = "cbo_purpose")
    private String cboPurpose;

    @Schema(description = "Наименование, описание ОКС")
    @Column(name = "cbo_description")
    private String cboDescription;

    @Schema(description = "Наличие объектов, на которые действие градостроительного регламента не распространяется или не устанавливается")
    @Column(name = "objects_not_under_construction_standarts")
    private String objectsNotUnderConstructionStandarts;

    @Schema(description = "Всего")
    @Column(name = "total_floor_area")
    private BigDecimal totalFloorArea;

    @Schema(description = "Жилой застройки")
    @Column(name = "residential_development_floor_area")
    private BigDecimal residentialDevelopmentFloorArea;

    @Schema(description = "Нежилой застройки")
    @Column(name = "non_residential_development_floor_area")
    private BigDecimal nonResidentialDevelopmentFloorArea;

    @Schema(description = "Жилых помещений")
    @Column(name = "residential_premises_floor_area")
    private BigDecimal residentialPremisesFloorArea;

    @Schema(description = "Встроенно-пристроенных, отдельно стоящих нежилых помещений")
    @Column(name = "non_residential_premises_floor_area")
    private BigDecimal nonResidentialPremisesFloorArea;

    @Schema(description = "Всего")
    @Column(name = "total_building_area")
    private BigDecimal totalBuildingArea;

    @Schema(description = "Жилой застройки")
    @Column(name = "total_residential_development_area")
    private BigDecimal totalResidentialDevelopmentArea;

    @Schema(description = "Нежилой застройки")
    @Column(name = "total_non_residential_development_area")
    private BigDecimal totalNonResidentialDevelopmentArea;

    @Schema(description = "Жилых помещений")
    @Column(name = "total_residential_premises_area")
    private BigDecimal totalResidentialPremisesArea;

    @Schema(description = "Встроенно-пристроенных, отдельно стоящих нежилых помещений")
    @Column(name = "total_non_residential_premises_area")
    private BigDecimal totalNonResidentialPremisesArea;

    @Schema(description = "Подземного пространства")
    @Column(name = "total_underground_space_area")
    private BigDecimal totalUndergroundSpaceArea;

    @Schema(description = "Всех объектов")
    @Column(name = "subzones_total_floor_area")
    private BigDecimal subzonesTotalFloorArea;

    @Schema(description = "Жилых объектов")
    @Column(name = "subzones_residential_objects_floor_area")
    private BigDecimal subzonesResidentialObjectsFloorArea;

    @Schema(description = "Нежилых объектов")
    @Column(name = "subzones_non_residential_objects_floor_area")
    private BigDecimal subzonesNonResidentialObjectsFloorArea;

    @Schema(description = "Жилых помещений")
    @Column(name = "subzones_residential_premises_floor_area")
    private BigDecimal subzonesResidentialPremisesFloorArea;

    @Schema(description = "Встроенно-пристроенных, отдельно стоящих нежилых помещений")
    @Column(name = "subzones_non_residential_premises_floor_area")
    private BigDecimal subzonesNonResidentialPremisesFloorArea;

    @Schema(description = "Всех объектов")
    @Column(name = "subzones_total_objects_area")
    private BigDecimal subzonesTotalObjectsArea;

    @Schema(description = "Жилых объектов")
    @Column(name = "subzones_total_residential_objects_area")
    private BigDecimal subzonesTotalResidentialObjectsArea;

    @Schema(description = "Нежилых объектов")
    @Column(name = "subzones_total_non_residential_objects_area")
    private BigDecimal subzonesTotalNonResidentialObjectsArea;

    @Schema(description = "Жилых помещений")
    @Column(name = "subzones_total_residential_premises_area")
    private BigDecimal subzonesTotalResidentialPremisesArea;

    @Schema(description = "Встроенно-пристроенных, отдельно стоящих нежилых помещений")
    @Column(name = "subzones_total_non_residential_premises_area")
    private BigDecimal subzonesTotalNonResidentialPremisesArea;

    @Schema(description = "Подземного пространства")
    @Column(name = "subzones_total_underground_space_area")
    private BigDecimal subzonesTotalUndergroundSpaceArea;

    @Schema(description = "Наличие или отсутствие существующих на ЗУ ОКС")
    @Column(name = "existing_cbo_availability")
    private String existingCboAvailability;

    @Schema(description = "Общее число существующих на ЗУ ОКС, единиц")
    @Column(name = "existing_cbo_total_count")
    private String existingCboTotalCount;

    @Schema(description = "Назначение существующих ОКС")
    @Column(name = "existing_cbo_purpose")
    private String existingCboPurpose;

    @Schema(description = "Наименование, описание существующих ОКС")
    @Column(name = "existing_cbo_description")
    private String existingCboDescription;

    @Schema(description = "Максимальное число наземных этажей существующих ОКС, шт")
    @Column(name = "existing_cbo_max_floor_count")
    private Integer existingCboMaxFloorCount;

    @Schema(description = "Общая")
    @Column(name = "existing_cbo_total_area")
    private BigDecimal existingCboTotalArea;

    @Schema(description = "Жилых объектов")
    @Column(name = "existing_cbo_residential_objects_area")
    private BigDecimal existingCboResidentialObjectsArea;

    @Schema(description = "Нежилых объектов")
    @Column(name = "existing_cbo_non_residential_objects_area")
    private BigDecimal existingCboNonResidentialObjectsArea;

    @Schema(description = "Наличие или отсутствие существующих на ЗУ ОКН")
    @Column(name = "clo_availability")
    private String cloAvailability;

    @Schema(description = "Общее число существующих на ЗУ ОКН")
    @Column(name = "clo_total_count")
    private String cboTotalCount;

    @Schema(description = "Наименование, описание ОКН")
    @Column(name = "clo_description")
    private String cloDescription;

    @Schema(description = "Идентификационный номер ОКН")
    @Column(name = "clo_identification_no")
    private String cloIdentificationNo;

    @Schema(description = "Регистрационный номер ОКН")
    @Column(name = "clo_registration_no")
    private String cloRegistrationNo;

    @Schema(description = "Исполнитель (оператор), ФИО")
    @Column(name = "operator_name")
    private String operatorName;

    @Schema(description = "Дата актуализации документа")
    @Column(name = "actualisation_date")
    private String actualisationDate;
}