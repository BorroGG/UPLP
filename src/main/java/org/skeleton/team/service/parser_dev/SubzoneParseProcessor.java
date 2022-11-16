package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.mapper.UplpDocMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubzoneParseProcessor {
    /**
     * Парсинг подзон и атрибутов:<br>
     * - Наличие подзон ЗУ, номера<br>
     * - Площади подзон ЗУ, кв.<br>
     * Метод создаёт новый документ с подзонами и копирует туда общие атрибуты и заполняет соответствующими подзоне.
     * @param documentText текст документа в виде одной строки
     * @param docList список документов для добавления документов с подзонами
     * @param uplpDoc текущий документ
     */
     void parseSubzones(List<UplpDoc> docList, UplpDoc uplpDoc, String documentText, UplpDocMapper uplpDocMapper) {

         String[] docByLines = documentText.split("\n|\r\n");

         String subzone = "";

         int subZoneNumber;

         Pattern subZonePattern = Pattern.compile("Подзона ((№\\s*|N\\s*)(\\d+))");

         Matcher subZoneMatcher = subZonePattern.matcher(String.join(" ", docByLines));

         //Крутим цикл, пока есть подзоны
         while (subZoneMatcher.find()) {

             System.out.println("Найдена " + subZoneMatcher.group());

             subzone = subZoneMatcher.group(2)+subZoneMatcher.group(3);

             subZoneNumber = Integer.parseInt(subZoneMatcher.group(3));

             //В случае, если подзон больше, чем одна, то делим документ
             if (subZoneNumber > 1) {
                 splitAndParseUPlpDocs(docList, uplpDoc, subzone, docByLines, uplpDocMapper);

                 continue;
             }

             //Если это первая или единственная подзона, то изменяем оригинальный документ
             uplpDoc.setRecordNo(uplpDoc.getUplpNo()+subzone);

             uplpDoc.setSubzonesAvailability(subzone);

             parseSequences(uplpDoc, docByLines);
         }

         if (subzone.equals("")) {
             subzone = "нет";

             uplpDoc.setSubzonesAvailability(subzone);

             uplpDoc.setSubzonesArea(0);

             parseBuildingMaxHeight(uplpDoc, docByLines);

             parseBuiltUpAreaPercentage(docByLines, uplpDoc);

             parseBuildingDensity(docByLines, uplpDoc);
         }
     }
     private String[] parseAreaSquare(UplpDoc uplpDoc, String[] docByLines) {

         String subzoneNumber = uplpDoc.getSubzonesAvailability().substring(1);

         Pattern subzonePattern = Pattern.compile("(Подзона №+" + subzoneNumber + ").*");

         Matcher subzoneRestrictMatcher = subzonePattern.matcher(String.join(" ", docByLines));

         if (subzoneRestrictMatcher.find()) {
             String subzoneLine = subzoneRestrictMatcher.group(0);

             Pattern p1 = Pattern.compile("(\\d+\\.\\d*) (га|кв.м)");

             Matcher m1 = p1.matcher(String.join(" ", subzoneLine));

             String areaSquare = "";

             String metrics = "";

             if (m1.find()) {
                 areaSquare = m1.group(1);
                 metrics = m1.group(2);

                 //System.out.println(areaSquare + " "+metrics);
             }
             return new String[]{areaSquare, metrics};
         }
         return new String[]{"0", "0"};
     }
    private void splitAndParseUPlpDocs(List<UplpDoc> docList, UplpDoc uplpDoc, String zoneNumber, String[] docByLines, UplpDocMapper uplpDocMapper){

        //System.out.println("Create sub uplp:" + zoneNumber);

        UplpDoc uplpSubzone = uplpDocMapper.copyDocData(uplpDoc);

        uplpSubzone.setRecordNo(uplpSubzone.getUplpNo()+zoneNumber);

        uplpSubzone.setSubzonesAvailability(zoneNumber);

        parseSequences(uplpSubzone, docByLines);

        docList.add(uplpSubzone);
    }

    private void parseSequences(UplpDoc subZoneDoc, String[] docByLines) {
        calculateAreaSquare(subZoneDoc, docByLines);

        parseBuildingMaxHeight(subZoneDoc, docByLines);

        parseBuiltUpAreaPercentage(docByLines, subZoneDoc);

        parseBuildingDensity(docByLines, subZoneDoc);

    }

    private void parseBuildingDensity(String[] words, UplpDoc uplpDoc){

        String subzoneNumber = uplpDoc.getSubzonesAvailability().substring(1);

        Pattern subzonePattern = Pattern.compile("(Подзона №+"+subzoneNumber+").*");

        Matcher subzoneRestrictMatcher = subzonePattern.matcher(String.join(" ", words));

        if(subzoneRestrictMatcher.find()){
            String subzoneLine = subzoneRestrictMatcher.group(0);

            Pattern buildingDensityPattern = Pattern.compile("Максимальная плотность \\(тыс.кв.м/га\\) - (\\d+.\\d+|\\d+|без  ограничений|без ограничений|не установлен|по  существующему  положению|Действие  градостроительного  регламента  не  распространяется)");

            Matcher buildingDensityMatcher = buildingDensityPattern.matcher(String.join(" ", subzoneLine));

            if(buildingDensityMatcher.find()){
                //System.out.println(buildingDensityMatcher.group());
                //System.out.println(buildingDensityMatcher.group(1));
                uplpDoc.setBuildingDensity(buildingDensityMatcher.group(1));
            }
        }
    }

    private void parseBuiltUpAreaPercentage(String[] words, UplpDoc uplpDoc) {
        String subzoneNumber = uplpDoc.getSubzonesAvailability().substring(1);

        Pattern subzonePattern = Pattern.compile("(Подзона №+"+subzoneNumber+").*");

        Matcher subzoneRestrictMatcher = subzonePattern.matcher(String.join(" ", words));

        if(subzoneRestrictMatcher.find()){
            //System.out.println(subzoneRestrictMatcher.group(0));

            String subzoneLine = subzoneRestrictMatcher.group(0);

            Pattern areaPercentagePattern = Pattern.compile("(\\(%\\) - (\\d+|без  ограничений|без ограничений|не установлен|по  существующему  положению|Действие  градостроительного  регламента  не  распространяется))");

            Matcher areaPercentageMatcher = areaPercentagePattern.matcher(String.join(" ", subzoneLine));

            if(areaPercentageMatcher.find(0)){
                //System.out.println(areaPercentageMatcher.group(0));
                //System.out.println(areaPercentageMatcher.group(1));
                //System.out.println(areaPercentageMatcher.group(2));
                uplpDoc.setBuiltUpAreaPercentage(areaPercentageMatcher.group(2));
            }

        }
    }

    private void calculateAreaSquare(UplpDoc uplpDoc, String[] docByLines) {

        String[] areaSquareWithMetrics = parseAreaSquare(uplpDoc, docByLines);

        //System.out.println("Area square = "+areaSquareWithMetrics[0] + " "+areaSquareWithMetrics[1]);

        String areaSquare = areaSquareWithMetrics[0];

        String metrics = areaSquareWithMetrics[1];

        float area = 0f;

        switch (metrics.toLowerCase()) {
            case "га": {
                area = Float.parseFloat(areaSquare.replace(',', '.')) * 10000;
                uplpDoc.setSubzonesArea((int) area);
                break;
            }
            case "кв.м": {
                uplpDoc.setSubzonesArea((int)Float.parseFloat(areaSquare));
                break;
            }
        }
    }


    /**
     * Парсинг атрибута максимальная высота застройки
     * @param uplpDoc сущность документа для внесения атрибута
     * @param words текстовый массив строк
     */
    private void parseBuildingMaxHeight(UplpDoc uplpDoc, String[] words) {
        int maxHeight = 0;

        String subzoneNumber = uplpDoc.getSubzonesAvailability().substring(1);

        Pattern subzonePattern = Pattern.compile("(Подзона №+"+subzoneNumber+").*");

        Matcher subzoneRestrictMatcher = subzonePattern.matcher(String.join(" ", words));

        if(subzoneRestrictMatcher.find()) {

            String subzoneLine = subzoneRestrictMatcher.group(0);

            Pattern heightPattern = Pattern.compile("(\\(м\\.\\) - (\\d+|без  ограничений|без ограничений|не установлен|по  существующему  положению|Действие  градостроительного  регламента  не  распространяется))");

            Matcher heightMatcher = heightPattern.matcher(String.join(" ", subzoneLine));
            if (heightMatcher.find()) {
                //System.out.println(heightMatcher.group(0));
                //System.out.println(heightMatcher.group(1));
                //System.out.println(heightMatcher.group(2));
                uplpDoc.setBuildingMaxHeight(heightMatcher.group(2));
            }
        }
    }
}