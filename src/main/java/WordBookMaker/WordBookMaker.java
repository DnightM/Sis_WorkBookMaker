package WordBookMaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import util.PdfExtraction;
import util.WriteExcel;
import vo.WordBookVo;

public class WordBookMaker {
    WriteExcel excel = new WriteExcel();
    PdfExtraction pdf = new PdfExtraction();

    public void run(File pdfFile) {
        ArrayList<WordBookVo> list = new ArrayList<>();

        // pdf 추출
        try {
            String[] t = pdf.extractingText(pdfFile).split("\n");
            int count = 0;
            for (String a : t) {
                if (a.contains(":")) {
                    a = a.trim();
                    String[] tt = a.split("[:]{1,}");
                    if (tt.length != 2) {
                        System.out.println("Skip Word : [" + a + "]");
                        continue;
                    }
                    String word = tt[0].trim();
                    String mean = tt[1].trim();
                    WordBookVo vo = new WordBookVo(word, mean);
                    list.add(vo);
                    count++;
                }
            }
            System.out.println("총 단어 개수 : " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 셔플
        Collections.shuffle(list);

        // excel로 write
        String filename = pdfFile.getName().substring(pdfFile.getName().lastIndexOf(".") + 1);
        File outputPath = pdfFile.getParentFile();
        File outputQuestion = new File(outputPath, filename + "_문제.xlsx");
        File outputAnswer = new File(outputPath, filename + "_정답.xlsx");
        try {
            excel.excelWrite(list, outputQuestion, outputAnswer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        File pdfFile = new File("./input/v101.pdf");

        WordBookMaker maker = new WordBookMaker();
        maker.run(pdfFile);
    }
}
