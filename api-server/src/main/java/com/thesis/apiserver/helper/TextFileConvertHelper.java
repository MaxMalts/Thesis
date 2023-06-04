package com.thesis.apiserver.helper;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.thesis.apiserver.error.InternalException;
import com.thesis.apiserver.persistence.FileSystemAdapter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class TextFileConvertHelper {

    private final int FONT_SIZE = 14;

    private final FileSystemAdapter fileSystemAdapter;

    public void writePdf(String text, Path path) {
        var result = new ByteArrayOutputStream();
        var document = new Document();
        try {
            PdfWriter.getInstance(document, result).setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        } catch (DocumentException e) {
            throw new InternalException("Error creating PdfWriter instance", e);
        }

        document.open();
        var font = new Font();
        font.setSize(FONT_SIZE);
        try {
            document.add(new Paragraph(text, font));
        } catch (DocumentException e) {
            throw new InternalException("Unable to add text chunk to pdf file", e);
        }
        document.close();

        fileSystemAdapter.save(result.toByteArray(), path);
    }

    public void writeDocx(String text, Path path) {
        var result = new ByteArrayOutputStream();
        var document = new XWPFDocument();

        var documentRun = document.createParagraph().createRun();
        documentRun.setText(text);
        documentRun.setFontSize(FONT_SIZE);

        try {
            document.write(result);
        } catch (IOException e) {
            throw new InternalException("Error writing docx to byte array", e);
        }
        try {
            document.close();
        } catch (IOException e) {
            throw new InternalException("Error while closing the docx document", e);
        }

        fileSystemAdapter.save(result.toByteArray(), path);
    }

    public void writeTxt(String text, Path path) {
        throw new UnsupportedOperationException();
    }
}
