package org.skeleton.team.service.parser_dev;

import org.skeleton.team.entity.UplpDoc;

import java.io.File;
import java.io.IOException;

/***
 * Класс с точкой входа для тестирования парсера
 */
public class TestingInstance {

    private static UPLPParser parser;

    public static void main(String[] args) throws IOException {

        File file = new File("RU77106000-043720-GPZU.pdf");

        parser = new UPLPParser();

        UplpDoc doc = parser.parsingUPLPFile(file);

        /* Метод парсит все ГПЗУ файлы в пределах корневого каталога проекта */
        //parseAll();


    }

    private static void parseAll() throws IOException {
        File file = new File(".");

        for(String uplp : file.list()){
            if(uplp.endsWith(".pdf")) {
                File uplpFile = new File(uplp);
                parser.parsingUPLPFile(uplpFile);
            }
        }
    }
}
