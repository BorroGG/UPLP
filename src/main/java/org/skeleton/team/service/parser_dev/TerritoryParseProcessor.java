package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.regex.*;

public class TerritoryParseProcessor {

    /**
     * Парсинг атрибута "Строительный адрес"
     * @param words массив строк документа
     * @return строительный адрес
     */
    String parseBuildingAddress(String[] words) {
        String line = "";

        for(int i = 0; i < words.length/2; i++){
            if(words[i].startsWith("г. Москва")){
                int j = i;
                while (!words[j].startsWith("Описание границ")){
                    line += words[j];
                    j++;
                }
            }
        }

        String[] lines = line.split(", ");

        Stream<String> sLines = Arrays.stream(lines).dropWhile(this::checkRegion);

        return sLines.collect(Collectors.joining(", "));
    }

    /**
     * Парсинг атрибута "Кадастровый номер земельного участка (ЗУ) или условный номер"
     * @param docByLines строковый массив документа
     */
    String parseCadastralNumber(String[] docByLines) {
        for(String line : docByLines){//77:06:0003002:70 //02/01/10179
            if(line.matches("\\d{2}:\\d{2}:\\d{7}:\\d+")
                    || line.matches("\\d{2}/\\d{2}/\\d{5}")
                    || line.matches("участок №\\d+|участок \\d+")){
                return line;
            }
        }
        return "Не определено";
    }

    /**
     * Парсинг атрибута "Наличие проекта планировки территории (ППТ) в границах ГПЗУ реквизиты документа"
     *
     * @param docByLines текстовый массив документа
     * @return строка наличия проекта планировки
     */
    String parseTlpAvailability(String[] docByLines) {
        String availability = "";

        for(String line : docByLines){
            if(line.startsWith("Проект планировки территории")){
                availability = line.split("Проект планировки территории ")[1];
                break;
            }

            if(line.startsWith("Проект планировки ")){
                availability = line.split("Проект планировки ")[1];
                break;
            }
        }

        availability = (availability.split("\\. |, ").length > 0 ? availability.split("\\. |, ")[0] : availability);

        return availability;
    }

    String parseTlpDocumentDetails(String[] docByLines) {
        String details = "";
        Pattern pattern = Pattern.compile("№\\W{0,3}[\\w-]+\\W{0,3}от\\W{0,3}\\d{2}.\\d{2}.\\d{4}");
        Matcher matcher;
        Boolean foundProject = false;
        String lastLine = "";
        Boolean lastLineFound = false;

        for(String line : docByLines){
            if(line.startsWith(" - Проект планировки территории")) {
                foundProject = true;
            }
            if(foundProject) {
                matcher = pattern.matcher(line);
                if(matcher.find()) {
                    if(!details.equals("")) {
                        details += "; ";
                    }
                    details += line.substring(matcher.start(), matcher.end());
                    lastLineFound = true;
                } else if (!lastLine.equals("")) {
                    matcher = pattern.matcher(lastLine+line);
                    if(matcher.find() & !lastLineFound) {
                        if (!details.equals("")) {
                            details += "; ";
                        }
                        details += (lastLine+line).substring(matcher.start(), matcher.end());
                        lastLineFound = true;
                    } else {
                        lastLineFound = false;
                    }
                }
                lastLine = line;
            }
            if (line.startsWith("Градостроительный план подготовлен") | (line.contains("Проект межевания территории") & foundProject)) {
                break;
            }
        }

        return details;
    }
    String parseSurveyingAvailability(String[] docByLines) {
        String availability = "";

        for(String line : docByLines){
            if(line.contains("Проект межевания территории")) {
                if (line.contains("не утвержден.")) {
                    availability = "не утвержден";
                } else {
                    availability = "утвержден";
                }
                break;
            }
            }

        return availability;
    }

    String parseSurveyingProjectDetails(String[] docByLines) {
        String details = "";
        Pattern pattern = Pattern.compile("№\\W{0,3}\\d+\\W{0,3}от\\W{0,3}\\d{2}.\\d{2}.\\d{4}");
        Matcher matcher;
        Boolean foundProject = false;
        String lastLine = "";
        Boolean lastLineFound = false;

        for(String line : docByLines){
            if(line.startsWith(" - Проект межевания территории")) {
                foundProject = true;
            }
            if(foundProject) {
                matcher = pattern.matcher(line);
                if(matcher.find()) {
                    if(!details.equals("")) {
                        details += "; ";
                    }
                    details += line.substring(matcher.start(), matcher.end());
                    lastLineFound = true;
                } else if (!lastLine.equals("")) {
                    matcher = pattern.matcher(lastLine+line);
                    if(matcher.find() & !lastLineFound) {
                        if (!details.equals("")) {
                            details += "; ";
                        }
                        details += (lastLine+line).substring(matcher.start(), matcher.end());
                        lastLineFound = true;
                    } else {
                        lastLineFound = false;
                    }
                }
                lastLine = line;
            }
            if (line.startsWith("Градостроительный план подготовлен")) {
                break;
            }
        }
        return details;
    }

    private boolean checkRegion(String s){
        return !(s.startsWith("ул.")|| s.endsWith(" ул."))
                && !s.startsWith("проезд")
                && !s.startsWith("посёлок")
                && !s.startsWith("пос.")
                && !s.startsWith("шоссе")
                && !s.startsWith("проспект")
                && !s.startsWith("наб.");
    }

    /**
     * Парсинг атрибута "Площадь земельного  участка (ЗУ), кв.м"
     * @param docByLines текстовый массив документа
     */
    Integer parsePlotArea(String[] docByLines) {
        for(String line : docByLines){
            if(line.matches("\\d+ ± \\d.+|\\d+. кв.м")){
                String plotArea = line.split(" ")[0];
                return Integer.parseInt(plotArea);
            }
        }
        return null;
    }

    void outputUplpTerritoryData(UplpDoc uplpDoc) {
        System.out.println("==================== ВЫВОД ДАННЫХ ДОКУМЕНТА НА ЭТАПЕ II ===================");

        System.out.println(
                uplpDoc.getAdministrativeArea() + "\n" +
                        uplpDoc.getDistrict() + "\n" +
                        uplpDoc.getBuildingAddress() + "\n" +
                        uplpDoc.getCadastralNo() + "\n" +
                        uplpDoc.getTlpProjectAvailability() + "\n" +
                        uplpDoc.getTlpDocumentDetails() + "\n" +
                        uplpDoc.getSurveyingProjectAvailability() + "\n" +
                        uplpDoc.getSurveyingProjectDetails()
        );

        System.out.println("===========================================================================");
    }

}
