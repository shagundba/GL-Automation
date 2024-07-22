package com.example.GL_Automation.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.example.GL_Automation.entity.Student;
import com.example.GL_Automation.repository.StudentRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
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

    public boolean is() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return dayOfMonth >= 4 && dayOfMonth <= lastDayOfMonth;
    }

    public void extractAndUploadData() throws IOException {
        List<Student> data = dateUtil.fetchDataForPreviousDate();
        System.out.println("fetched data : "+data);
        ByteArrayInputStream excelFile = convertToExcel(data);
        uploadToAzureStorage(excelFile);
    }

    private ByteArrayInputStream convertToExcel(List<Student> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Data");

        for (Student entity : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entity.getId());
            row.createCell(1).setCellValue(entity.getDate().toString());
            row.createCell(2).setCellValue(entity.getData());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private void uploadToAzureStorage(ByteArrayInputStream excelFile) {
        LocalDate currentDate = LocalDate.now();
        String fileName = "Data_" + currentDate.toString() + ".xlsx";

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://" + accountName + ".blob.core.windows.net")
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();

        BlobClient blobClient = blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
        blobClient.upload(excelFile, excelFile.available(), true);
    }
}