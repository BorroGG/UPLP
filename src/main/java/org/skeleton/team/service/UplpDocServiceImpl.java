package org.skeleton.team.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.skeleton.team.entity.UplpDoc;
import org.skeleton.team.repository.UplpDocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UplpDocServiceImpl implements UplpDocService {

    private final UplpDocRepository uplpDocRepository;

    @Override
    @Transactional(readOnly = true)
    public UplpDoc getUplpDocById(Integer id) {
        return uplpDocRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public UplpDoc deleteUplpDocById(Integer id) {
        UplpDoc uplpDoc = uplpDocRepository.findById(id).orElse(null);
        if (uplpDoc != null) {
            uplpDocRepository.delete(uplpDoc);
            return uplpDoc;
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, UplpDoc> createUplpDocs(List<MultipartFile> multipartFiles, String docType) {
        Map<String, UplpDoc> result = new HashMap<>();
        for (int i = 0; i < multipartFiles.size(); i++) {
            try {
                File file = File.createTempFile("pre", "su");
                multipartFiles.get(i).transferTo(file);
                String parsedText;
                PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
                parser.parse();
                COSDocument cosDoc = parser.getDocument();
                PDFTextStripper pdfStripper = new PDFTextStripper();
                PDDocument pdDoc = new PDDocument(cosDoc);
                parsedText = pdfStripper.getText(pdDoc);
//                PrintWriter pw = new PrintWriter("C:\\Users\\Евгений\\Desktop\\ЛиДеР\\pdf.txt");
//                pw.print(parsedText);
//                pw.close();
                String[] words = parsedText.split(" ");
                UplpDoc uplpDoc = new UplpDoc();
                StringBuilder parseErrors = new StringBuilder();
                for (String word : words) {
                    //TODO парсим
                }
                uplpDocRepository.save(uplpDoc);
                //TODO сохраение ссылки на исходный файл образ ГПЗУ Причем непосредственно
                //TODO хранение должно осуществляться на сетевом диске пользователя/организации.
                if (parseErrors.length() == 0) {
                    result.put(String.format("Документ №%s, обработан успешно ошибок нет", i + 1), uplpDoc);
                } else {
                    result.put(String.format("Документ №%s, обработан с ошибками:%s", i + 1, parseErrors), uplpDoc);
                }
            } catch (Exception e) {
                result.put(String.format("Документ №%s, не обработан, критическая ошибка:%s", i + 1, e), null);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public UplpDoc updateUplpDoc(MultipartFile file, Integer id) {
        return null;
    }
}
