package com.GL._Automation_System.Utility;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class UploadExcelInAzure {
    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public void uploadFile(String fileName, byte[] data) {

        String connectionString = String.format("DefaultEndpointsProtocol=https;AccountName=%s;AccountKey=%s;EndpointSuffix=core.windows.net",
                accountName.trim(), accountKey.trim());
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName.trim());
        BlobClient blobClient = containerClient.getBlobClient(fileName+" blobData");
        try (InputStream dataStream = new ByteArrayInputStream(data)) {
            blobClient.upload(dataStream, data.length, true);
            System.out.println(true);
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(false);
        }
    }
}
