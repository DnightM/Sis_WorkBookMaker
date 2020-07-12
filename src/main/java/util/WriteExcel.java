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
    public void excelWrite(ArrayList<WordBookVo> list, File outputQuestion, File outputAnswer) throws IOException {
        // create workbook
        XSSFWorkbook[] workbook = new XSSFWorkbook[2];
        XSSFSheet[] sheet = new XSSFSheet[2];
        for (int i = 0; i < 2; i++) {
            workbook[i] = new XSSFWorkbook();
            sheet[i] = workbook[i].createSheet();
        }

        // set data
        int r = 0;
        for (int j = 0; j < list.size(); j++) {
            int t = 0;
            for (int i = 0; i < 2; i++) {
                int tj = j;
                XSSFRow row = sheet[i].createRow(r);
                XSSFCell cell = null;
                for (int k = 0; k < 5; k += 2) {
                    cell = row.createCell(k);
                    cell.setCellValue(list.get(tj).getWord());
                    if (i % 2 == 1) {
                        cell = row.createCell(k + 1);
                        cell.setCellValue(list.get(tj).getMean());
                    }
                    tj++;
                    t++;
                }
            }
            j += t / 2;
            r++;
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
    }

    public void excelWrite(ArrayList<WordBookVo> list, File outputQuestion) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        int r = 0;
        for (int i = 0; i < list.size(); i++) {
            XSSFRow row = sheet.createRow(r++);
            for (int j = 0; j < 5; j += 2) {
                XSSFCell cell1 = row.createCell(j);
                cell1.setCellValue(list.get(i++).getWord());
            }
        }
        FileOutputStream fos = new FileOutputStream(outputQuestion);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }
}
