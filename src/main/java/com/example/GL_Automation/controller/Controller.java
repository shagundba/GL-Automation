package com.example.GL_Automation.controller;

import com.example.GL_Automation.entity.Student;
import com.example.GL_Automation.service.DateCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private DateCheckService dateCheckService;
    private final RestTemplate restTemplate;

    public Controller() {
        this.restTemplate = new RestTemplate();
    }

    // Initialize Calendar
    Calendar calendar = Calendar.getInstance();
    int date1 = calendar.get(Calendar.DAY_OF_MONTH);

    @GetMapping("/testing")
    public String testing() throws IOException {
        if (dateCheckService.is()) {
            dateCheckService.extractAndUploadData();

            return "service is available";
        } else {
            return "Service is only available from date 4 to second last date of month and today is " + date1;
        }
    }

    @Scheduled(cron = "0 17 12 22 * ?")
    public void triggerEndpoints() {
        triggerEndpoint1();
    }

    private void triggerEndpoint1() {
        String url = "http://localhost:9056/testing";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
    }
}