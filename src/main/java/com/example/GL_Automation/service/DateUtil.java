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
        List<Student> data = new ArrayList<>();

        if (date.getDayOfMonth() == 4) {
            List<LocalDate> dates = Arrays.asList(
                    date.minusMonths(1).withDayOfMonth(date.minusMonths(1).lengthOfMonth()), // Last day of last month
                    date.withDayOfMonth(1),   // First day of current month
                    date.withDayOfMonth(2),   // Second day of current month
                    date.withDayOfMonth(3)    // Third day of current month
            );

            for (LocalDate d : dates) {
                data.addAll(studentRepo.findByDate(d));
            }
        } else {
            LocalDate previousDate = date.minusDays(1);
            data.addAll(studentRepo.findByDate(previousDate));
        }

        return data;
    }

    // Helper method to manually set a specific date for testing
    public static LocalDate getDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }
}
