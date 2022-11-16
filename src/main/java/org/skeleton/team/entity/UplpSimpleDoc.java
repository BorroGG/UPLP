package org.skeleton.team.entity;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Класс для представления простого документа ГПЗУ.
 * Используется для создания файлов в разных форматах только с распаршенными данными без системных полей.
 */
@XmlRootElement(name = "uplpDoc")
@XmlType(propOrder = {
        "recordNo",
        "uplpNo",
        "dateOfIssue",
        "uplpStatus",
        "expiryDate",
        "uplpRecipient",
        "recipientType",
        "administrativeArea",
        "district",
        "buildingAddress",
        "cadastralNo",
        "tlpProjectAvailability",
        "tlpDocumentDetails",
        "surveyingProjectAvailability",
        "surveyingProjectDetails",
        "sutGroupName",
        "sutCodes",
        "plotArea",
        "subzonesAvailability",
        "subzonesArea",
        "buildingMaxHeight",
        "buildingMaxFloors",
        "builtUpAreaPercentage",
        "buildingDensity",
        "cboPurpose",
        "cboDescription",
        "objectsNotUnderConstructionStandarts",
        "totalFloorArea",
        "residentialDevelopmentFloorArea",
        "nonResidentialDevelopmentFloorArea",
        "residentialPremisesFloorArea",
        "nonResidentialPremisesFloorArea",
        "totalBuildingArea",
        "totalResidentialDevelopmentArea",
        "totalNonResidentialDevelopmentArea",
        "totalResidentialPremisesArea",
        "totalNonResidentialPremisesArea",
        "totalUndergroundSpaceArea",
        "subzonesTotalFloorArea",
        "subzonesResidentialObjectsFloorArea",
        "subzonesNonResidentialObjectsFloorArea",
        "subzonesResidentialPremisesFloorArea",
        "subzonesNonResidentialPremisesFloorArea",
        "subzonesTotalObjectsArea",
        "subzonesTotalResidentialObjectsArea",
        "subzonesTotalNonResidentialObjectsArea",
        "subzonesTotalResidentialPremisesArea",
        "subzonesTotalNonResidentialPremisesArea",
        "subzonesTotalUndergroundSpaceArea",
        "existingCboAvailability",
        "existingCboTotalCount",
        "existingCboPurpose",
        "existingCboDescription",
        "existingCboMaxFloorCount",
        "existingCboTotalArea",
        "existingCboResidentialObjectsArea",
        "existingCboNonResidentialObjectsArea",
        "cloAvailability",
        "cboTotalCount",
        "cloDescription",
        "cloIdentificationNo",
        "cloRegistrationNo",
})
@Getter
@Setter
public class UplpSimpleDoc {

    public UplpSimpleDoc() {
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
        this.buildingMaxHeight = "-";
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

    private String recordNo;
    private String uplpNo;
    private Date dateOfIssue;
    private String uplpStatus;
    private Date expiryDate;
    private String uplpRecipient;
    private String recipientType;
    private String administrativeArea;
    private String district;
    private String buildingAddress;
    private String cadastralNo;
    private String tlpProjectAvailability;
    private String tlpDocumentDetails;
    private String surveyingProjectAvailability;
    private String surveyingProjectDetails;
    private String sutGroupName;
    private String sutCodes;
    private Integer plotArea;
    private String subzonesAvailability;
    private Integer subzonesArea;
    private String buildingMaxHeight;
    private Integer buildingMaxFloors;
    private String builtUpAreaPercentage;
    private String buildingDensity;
    private String cboPurpose;
    private String cboDescription;
    private String objectsNotUnderConstructionStandarts;
    private BigDecimal totalFloorArea;
    private BigDecimal residentialDevelopmentFloorArea;
    private BigDecimal nonResidentialDevelopmentFloorArea;
    private BigDecimal residentialPremisesFloorArea;
    private BigDecimal nonResidentialPremisesFloorArea;
    private BigDecimal totalBuildingArea;
    private BigDecimal totalResidentialDevelopmentArea;
    private BigDecimal totalNonResidentialDevelopmentArea;
    private BigDecimal totalResidentialPremisesArea;
    private BigDecimal totalNonResidentialPremisesArea;
    private BigDecimal totalUndergroundSpaceArea;
    private BigDecimal subzonesTotalFloorArea;
    private BigDecimal subzonesResidentialObjectsFloorArea;
    private BigDecimal subzonesNonResidentialObjectsFloorArea;
    private BigDecimal subzonesResidentialPremisesFloorArea;
    private BigDecimal subzonesNonResidentialPremisesFloorArea;
    private BigDecimal subzonesTotalObjectsArea;
    private BigDecimal subzonesTotalResidentialObjectsArea;
    private BigDecimal subzonesTotalNonResidentialObjectsArea;
    private BigDecimal subzonesTotalResidentialPremisesArea;
    private BigDecimal subzonesTotalNonResidentialPremisesArea;
    private BigDecimal subzonesTotalUndergroundSpaceArea;
    private String existingCboAvailability;
    private String existingCboTotalCount;
    private String existingCboPurpose;
    private String existingCboDescription;
    private Integer existingCboMaxFloorCount;
    private BigDecimal existingCboTotalArea;
    private BigDecimal existingCboResidentialObjectsArea;
    private BigDecimal existingCboNonResidentialObjectsArea;
    private String cloAvailability;
    private String cboTotalCount;
    private String cloDescription;
    private String cloIdentificationNo;
    private String cloRegistrationNo;
}
