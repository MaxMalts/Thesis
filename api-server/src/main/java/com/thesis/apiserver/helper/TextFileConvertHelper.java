package com.thesis.apiserver.helper;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.thesis.apiserver.error.InternalException;
import com.thesis.apiserver.persistence.FileSystemAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class TextFileConvertHelper {

    private final FileSystemAdapter fileSystemAdapter;

    public void writePdf(String text, Path path) {
        var result = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, result).setPdfVersion(PdfWriter.PDF_VERSION_1_7);
        } catch (DocumentException e) {
            throw new InternalException("Error creating PdfWriter instance", e);
        }

        document.open();
        try {
            document.add(new Paragraph(text));
        } catch (DocumentException e) {
            throw new InternalException("Unable to add text chunk to pdf file", e);
        }
        document.close();

        fileSystemAdapter.save(result.toByteArray(), path);
    }

    public void writeDocx(String text, Path path) {
        throw new UnsupportedOperationException();
    }

    public void writeTxt(String text, Path path) {
        throw new UnsupportedOperationException();
    }
}
