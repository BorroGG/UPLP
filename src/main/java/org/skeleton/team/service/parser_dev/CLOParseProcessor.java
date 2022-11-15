package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLOParseProcessor {
    void parseCloAttributes(UplpDoc uplpDoc, String parsedText) {
        if (parsedText.contains("3.2. Объекты, включенные в единый государственный реестр объектов культурного наследия (памятников \r\nистории и культуры) народов Российской Федерации")) {
            String[] innerData = parsedText.split("3.2. Объекты, включенные в единый государственный реестр объектов культурного наследия \\(памятников \r\nистории и культуры\\) народов Российской Федерации");
            String afterMatches = innerData[1];
            if (afterMatches.toLowerCase().startsWith("\r\nне имеются") ||
                    afterMatches.toLowerCase().startsWith("\r\nинформация отсутствует")) {
                uplpDoc.setCloAvailability("Отсутствуют");
            } else {
                uplpDoc.setCloAvailability("Присутствуют");
            }
        } else {
            uplpDoc.setCloAvailability("Отсутствуют");
        }
        if (uplpDoc.getCloAvailability().equals("Отсутствуют")) {
            uplpDoc.setCboTotalCount("0");
            uplpDoc.setCloDescription("Нет");
            uplpDoc.setCloIdentificationNo("Нет");
            uplpDoc.setCloRegistrationNo("Нет");
        } else {
            String[] innerData = parsedText.split("3.2. Объекты, включенные в единый государственный реестр объектов культурного наследия \\(памятников \r\nистории и культуры\\) народов Российской Федерации");
            String afterMatches = innerData[1].split("4. Информация о расчетных показателях минимально допустимого уровня обеспеченности")[0];
            String[] numbers = afterMatches.split("на чертеже ГПЗУ");
            uplpDoc.setCboTotalCount(numbers.length - 1 + "");
            StringBuilder sb = new StringBuilder();
            for (String number : numbers) {
                number = number.replace("\r\n", " ");
                Pattern p60 = Pattern.compile(".*Наименование .{1,15}: (.*); .*");
                Matcher m60 = p60.matcher(number);
                if (m60.matches()) {
                    if (sb.toString().length() == 0) {
                        sb.append(m60.group(1).split(";")[0]);
                    } else {
                        sb.append(";").append(m60.group(1).split(";")[0]);
                    }
                }
            }
            uplpDoc.setCloDescription(sb.toString().equals("") ? "-" : sb.toString());
            sb = new StringBuilder();
            for (String number : numbers) {
                number = number.replace("\r\n", " ");
                Pattern p61 = Pattern.compile(".*Идентификационный .{1,20}: (.*); .*");
                Matcher m61 = p61.matcher(number);
                if (m61.matches()) {
                    if (sb.toString().length() == 0) {
                        sb.append(m61.group(1).split(";")[0]);
                    } else {
                        sb.append(";").append(m61.group(1).split(";")[0]);
                    }
                }
            }
            uplpDoc.setCloIdentificationNo(sb.toString().equals("") ? "-" : sb.toString());
            sb = new StringBuilder();
            for (String number : numbers) {
                number = number.replace("\r\n", " ");
                Pattern p62 = Pattern.compile(".*Регистрационный .{1,15}: (.*); .*");
                Matcher m62 = p62.matcher(number);
                if (m62.matches()) {
                    if (sb.toString().length() == 0) {
                        sb.append(m62.group(1).split(";")[0]);
                    } else {
                        sb.append(";").append(m62.group(1).split(";")[0]);
                    }
                }
            }
            uplpDoc.setCloRegistrationNo(sb.toString().equals("") ? "-" : sb.toString());
        }
    }
}
