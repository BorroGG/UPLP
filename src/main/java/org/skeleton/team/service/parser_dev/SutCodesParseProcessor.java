package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;

public class SutCodesParseProcessor {

    private static final String SUT_NOT_LIVING = "Не жилая";
    private static final String SUT_MIXED = "Смешанная";
    private static final String SUT_LIVING = "Жилая";

    /**
     * Парсинг атрибута "Наименование условной группы использования ЗУ по ВРИ"
     * Заполняет соответствующее поле в документе и возвращает набор кодов.
     * @param documentText "сырая" строка с текстом документа
     * @return атрибут "Коды основных видов разрешенного использования (ВРИ) земельного  участка (ЗУ)"
     */
    String parseSutCodes(String documentText) {
        String[] words;
        words = documentText.split(" ");

        String codeVRI = "";

        String codesVRI = "";

        for(String word : words){
            if(word.matches("\\d.\\d.\\d+,")
                    || word.matches("\\(\\d.\\d.\\d\\)")){
                codesVRI += (codesVRI.length() > 0 ? " " : "") + word.split(",")[0].strip() + ";";
            }
            if(word.matches("\\(\\d.\\d.\\d\\)\r\n.+|\\(\\d.\\d.\\d\\)\n.+")){
                codeVRI = word.split("\n")[0].trim().strip();
                codesVRI += (codesVRI.length() > 0 ? " " : "") +
                        codeVRI
                                .replace('(',' ')
                                .replace(')',' ')
                                .strip() + ";";
            }
        }

        if(codesVRI.equals("")){
            return "Действие градостроительного регламента не распространяется";
        }

        return codesVRI.substring(0, codesVRI.length()-1);
    }

    /**
     * Проверка типа помещения
     * @param codesVRI коды ВРИ
     * @return тип помещения
     */
    String checkLivingPlace(String codesVRI) {

        String sutStatus = "";

        if (codesVRI.startsWith("Действие ")){
            return SUT_NOT_LIVING;
        }

        for(String code : codesVRI.split("; |;")){
            if(sutStatus.equals(SUT_MIXED)){
                break;
            }
            if(code.matches("2\\.\\d|2.[0-7].\\d|13.2")){
                sutStatus = sutStatus.equals(SUT_NOT_LIVING) ? SUT_MIXED : SUT_LIVING;
            } else {
                sutStatus = sutStatus.equals(SUT_LIVING) ? SUT_MIXED : SUT_NOT_LIVING;
            }
        }
        return sutStatus;
    }

    void outputUplpSutData(UplpDoc uplpDoc) {
        System.out.println("==================== ВЫВОД ДАННЫХ ДОКУМЕНТА НА ЭТАПЕ III ==================");

        System.out.println(
                uplpDoc.getSutCodes() + "\n" +
                        uplpDoc.getSutGroupName() + "\n"
        );

        System.out.println("===========================================================================");
    }
}
