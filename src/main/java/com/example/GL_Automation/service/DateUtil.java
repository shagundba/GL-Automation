package com.example.GL_Automation.service;

import com.example.GL_Automation.entity.Student;
import com.example.GL_Automation.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Student> fetchDataForDate(LocalDate date) {
        LocalDate previousDate = date.minusDays(1);  // Get the previous day
        System.out.println("Fetching data for the previous date: " + previousDate);

        String sql = "SELECT * FROM students WHERE date = ?";
        return jdbcTemplate.query(sql, new Object[]{previousDate}, (rs, rowNum) -> {
            Student student = new Student();
            student.setId(rs.getLong("id"));
            student.setDate(rs.getDate("date").toLocalDate());
            student.setData(rs.getString("data"));
            return student;
        });
    }
}
