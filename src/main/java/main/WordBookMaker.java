package main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.PdfExtraction;
import util.ReadXml;
import util.WriteExcel;
import util.LicensePeriod;
import vo.WordBookVo;

public class WordBookMaker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Scanner sc = new Scanner(System.in);
    private final WriteExcel excel = new WriteExcel();
    private final PdfExtraction pdf = new PdfExtraction();

    private int[] pages;
    private int limitWordCtn;
    private int cellWidth;
    private boolean isShuffle;

    public void run(File[] pdfFiles, File outputDir) {
        this.pages = getPages("\tInput number of pages to extract : ");
        this.limitWordCtn = getInteger("\tInput number of words to extract : ");
        this.cellWidth = getInteger("\tInput the horizontal number of cells in an Excel cell : ");
        this.isShuffle = getBoolean("\tDo you want shuffle[y/n]?");

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
        sc.close();
    }

    private void procPdfToExcel(File pdfFile, File outputDir) {
        ArrayList<WordBookVo> list = new ArrayList<>();

        // pdf 추출
        String pageInfo = null;
        int count = 0;
        try {
            String pdfTxt = pdf.extractingText(pdfFile, pages);
            if (pdfTxt == null) {
                logger.info("File | " + pdfFile + " | Can't work. Skip this file");
                return;
            }
            String[] pdfTxtLines = pdfTxt.split("\n");
            for (String line : pdfTxtLines) {
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
            pageInfo = pages[0] == pages[1] ? String.valueOf(pages[0]) : pages[0] + "-" + pages[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 셔플
        Collections.shuffle(list);

        // excel로 write
        String filename = pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf("."));
        File outputQuestion = new File(outputDir, filename + "_문제_Page" + pageInfo + "_" + limitWordCtn + "개" + (isShuffle ? "_shuffle_" : "") + ".xlsx");
        File outputAnswer = new File(outputDir, filename + "_정답_Page" + pageInfo + "_" + limitWordCtn + "개" + (isShuffle ? "_shuffle_" : "") + ".xlsx");
        try {
            int extractionCtn = excel.excelWrite(list, outputQuestion, outputAnswer, limitWordCtn, cellWidth);
            logger.info("[" + pdfFile.getName() + "] | Total Word Ctn : " + count + " | Select Word Ctn : " + extractionCtn + " | Page : " + pageInfo + " | Shuffle : " + (isShuffle ? "Yes" : "No"));
            logger.info("\t-> " + outputQuestion.getAbsolutePath());
            logger.info("\t-> " + outputAnswer.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean getBoolean(String msg) {
        System.out.print(msg);
        String input = sc.nextLine().trim().toLowerCase();
        try {
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            logger.info("Invalid answer[y/n]. Try again.");
            return getBoolean(msg);
        } catch (Exception e) {
            logger.info("Invalid answer[y/n]. Try again.");
            return getBoolean(msg);
        }
    }

    private int getInteger(String msg) {
        System.out.print(msg);
        String input = sc.nextLine();
        try {
            return Integer.parseInt(input.trim());
        } catch (Exception e) {
            logger.info("Invalid number. Try again.");
            return getInteger(msg);
        }
    }

    private int[] getPages(String msg) {
        System.out.print(msg);
        String pageString = sc.nextLine();
        int[] pages = pashingPages(pageString);
        if (pages[0] < 0) {
            logger.info("Invalid number. Try again.");
            return getPages(msg);
        }
        return pages;
    }

    private int[] pashingPages(String msg) {
        String[] temp = msg.split("-");
        if (temp.length != 2) {
            try {
                int p = Integer.parseInt(msg.trim());
                return new int[]{p, p};
            } catch (Exception e) {
                return new int[]{-1};
            }
        }

        int[] result = new int[2];
        for (int i = 0; i < 2; i++) {
            try {
                result[i] = Integer.parseInt(temp[i].trim());
            } catch (Exception e) {
                return new int[]{-1};
            }
        }
        return result;
    }

    public static void main(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("args[0]=config.xml file path");
        }
        System.out.println("START Process");
        ReadXml xml = new ReadXml(new File(args[0]));
        File[] inputFiles = xml.getInputFiles();
        File outputDir = xml.getOutputDirFile();
        if (inputFiles == null || outputDir == null) {
            System.out.println("Check input files and output directory");
            return;
        }

        WordBookMaker maker = new WordBookMaker();
        maker.run(inputFiles, outputDir);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static {
        // 빌드 날짜 기준 다음달 말일까지
        LocalDate ld = LocalDate.now();
        LicensePeriod lp = new LicensePeriod();
        try {
            if (!lp.limit(ld.getYear(), ld.getMonthValue() + 2, 1)) {
                System.out.println("License period has expired");
                System.exit(0);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            System.exit(0);
        }
    }

}
