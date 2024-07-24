package com.example.GL_Automation.controller;

import com.example.GL_Automation.service.DateCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

@RestController
public class Controller {

    @Autowired
    private DateCheckService dateCheckService;

    private final RestTemplate restTemplate;

    public Controller() {
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/testing")
    public String testing(@RequestParam(required = false) String testDate) throws IOException {
        LocalDate date = testDate != null ? LocalDate.parse(testDate) : LocalDate.now();
        if (dateCheckService.dateCheck(date)) {
            dateCheckService.extractAndUploadData(date);
            return "Service is available";
        } else {
            return "Service is only available from date 4 to second last date of month and today is " + date.getDayOfMonth();
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void triggerEndpoints() {
        triggerEndpoint1();
    }

    private void triggerEndpoint1() {
        String url = "http://localhost:9056/testing";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
    }
}