package com.pks.RESANALYZER.service;

import com.pks.RESANALYZER.entity.Resume;
import com.pks.RESANALYZER.entity.User;
import com.pks.RESANALYZER.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    public Resume uploadResume(User user, MultipartFile file) throws IOException {
        String extractedText = extractTextFromFile(file);

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);

        return resumeRepository.save(resume);
    }

    private String extractTextFromFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return "";
        }

        // Handle PDF files (using the stable 2.0.29 API)
        if (fileName.toLowerCase().endsWith(".pdf")) {
            try (InputStream is = file.getInputStream()) {
                PDDocument document = PDDocument.load(is); // This will now work
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                document.close();
                return text;
            }
        }

        // Handle DOCX files
        if (fileName.toLowerCase().endsWith(".docx")) {
            try (InputStream is = file.getInputStream()) {
                XWPFDocument document = new XWPFDocument(is);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                String text = extractor.getText();
                extractor.close();
                return text;
            }
        }

        // Return empty string for unsupported file types
        return "";
    }
}