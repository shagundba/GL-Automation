package com.example.GL_Automation.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.example.GL_Automation.entity.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Service
public class DateCheckService {

    @Autowired
    private DateUtil dateUtil;

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String accountName;
    @Value("${spring.cloud.azure.storage.blob.account-key}")
    private String accountKey;
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    /**
     * Checks if the service is available based on the given date.
     * @param date the date to check
     * @return true if the service is available, false otherwise
     */
    public boolean dateCheck(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();
        int lastDayOfMonth = date.lengthOfMonth();
        return dayOfMonth >= 4 && dayOfMonth <= lastDayOfMonth ;
    }

    /**
     * Extracts and uploads data based on the given date.
     * @param date the date for which to extract and upload data
     * @throws IOException if an I/O error occurs
     */
    public void extractAndUploadData(LocalDate date) throws IOException {
        if (date.getDayOfMonth() == 4) {
            handleSpecialCase(date);
        } else {
            List<Student> data = dateUtil.fetchDataForDate(date);
            System.out.println("Fetched data: " + data);
            ByteArrayInputStream excelFile = convertToExcel(data);
            uploadToAzureStorage(excelFile, date);
        }
    }

    /**
     * Handles the special case for the 4th of the month by generating separate
     * Excel files for the 3rd, 2nd, 1st, and the last day of the previous month.
     * @param currentDate the current date
     * @throws IOException if an I/O error occurs
     */
    private void handleSpecialCase(LocalDate currentDate) throws IOException {
        LocalDate previousDate = currentDate.minusDays(1);
        List<LocalDate> datesToProcess = Arrays.asList(
                previousDate.withDayOfMonth(1).minusDays(1), // Last day of previous month
                previousDate.withDayOfMonth(1),  // First day of the current month
                previousDate.withDayOfMonth(2),  // Second day of the current month
                previousDate.withDayOfMonth(3)   // Third day of the current month

        );
        for (LocalDate date : datesToProcess) {
            List<Student> data = dateUtil.fetchDataForDate(date);
            System.out.println("Fetched data for date " + date + ": " + data);
            ByteArrayInputStream excelFile = convertToExcel(data);
            uploadToAzureStorage(excelFile, date);
        }
    }

    /**
     * Converts a list of student data to an Excel file.
     * @param data the list of student data
     * @return a ByteArrayInputStream representing the Excel file
     * @throws IOException if an I/O error occurs
     */
    private ByteArrayInputStream convertToExcel(List<Student> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Data");

        // Populate data rows
        int rowNum = 1;
        for (Student student : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getDate().toString());
            row.createCell(2).setCellValue(student.getData());
        }

        // Write to ByteArrayOutputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
     * Uploads the provided Excel file to Azure Blob Storage.
     * @param excelFile the Excel file to upload
     * @param date the date associated with the data
     */
    private void uploadToAzureStorage(ByteArrayInputStream excelFile, LocalDate date) {
        String fileName = "Data_" + date.toString() + ".xlsx";
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();

        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
        blobClient.upload(excelFile, excelFile.available(), true);
    }
}