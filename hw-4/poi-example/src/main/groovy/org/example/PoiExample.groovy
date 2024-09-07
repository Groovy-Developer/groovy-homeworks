package org.example

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook

SXSSFWorkbook workbook = new SXSSFWorkbook()

def sheet = workbook.createSheet()

data = [
        [1, true, null, 'hello'],
]

CellStyle testStyle = workbook.createCellStyle()

def rowIdx = 0
data.forEach {
    def row = sheet.createRow(++rowIdx)
    def cellIdx = 0
    it.forEach { cellData ->
        Cell cell = row.createCell(++cellIdx)
        cell.setCellValue(cellData?.toString())
        cell.setCellStyle(testStyle)
    }
}

FileOutputStream fos = new FileOutputStream(new File("test.xlsx"))
try {
    workbook.write(fos);
    workbook.close();
} catch (Exception e) {
    throw new RuntimeException(e);
}
