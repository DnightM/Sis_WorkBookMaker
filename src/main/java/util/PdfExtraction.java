
package util;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfExtraction {
    public String extractingText(File targetFile, int[] pages) {
        PDDocument pdDocument = null;
        try {
            pdDocument = PDDocument.load(targetFile);
            PDFTextStripper str = null;
            str = new PDFTextStripper();
            str.setStartPage(pages[0]);
            str.setEndPage(pages[1]);
            return str.getText(pdDocument);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pdDocument != null) {
                try {
                    pdDocument.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }
}
