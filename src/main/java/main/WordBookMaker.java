package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.PdfExtraction;
import util.ReadXml;
import util.WriteExcel;
import vo.WordBookVo;

public class WordBookMaker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private WriteExcel excel = new WriteExcel();
    private PdfExtraction pdf = new PdfExtraction();

    public void run(File[] pdfFiles, File outputDir) {
        for (File pdfFile : pdfFiles) {
            logger.info("=================================");
            if (!pdfFile.isFile()) {
                logger.error("Error | This is not file. | " + pdfFile);
                continue;
            }
            if (!pdfFile.getName().substring(pdfFile.getName().lastIndexOf(".") + 1).equals("pdf")) {
                logger.error("Error | This is not pdf file. | " + pdfFile);
                continue;
            }
            procPdfToExcel(pdfFile, outputDir);
        }
    }

    private void procPdfToExcel(File pdfFile, File outputDir) {
        ArrayList<WordBookVo> list = new ArrayList<>();

        // pdf 추출
        try {
            String[] pdfTxt = pdf.extractingText(pdfFile).split("\n");
            int count = 0;
            for (String line : pdfTxt) {
                if (line.contains(":")) {
                    line = line.trim();
                    String[] tt = line.split("[:]{1,}");
                    if (tt.length != 2) {
                        logger.info("Skip Word : [" + line + "]");
                        continue;
                    }
                    String word = tt[0].trim();
                    String mean = tt[1].trim();
                    WordBookVo vo = new WordBookVo(word, mean);
                    list.add(vo);
                    count++;
                }
            }
            logger.info("File | " + pdfFile + " | Total Word Ctn : " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 셔플
        Collections.shuffle(list);

        // excel로 write
        String filename = pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf("."));
        File outputQuestion = new File(outputDir, filename + "_문제.xlsx");
        File outputAnswer = new File(outputDir, filename + "_정답.xlsx");
        try {
            excel.excelWrite(list, outputQuestion, outputAnswer);
            logger.info(pdfFile.getAbsolutePath());
            logger.info("\t-> " + outputQuestion);
            logger.info("\t-> " + outputQuestion);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //                args = new String[1];
        //                args[0] = WordBookMaker.class.getClassLoader().getResource("WordBookMaker_Config.xml").getPath();
        if (args.length != 1) {
            System.out.println("args[0] = xmlFilePath");
        } else {
            ReadXml xml = new ReadXml(new File(args[0]));

            File[] inputFiles = xml.getInputFiles();
            File outputDir = xml.getOutputDirFile();

            WordBookMaker maker = new WordBookMaker();
            maker.run(inputFiles, outputDir);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
