package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import vo.WordBookVo;

public class WriteExcel {
    private static final int cellSize = 3500;

    public int excelWrite(ArrayList<WordBookVo> list, File outputQuestion, File outputAnswer, int limitWordCtn, int cellWidth) throws IOException {
        // create workbook
        XSSFWorkbook[] workbook = new XSSFWorkbook[2];
        XSSFSheet[] sheet = new XSSFSheet[2];
        for (int i = 0; i < 2; i++) {
            workbook[i] = new XSSFWorkbook();
            sheet[i] = workbook[i].createSheet();
        }

        // set data
        int len = Math.min(limitWordCtn, list.size());
        for (int i = 0; i < 2; i++) {
            int r = -1;
            boolean isAnswer = i % 2 == 1;
            XSSFRow row = null;
            for (int j = 0; j < len; j++) {
                if (j % cellWidth == 0) {
                    row = sheet[i].createRow(++r);
                }
                XSSFCell cell = null;
                int questionIdx = j % cellWidth * 2;
                sheet[i].setColumnWidth(cellSize, cellWidth);
                cell = row.createCell(questionIdx);
                cell.setCellValue(list.get(j).getWord());
                if (isAnswer) {
                    int answerIdx = j % cellWidth * 2 + 1;
                    sheet[i].setColumnWidth(cellSize, cellWidth);
                    cell = row.createCell(answerIdx);
                    cell.setCellValue(list.get(j).getMean());
                }
            }
            // 한줄에 width개씩 write 함
        }

        // write
        FileOutputStream[] fos = new FileOutputStream[2];
        fos[0] = new FileOutputStream(outputQuestion);
        fos[1] = new FileOutputStream(outputAnswer);
        for (int i = 0; i < 2; i++) {
            workbook[i].write(fos[i]);
            workbook[i].close();
            fos[i].close();
        }
        return len;
    }
}
