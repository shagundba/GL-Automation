package com.GL._Automation_System.Utility;


import com.GL._Automation_System.Entitty.GLAutoData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class GenerateExcelAndUpload {
    @Autowired
    private UploadExcelInAzure uploadExcelInAzure;

    public void generateExcel(List<GLAutoData> glAutoData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("MIS_Report");
        int rowCount = 0;

        String[] header = {"ApplicationNumber", "BranchName", "ApplicantName", "ChequeAmount", "ConsumerType", "HandoverDate", "LoanAmount", "UpdatedBy"};
        Row headerRow = sheet.createRow(rowCount++);
        int cellCount = 0;

        for (String headerValue : header) {
            headerRow.createCell(cellCount++).setCellValue(headerValue);
        }
        for (GLAutoData details : glAutoData) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(details.getChequeId() != null ? details.getChequeId() : "");
            row.createCell(1).setCellValue(details.getApplicantName() != null ? details.getApplicantName() : "");
            row.createCell(2).setCellValue(details.getSanction() != null ? details.getSanction().toString() : "");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            byte[] excelData = outputStream.toByteArray();

            uploadExcelInAzure.uploadFile(String.valueOf(LocalDate.now()), excelData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}