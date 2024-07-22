package com.GL._Automation_System.Controller;

import com.GL._Automation_System.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private Service service;

    @PostMapping("/startGLAutomation")
    public ResponseEntity<String> startGLAutomation() {
        service.extractDataGenerateExcel();
        return ResponseEntity.ok("GL Automation started successfully.");
    }
}