package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.entity.UplpSimpleDoc;
import org.skeleton.team.mapper.UplpDocMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/***
 * Класс с точкой входа для тестирования парсера
 */
public class TestingInstance {

    private static UPLPParser parser;

    public static void main(String[] args) throws IOException {

        File file = new File("RU77166000-042753-GPZU.pdf");

        //Спринг сам все сделает, это чисто для теста
        parser = new UPLPParser(new UplpDocMapper() {
            @Override
            public UplpSimpleDoc toSimpleDoc(UplpDoc doc) {
                return null;
            }

            @Override
            public List<UplpSimpleDoc> toSimpleDoc(List<UplpDoc> doc) {
                return null;
            }

            @Override
            public UplpDoc copyDocData(UplpDoc doc) {
                if ( doc == null ) {
                    return null;
                }

                UplpDoc uplpDoc = new UplpDoc();

                uplpDoc.setUplpDocId( doc.getUplpDocId() );
                uplpDoc.setRecordNo( doc.getRecordNo() );
                uplpDoc.setUplpNo( doc.getUplpNo() );
                uplpDoc.setDateOfIssue( doc.getDateOfIssue() );
                uplpDoc.setUplpStatus( doc.getUplpStatus() );
                uplpDoc.setExpiryDate( doc.getExpiryDate() );
                uplpDoc.setUplpRecipient( doc.getUplpRecipient() );
                uplpDoc.setRecipientType( doc.getRecipientType() );
                uplpDoc.setAdministrativeArea( doc.getAdministrativeArea() );
                uplpDoc.setDistrict( doc.getDistrict() );
                uplpDoc.setBuildingAddress( doc.getBuildingAddress() );
                uplpDoc.setCadastralNo( doc.getCadastralNo() );
                uplpDoc.setTlpProjectAvailability( doc.getTlpProjectAvailability() );
                uplpDoc.setTlpDocumentDetails( doc.getTlpDocumentDetails() );
                uplpDoc.setSurveyingProjectAvailability( doc.getSurveyingProjectAvailability() );
                uplpDoc.setSurveyingProjectDetails( doc.getSurveyingProjectDetails() );
                uplpDoc.setSutGroupName( doc.getSutGroupName() );
                uplpDoc.setSutCodes( doc.getSutCodes() );
                uplpDoc.setPlotArea( doc.getPlotArea() );
                uplpDoc.setSubzonesAvailability( doc.getSubzonesAvailability() );
                uplpDoc.setSubzonesArea( doc.getSubzonesArea() );
                uplpDoc.setBuildingMaxHeight( doc.getBuildingMaxHeight() );
                uplpDoc.setBuildingMaxFloors( doc.getBuildingMaxFloors() );
                uplpDoc.setBuiltUpAreaPercentage( doc.getBuiltUpAreaPercentage() );
                uplpDoc.setBuildingDensity( doc.getBuildingDensity() );
                uplpDoc.setCboPurpose( doc.getCboPurpose() );
                uplpDoc.setCboDescription( doc.getCboDescription() );
                uplpDoc.setObjectsNotUnderConstructionStandarts( doc.getObjectsNotUnderConstructionStandarts() );
                uplpDoc.setTotalFloorArea( doc.getTotalFloorArea() );
                uplpDoc.setResidentialDevelopmentFloorArea( doc.getResidentialDevelopmentFloorArea() );
                uplpDoc.setNonResidentialDevelopmentFloorArea( doc.getNonResidentialDevelopmentFloorArea() );
                uplpDoc.setResidentialPremisesFloorArea( doc.getResidentialPremisesFloorArea() );
                uplpDoc.setNonResidentialPremisesFloorArea( doc.getNonResidentialPremisesFloorArea() );
                uplpDoc.setTotalBuildingArea( doc.getTotalBuildingArea() );
                uplpDoc.setTotalResidentialDevelopmentArea( doc.getTotalResidentialDevelopmentArea() );
                uplpDoc.setTotalNonResidentialDevelopmentArea( doc.getTotalNonResidentialDevelopmentArea() );
                uplpDoc.setTotalResidentialPremisesArea( doc.getTotalResidentialPremisesArea() );
                uplpDoc.setTotalNonResidentialPremisesArea( doc.getTotalNonResidentialPremisesArea() );
                uplpDoc.setTotalUndergroundSpaceArea( doc.getTotalUndergroundSpaceArea() );
                uplpDoc.setSubzonesTotalFloorArea( doc.getSubzonesTotalFloorArea() );
                uplpDoc.setSubzonesResidentialObjectsFloorArea( doc.getSubzonesResidentialObjectsFloorArea() );
                uplpDoc.setSubzonesNonResidentialObjectsFloorArea( doc.getSubzonesNonResidentialObjectsFloorArea() );
                uplpDoc.setSubzonesResidentialPremisesFloorArea( doc.getSubzonesResidentialPremisesFloorArea() );
                uplpDoc.setSubzonesNonResidentialPremisesFloorArea( doc.getSubzonesNonResidentialPremisesFloorArea() );
                uplpDoc.setSubzonesTotalObjectsArea( doc.getSubzonesTotalObjectsArea() );
                uplpDoc.setSubzonesTotalResidentialObjectsArea( doc.getSubzonesTotalResidentialObjectsArea() );
                uplpDoc.setSubzonesTotalNonResidentialObjectsArea( doc.getSubzonesTotalNonResidentialObjectsArea() );
                uplpDoc.setSubzonesTotalResidentialPremisesArea( doc.getSubzonesTotalResidentialPremisesArea() );
                uplpDoc.setSubzonesTotalNonResidentialPremisesArea( doc.getSubzonesTotalNonResidentialPremisesArea() );
                uplpDoc.setSubzonesTotalUndergroundSpaceArea( doc.getSubzonesTotalUndergroundSpaceArea() );
                uplpDoc.setExistingCboAvailability( doc.getExistingCboAvailability() );
                uplpDoc.setExistingCboTotalCount( doc.getExistingCboTotalCount() );
                uplpDoc.setExistingCboPurpose( doc.getExistingCboPurpose() );
                uplpDoc.setExistingCboDescription( doc.getExistingCboDescription() );
                uplpDoc.setExistingCboMaxFloorCount(
                        doc.getExistingCboMaxFloorCount() );
                uplpDoc.setExistingCboTotalArea( doc.getExistingCboTotalArea() );
                uplpDoc.setExistingCboResidentialObjectsArea( doc.getExistingCboResidentialObjectsArea() );
                uplpDoc.setExistingCboNonResidentialObjectsArea( doc.getExistingCboNonResidentialObjectsArea() );
                uplpDoc.setCloAvailability( doc.getCloAvailability() );
                uplpDoc.setCboTotalCount( doc.getCboTotalCount() );
                uplpDoc.setCloDescription( doc.getCloDescription() );
                uplpDoc.setCloIdentificationNo( doc.getCloIdentificationNo() );
                uplpDoc.setCloRegistrationNo( doc.getCloRegistrationNo() );
                uplpDoc.setOperatorName( doc.getOperatorName() );
                uplpDoc.setActualisationDate( doc.getActualisationDate() );
                uplpDoc.setFileReference( doc.getFileReference() );
                uplpDoc.setObjectZoneNo( doc.getObjectZoneNo() );
                uplpDoc.setUplpLog( doc.getUplpLog() );

                return uplpDoc;
            }
        });

        //List<UplpDoc> doc = parser.parsingUPLPFile(file, new StringBuilder());

        /* Метод парсит все ГПЗУ файлы в пределах корневого каталога проекта */
        parseAll();

        //doc.forEach(System.out::println);

    }

    private static void parseAll() throws IOException {
        File file = new File(".");

        for(String uplp : file.list()){
            if(uplp.endsWith(".pdf")) {

                System.out.println("======================== START PARSING "+uplp+" ========================");

                File uplpFile = new File(uplp);

                List<UplpDoc> docList = parser.parsingUPLPFile(uplpFile, new StringBuilder());

                docList.forEach(System.out::println);

                System.out.println("======================== END PARSING "+uplp+" ========================");
            }
        }
    }
}
