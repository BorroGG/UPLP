package org.skeleton.team.service.parser_dev;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;

public class CBOParseProcessor {

    String getExistingCboAvailability(String[] words) {
        if (String.join(" ", words).contains("Объекты капитального строительства отсутсвуют")) {
            return "Отсутствуют";
        }
        if (String.join(" ", words)
                .contains("В границах земельного участка расположены объекты капитального строительства")) {
            return "Присутствуют";
        }
        return "-";
    }

    String getExistingCboTotalCount(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")) {
            return "0";
        }
        Pattern p = Pattern.compile("В границах земельного участка расположены объекты капитального строительства\\. Количество {0,2}объектов (\\d+) единиц");
        Matcher m = p.matcher(String.join(" ", words));
        if (m.find()) {
            return m.group(1);
        }
        return "0";
    }

    String getExistingCboPurpose(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")) {
            return "Нет";
        }
        String join = String.join(" ", words);
        int count = StringUtils.countMatches(join.substring(
                join.lastIndexOf("3.1. Объекты капитального строительства"),
                join.lastIndexOf("3.2. Объекты, включенные в")), "на чертеже ГПЗУ");
        if (String.join(" ", words).contains("Нежилое")
                && StringUtils.countMatches(String.join(" ", words), "Нежилое") == count) {
            return "Нежилое";
        }
        if (String.join(" ", words).contains("Нежилое")
                && StringUtils.countMatches(String.join(" ", words), "Нежилое") != count) {
            return "Смешанное";
        }
        return "Жилое";
    }

    String getExistingCboDescription(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")) {
            return "Нет";
        }
        Pattern p = Pattern.compile(" на чертеже ГПЗУ (.{1,30}) Адрес:");
        Matcher m = p.matcher(String.join(" ", words));
        List<String> names = new ArrayList<>();
        if (m.find()) {
            names.add(m.group(1).trim());
        }
        return names.isEmpty() ? "-" : String.join(", ", names);
    }

    int getExistingCboMaxFloorCount(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")) {
            return 0;
        }
        Pattern p = Pattern.compile("Количество этажей: (\\d+)");
        Matcher m = p.matcher(String.join(" ", words));
        int max = 0;
        if (m.find()) {
            int flours = Integer.parseInt(m.group(1));
            if (flours > max) {
                max = flours;
            }
        }
        return max;
    }

    BigDecimal getExistingCboTotalArea(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")) {
            return BigDecimal.ZERO;
        }
        Pattern p = Pattern.compile("Площадь: (\\d+,?.?\\d*)");
        Matcher m = p.matcher(String.join(" ", words));
        List<Double> squares = new ArrayList<>();
        while (m.find()) {
            squares.add(Double.parseDouble(m.group(1)));
        }
        return BigDecimal.valueOf(DoubleStream.of(squares.stream()
                        .mapToDouble(d -> d)
                        .toArray())
                .average()
                .orElse(0));
    }

    BigDecimal getExistingCboResidentialObjectsArea(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")
                || getExistingCboPurpose(words).equals("Нежилое")) {
            return BigDecimal.ZERO;
        }
        Pattern p = Pattern.compile("Назначение: .{1,100}; Площадь: (\\d+,?.?\\d*)");
        Matcher m = p.matcher(String.join(" ", words));
        List<Double> squares = new ArrayList<>();
        while (m.find()) {
            if (m.group(0).contains("Нежилое")) {
                continue;
            }
            squares.add(Double.parseDouble(m.group(1)));
        }
        return BigDecimal.valueOf(DoubleStream.of(squares.stream()
                        .mapToDouble(d -> d)
                        .toArray())
                .average()
                .orElse(0));
    }

    BigDecimal getExistingCboNonResidentialObjectsArea(String[] words) {
        if (Objects.equals(getExistingCboAvailability(words), "Отсутствуют")
                || getExistingCboPurpose(words).equals("Жилое")) {
            return BigDecimal.ZERO;
        }
        Pattern p = Pattern.compile("Назначение: .{1,100}; Площадь: (\\d+,?.?\\d*)");
        Matcher m = p.matcher(String.join(" ", words));
        List<Double> squares = new ArrayList<>();
        while (m.find()) {
            if (m.group(0).contains("Нежилое")) {
                squares.add(Double.parseDouble(m.group(1)));
            }
        }
        return BigDecimal.valueOf(DoubleStream.of(squares.stream()
                        .mapToDouble(d -> d)
                        .toArray())
                .average()
                .orElse(0));
    }
}
