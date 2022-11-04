package org.skeleton.team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Класс для представления документа ГПЗУ.
 */
@Getter
@Setter
@Schema(description = "Документ ГПЗУ")
@Table(name = "uplp_doc")
@Entity
public class UplpDoc {

    /**
     * Простановка дефолтных занчений для строковых данных.
     */
    public UplpDoc() {
        this.recordNo = "-";
        this.uplpNo = "-";
        this.uplpStatus = "-";
        this.uplpRecipient = "-";
        this.recipientType = "-";
        this.administrativeArea = "-";
        this.district = "-";
        this.buildingAddress = "-";
        this.cadastralNo = "-";
        this.tlpProjectAvailability = "-";
        this.tlpDocumentDetails = "-";
        this.surveyingProjectAvailability = "-";
        this.surveyingProjectDetails = "-";
        this.sutGroupName = "-";
        this.sutCodes = "-";
        this.subzonesAvailability = "-";
        this.builtUpAreaPercentage = "-";
        this.buildingDensity = "-";
        this.cboPurpose = "-";
        this.cboDescription = "-";
        this.objectsNotUnderConstructionStandarts = "-";
        this.existingCboAvailability = "-";
        this.existingCboTotalCount = "-";
        this.existingCboPurpose = "-";
        this.existingCboDescription = "-";
        this.cloAvailability = "-";
        this.cboTotalCount = "-";
        this.cloDescription = "-";
        this.cloIdentificationNo = "-";
        this.cloRegistrationNo = "-";
    }

    @Id
    @Schema(description = "ИД документа")
    @Column(name = "uplp_doc_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uplpDocId;

    @Schema(description = "Уникальный номер записи")
    @Column(name = "record_no")
    private String recordNo;

    @Schema(description = "Номер ГПЗУ")
    @Column(name = "uplp_no")
    private String uplpNo;

    @Schema(description = "Дата выдачи ГПЗУ")
    @Column(name = "date_of_issue")
    private Date dateOfIssue;

    @Schema(description = "Статус ГПЗУ")
    @Column(name = "uplp_status")
    private String uplpStatus;

    @Schema(description = "Срок действия ГПЗУ")
    @Column(name = "expiry_date")
    private Date expiryDate;

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

    @Schema(description = "Максимальная высота застройки, м")
    @Column(name = "building_max_height")
    private BigDecimal buildingMaxHeight;

    @Schema(description = "Максимальное количество этажей, шт")
    @Column(name = "building_max_floors")
    private Integer buildingMaxFloors;

    @Schema(description = "Максимальный процент застроенности, %")
    @Column(name = "built_up_area_percentage")
    private String builtUpAreaPercentage;

    @Schema(description = "Максимальная плотность застройки, тыс. кв.м/га")
    @Column(name = "building_density")
    private String buildingDensity;

    @Schema(description = "Назначение объектов капитального строительства (ОКС)")
    @Column(name = "cbo_purpose")
    private String cboPurpose;

    @Schema(description = "Наименование, описание ОКС")
    @Column(name = "cbo_description")
    private String cboDescription;

    @Schema(description = "Наличие объектов, на которые действие градостроительного регламента не распространяется или не устанавливается")
    @Column(name = "objects_not_under_construction_standarts")
    private String objectsNotUnderConstructionStandarts;

    @Schema(description = "Общая суммарная поэтажная площадь строящихся ОКС, кв.м")
    @Column(name = "total_floor_area")
    private BigDecimal totalFloorArea;

    @Schema(description = "Суммарная поэтажная площадь строящихся объектов жилой застройки, кв.м")
    @Column(name = "residential_development_floor_area")
    private BigDecimal residentialDevelopmentFloorArea;

    @Schema(description = "Суммарная поэтажная площадь строящихся объектов нежилой застройки, кв.м")
    @Column(name = "non_residential_development_floor_area")
    private BigDecimal nonResidentialDevelopmentFloorArea;

    @Schema(description = "Суммарная поэтажная площадь строящихся жилых помещений, кв.м")
    @Column(name = "residential_premises_floor_area")
    private BigDecimal residentialPremisesFloorArea;

    @Schema(description = "Суммарная поэтажная площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений, кв.м")
    @Column(name = "non_residential_premises_floor_area")
    private BigDecimal nonResidentialPremisesFloorArea;

    @Schema(description = "Общая площадь строящихся ОКС, кв.м")
    @Column(name = "total_building_area")
    private BigDecimal totalBuildingArea;

    @Schema(description = "Общая площадь строящихся объектов жилой застройки, кв.м")
    @Column(name = "total_residential_development_area")
    private BigDecimal totalResidentialDevelopmentArea;

    @Schema(description = "Общая площадь строящихся объектов нежилой застройки, кв.м")
    @Column(name = "total_non_residential_development_area")
    private BigDecimal totalNonResidentialDevelopmentArea;

    @Schema(description = "Общая площадь строящихся жилых помещений, кв.м")
    @Column(name = "total_residential_premises_area")
    private BigDecimal totalResidentialPremisesArea;

    @Schema(description = "Общая площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений, кв.м")
    @Column(name = "total_non_residential_premises_area")
    private BigDecimal totalNonResidentialPremisesArea;

    @Schema(description = "Общая площадь подземного пространства, кв.м")
    @Column(name = "total_underground_space_area")
    private BigDecimal totalUndergroundSpaceArea;

    @Schema(description = "Общая суммарная поэтажная площадь строящихся ОКС по всем подзонам, кв.м")
    @Column(name = "subzones_total_floor_area")
    private BigDecimal subzonesTotalFloorArea;

    @Schema(description = "Общая суммарная поэтажная площадь строящихся жилых объектов по всем подзонам, кв.м")
    @Column(name = "subzones_residential_objects_floor_area")
    private BigDecimal subzonesResidentialObjectsFloorArea;

    @Schema(description = "Общая суммарная поэтажная площадь строящихся нежилых объектов по всем подзонам, кв.м")
    @Column(name = "subzones_non_residential_objects_floor_area")
    private BigDecimal subzonesNonResidentialObjectsFloorArea;

    @Schema(description = "Общая суммарная поэтажная площадь строящихся жилых помещений по всем подзонам, кв.м")
    @Column(name = "subzones_residential_premises_floor_area")
    private BigDecimal subzonesResidentialPremisesFloorArea;

    @Schema(description = "Общая суммарная поэтажная площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений по всем подзонам, кв.м")
    @Column(name = "subzones_non_residential_premises_floor_area")
    private BigDecimal subzonesNonResidentialPremisesFloorArea;

    @Schema(description = "Общая площадь строящихся ОКС по всем подзонам, кв.м")
    @Column(name = "subzones_total_objects_area")
    private BigDecimal subzonesTotalObjectsArea;

    @Schema(description = "Общая площадь строящихся жилых объектов по всем подзонам, кв.м")
    @Column(name = "subzones_total_residential_objects_area")
    private BigDecimal subzonesTotalResidentialObjectsArea;

    @Schema(description = "Общая площадь строящихся нежилых объектов по всем подзонам, кв.м")
    @Column(name = "subzones_total_non_residential_objects_area")
    private BigDecimal subzonesTotalNonResidentialObjectsArea;

    @Schema(description = "Общая площадь строящихся жилых помещений по всем подзонам, кв.м")
    @Column(name = "subzones_total_residential_premises_area")
    private BigDecimal subzonesTotalResidentialPremisesArea;

    @Schema(description = "Общая площадь строящихся встроенно-пристроенных, отдельно стоящих нежилых помещений по всем подзонам, кв.м")
    @Column(name = "subzones_total_non_residential_premises_area")
    private BigDecimal subzonesTotalNonResidentialPremisesArea;

    @Schema(description = "Общая площадь поздемного пространства по всем подзонам, кв.м")
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

    //Доп данные
    @Schema(description = "Исполнитель (оператор), ФИО")
    @Column(name = "operator_name")
    private String operatorName;

    @Schema(description = "Дата актуализации документа")
    @Column(name = "actualisation_date")
    private Date actualisationDate;

    //Системные данные
    @Schema(description = "Ссылка на исходный файл в системе")
    @Column(name = "file_reference")
    @JsonIgnore
    private String fileReference;

    @Schema(description = "Логи обработки документа ГПЗУ")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uplp_log_id", referencedColumnName = "uplp_log_id")
    @JsonIgnore
    private UplpLog uplpLog;



    @Override
    public String toString() {
        return "{\"UplpDoc\":{"
                + "\"uplpDocId\":\"" + uplpDocId + "\""
                + ",\n \"recordNo\":\"" + recordNo + "\""
                + ",\n \"uplpNo\":\"" + uplpNo + "\""
                + ",\n \"dateOfIssue\":\"" + dateOfIssue + "\""
                + ",\n \"uplpStatus\":\"" + uplpStatus + "\""
                + ",\n \"expiryDate\":\"" + expiryDate + "\""
                + ",\n \"uplpRecipient\":\"" + uplpRecipient + "\""
                + ",\n \"recipientType\":\"" + recipientType + "\""
                + ",\n \"administrativeArea\":\"" + administrativeArea + "\""
                + ",\n \"district\":\"" + district + "\""
                + ",\n \"buildingAddress\":\"" + buildingAddress + "\""
                + ",\n \"cadastralNo\":\"" + cadastralNo + "\""
                + ",\n \"tlpProjectAvailability\":\"" + tlpProjectAvailability + "\""
                + ",\n \"tlpDocumentDetails\":\"" + tlpDocumentDetails + "\""
                + ",\n \"surveyingProjectAvailability\":\"" + surveyingProjectAvailability + "\""
                + ",\n \"surveyingProjectDetails\":\"" + surveyingProjectDetails + "\""
                + ",\n \"sutGroupName\":\"" + sutGroupName + "\""
                + ",\n \"sutCodes\":\"" + sutCodes + "\""
                + ",\n \"plotArea\":\"" + plotArea + "\""
                + ",\n \"subzonesAvailability\":\"" + subzonesAvailability + "\""
                + ",\n \"subzonesArea\":\"" + subzonesArea + "\""
                + ",\n \"buildingMaxHeight\":\"" + buildingMaxHeight + "\""
                + ",\n \"buildingMaxFloors\":\"" + buildingMaxFloors + "\""
                + ",\n \"builtUpAreaPercentage\":\"" + builtUpAreaPercentage + "\""
                + ",\n \"buildingDensity\":\"" + buildingDensity + "\""
                + ",\n \"cboPurpose\":\"" + cboPurpose + "\""
                + ",\n \"cboDescription\":\"" + cboDescription + "\""
                + ",\n \"objectsNotUnderConstructionStandarts\":\"" + objectsNotUnderConstructionStandarts + "\""
                + ",\n \"totalFloorArea\":\"" + totalFloorArea + "\""
                + ",\n \"residentialDevelopmentFloorArea\":\"" + residentialDevelopmentFloorArea + "\""
                + ",\n \"nonResidentialDevelopmentFloorArea\":\"" + nonResidentialDevelopmentFloorArea + "\""
                + ",\n \"residentialPremisesFloorArea\":\"" + residentialPremisesFloorArea + "\""
                + ",\n \"nonResidentialPremisesFloorArea\":\"" + nonResidentialPremisesFloorArea + "\""
                + ",\n \"totalBuildingArea\":\"" + totalBuildingArea + "\""
                + ",\n \"totalResidentialDevelopmentArea\":\"" + totalResidentialDevelopmentArea + "\""
                + ",\n \"totalNonResidentialDevelopmentArea\":\"" + totalNonResidentialDevelopmentArea + "\""
                + ",\n \"totalResidentialPremisesArea\":\"" + totalResidentialPremisesArea + "\""
                + ",\n \"totalNonResidentialPremisesArea\":\"" + totalNonResidentialPremisesArea + "\""
                + ",\n \"totalUndergroundSpaceArea\":\"" + totalUndergroundSpaceArea + "\""
                + ",\n \"subzonesTotalFloorArea\":\"" + subzonesTotalFloorArea + "\""
                + ",\n \"subzonesResidentialObjectsFloorArea\":\"" + subzonesResidentialObjectsFloorArea + "\""
                + ",\n \"subzonesNonResidentialObjectsFloorArea\":\"" + subzonesNonResidentialObjectsFloorArea + "\""
                + ",\n \"subzonesResidentialPremisesFloorArea\":\"" + subzonesResidentialPremisesFloorArea + "\""
                + ",\n \"subzonesNonResidentialPremisesFloorArea\":\"" + subzonesNonResidentialPremisesFloorArea + "\""
                + ",\n \"subzonesTotalObjectsArea\":\"" + subzonesTotalObjectsArea + "\""
                + ",\n \"subzonesTotalResidentialObjectsArea\":\"" + subzonesTotalResidentialObjectsArea + "\""
                + ",\n \"subzonesTotalNonResidentialObjectsArea\":\"" + subzonesTotalNonResidentialObjectsArea + "\""
                + ",\n \"subzonesTotalResidentialPremisesArea\":\"" + subzonesTotalResidentialPremisesArea + "\""
                + ",\n \"subzonesTotalNonResidentialPremisesArea\":\"" + subzonesTotalNonResidentialPremisesArea + "\""
                + ",\n \"subzonesTotalUndergroundSpaceArea\":\"" + subzonesTotalUndergroundSpaceArea + "\""
                + ",\n \"existingCboAvailability\":\"" + existingCboAvailability + "\""
                + ",\n \"existingCboTotalCount\":\"" + existingCboTotalCount + "\""
                + ",\n \"existingCboPurpose\":\"" + existingCboPurpose + "\""
                + ",\n \"existingCboDescription\":\"" + existingCboDescription + "\""
                + ",\n \"existingCboMaxFloorCount\":\"" + existingCboMaxFloorCount + "\""
                + ",\n \"existingCboTotalArea\":\"" + existingCboTotalArea + "\""
                + ",\n \"existingCboResidentialObjectsArea\":\"" + existingCboResidentialObjectsArea + "\""
                + ",\n \"existingCboNonResidentialObjectsArea\":\"" + existingCboNonResidentialObjectsArea + "\""
                + ",\n \"cloAvailability\":\"" + cloAvailability + "\""
                + ",\n \"cboTotalCount\":\"" + cboTotalCount + "\""
                + ",\n \"cloDescription\":\"" + cloDescription + "\""
                + ",\n \"cloIdentificationNo\":\"" + cloIdentificationNo + "\""
                + ",\n \"cloRegistrationNo\":\"" + cloRegistrationNo + "\""
                + ",\n \"operatorName\":\"" + operatorName + "\""
                + ",\n \"actualisationDate\":\"" + actualisationDate + "\""
                + "}}";
    }
}