package com.example.GL_Automation.service;

import com.example.GL_Automation.entity.Student;
import com.example.GL_Automation.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Component
public class DateUtil {

    @Autowired
    private StudentRepo studentRepo;

    public List<Student> fetchDataForDate(LocalDate date) {
        LocalDate previousDate = date.minusDays(1);  // Get the previous day
        System.out.println("Fetching data for the previous date: " + previousDate);
        return studentRepo.findByDate(previousDate); // Fetch data for the previous date
    }

}
