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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UplpDocServiceImpl implements UplpDocService {

    private final UplpDocRepository uplpDocRepository;

    @Override
    public UplpDoc getUplpDocById(Integer id) {
        return uplpDocRepository.findById(id).orElse(null);
    }

    @Override
    public UplpDoc deleteUplpDocById(Integer id) {
        return null;
    }

    @Override
    public UplpDoc createUplpDoc(List<MultipartFile> multipartFiles, String docType) {
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                File file = File.createTempFile("pre", "su");
                multipartFile.transferTo(file);
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
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public UplpDoc updateUplpDoc(MultipartFile file, Integer id) {
        return null;
    }
}
