package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.mapper.UplpDocMapper;

import java.math.BigDecimal;
import java.util.List;

public class SubzoneParseProcessor {
    /**
     * Парсинг подзон и атрибутов:<br>
     * - Наличие подзон ЗУ, номера<br>
     * - Площади подзон ЗУ, кв.<br>
     * Метод создаёт новый документ с подзонами и копирует туда общие атрибуты и заполняет соответствующими подзоне.
     * @param words текстовый массив строк документов
     * @param docList список документов для добавления документов с подзонами
     * @param uplpDoc текущий документ
     */
     void parseSubzones(List<UplpDoc> docList, UplpDoc uplpDoc, String[] words, UplpDocMapper uplpDocMapper) {
        String subzones = "";

        String areaSquare = "";

        for(String word : words) {
            if(word.matches("Подзона № \\d+.+")){
                String zoneNumber = "№" + word.split(" ")[2];

                //System.out.println(zoneNumber);

                if(subzones.length() > 0){
                    splitUPlpDocs(docList, uplpDoc, word.split(" на чертеже ")[1], zoneNumber, words, uplpDocMapper);

                    subzones += zoneNumber;

                    uplpDoc.setSubzonesAvailability(subzones);

                    continue;
                }

                uplpDoc.setRecordNo(uplpDoc.getUplpNo() + zoneNumber.strip());

                subzones += zoneNumber;

                uplpDoc.setSubzonesAvailability(subzones);

                areaSquare = word.split(" на чертеже ")[1];

                parseAreaSquare(uplpDoc, areaSquare);

                //System.out.println(word);

            } else if (word.matches("Подзона №\\d+.+")) {

                String zoneNumber = "№" + word.split(" ")[1].replace('№',' ')+" ";

                //System.out.println(word);

                if(subzones.length() > 0){
                    splitUPlpDocs(docList, uplpDoc, word.split(" на чертеже ")[1], zoneNumber, words, uplpDocMapper);

                    subzones += zoneNumber;

                    uplpDoc.setSubzonesAvailability(subzones);

                    continue;
                }

                uplpDoc.setRecordNo(uplpDoc.getUplpNo() + zoneNumber.strip());

                subzones += zoneNumber;

                uplpDoc.setSubzonesAvailability(subzones);

                areaSquare = word.split(" на чертеже ")[1];

                parseAreaSquare(uplpDoc, areaSquare);

                //System.out.println(word);

            } else if (word.matches("Подзона N\\d+.+")) {

                String zoneNumber = "№" + word.split(" ")[1].substring(1);

                if(subzones.length() > 0){
                    splitUPlpDocs(docList, uplpDoc, word.split(" на чертеже ")[1], zoneNumber, words, uplpDocMapper);

                    subzones += zoneNumber;

                    continue;
                }

                uplpDoc.setRecordNo(uplpDoc.getUplpNo() + zoneNumber.strip());

                subzones += zoneNumber;

                areaSquare = word.split(" на чертеже ")[1];

                //System.out.println(subzones);

                parseAreaSquare(uplpDoc, areaSquare);

                parseBuildingMaxHeight(uplpDoc, words);

                parseBuiltUpAreaPercentage(words, uplpDoc);
            }
        }
        if(subzones.equals("")) {
            subzones = "нет";
            uplpDoc.setSubzonesAvailability(subzones);
            uplpDoc.setSubzonesArea(0);
            parseBuildingMaxHeight(uplpDoc, words);

            parseBuiltUpAreaPercentage(words, uplpDoc);

            parseBuildingDensity(words, uplpDoc);
        }
    }

    private void splitUPlpDocs(List<UplpDoc> docList, UplpDoc uplpDoc, String areaSquareText, String zoneNumber, String[] words, UplpDocMapper uplpDocMapper){

        System.out.println("Create sub uplp:" + zoneNumber);

        UplpDoc uplpSubzone = uplpDocMapper.copyDocData(uplpDoc);

        uplpDoc.setRecordNo(uplpSubzone.getUplpNo()+zoneNumber.strip());

        parseAreaSquare(uplpSubzone, areaSquareText);

        parseBuildingMaxHeight(uplpSubzone, words);

        parseBuiltUpAreaPercentage(words, uplpSubzone);

        parseBuildingDensity(words, uplpDoc);

        docList.add(uplpSubzone);
    }

    private void parseBuildingDensity(String[] words, UplpDoc uplpDoc){
        for (String word : words){
            if(word.matches(".+Максимальная плотность.+")){
                //System.out.println(word);

                String density = word.split(" Максимальная плотность \\(тыс.кв.м/га\\) - ")[1];
                //System.out.println(density);

                if(density.contains("по")){
                    uplpDoc.setBuildingDensity("по существующему положению");
                    return;
                }
                if(density.contains("действие")){
                    uplpDoc.setBuildingDensity("Действие градостроительного регламента не распространяется");
                    return;
                }
                if(density.contains("без")){
                    uplpDoc.setBuildingDensity("без ограничений");
                    return;
                }
                if(density.contains("не")){
                    uplpDoc.setBuildingDensity("не установлен");
                    return;
                }
                uplpDoc.setBuildingDensity(density.strip());
            }
        }

    }

    private void parseBuiltUpAreaPercentage(String[] words, UplpDoc uplpDoc) {
        String builtUpAreaPercentage;

        for (String word : words){
            if(word.matches("\\(%\\) - .+")){
                //System.out.println(word);
                builtUpAreaPercentage = getAreaPercentage(word.split(" - ")[1]);
                uplpDoc.setBuiltUpAreaPercentage(builtUpAreaPercentage);
            }
        }

    }

    private void parseAreaSquare(UplpDoc uplpDoc, String areaSquare) {
        String metrics;

        metrics = areaSquare.split(" ")[1];

        metrics = metrics.substring(0, metrics.length() - 2);

        //System.out.println(metrics);

        areaSquare = areaSquare.split(" ")[0].replace('(', ' ').strip();

        float area = 0f;

        switch (metrics.toLowerCase()) {
            case "га": {
                area = Float.parseFloat(areaSquare.replace(',', '.')) * 10000;
                uplpDoc.setSubzonesArea((int) area);
                break;
            }
            case "кв.м": {
                uplpDoc.setSubzonesArea(Integer.parseInt(areaSquare));
                break;
            }
        }

        //System.out.println("" + area);

        //System.out.println(areaSquare);
    }


    /**
     * Парсинг атрибута максимальная высота застройки
     * @param uplpDoc сущность документа для внесения атрибута
     * @param words текстовый массив строк
     */
    private void parseBuildingMaxHeight(UplpDoc uplpDoc, String[] words) {
        int maxHeight = 0;

        for(String word : words){
            if(word.matches("\\(м\\.\\) - \\d+")){
                maxHeight = Integer.parseInt(word.split(" - ")[1]);
                uplpDoc.setBuildingMaxHeight(BigDecimal.valueOf(maxHeight));
            }
        }
    }

    private String getAreaPercentage(String word) {
        if(word.contains("действие ")){
            return "Действие градостроительного регламента не распространяется";
        }
        if(word.contains("без ")){
            return "без ограничений";
        }
        if(word.contains("по ")){
            return "по существующему положению";
        }
        return word;
    }

}
