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

        File file = new File("RU77106000-043720-GPZU.pdf");

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
                return doc;
            }
        });

        List<UplpDoc> doc = parser.parsingUPLPFile(file, new StringBuilder());

        /* Метод парсит все ГПЗУ файлы в пределах корневого каталога проекта */
        //parseAll();


    }

    private static void parseAll() throws IOException {
        File file = new File(".");

        for(String uplp : file.list()){
            if(uplp.endsWith(".pdf")) {
                File uplpFile = new File(uplp);
                parser.parsingUPLPFile(uplpFile, new StringBuilder());
            }
        }
    }
}
