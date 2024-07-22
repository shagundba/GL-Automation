package com.GL._Automation_System.Utility;

import com.GL._Automation_System.Entitty.GLAutoData;
//import com.GL._Automation_System.Repository.GLRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FetchDataQuery {
//    @Autowired
//    private GLRepo glRepo;
    @Autowired
    private GenerateExcelAndUpload generateExcelAndUpload;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void processWhenDayEquals4(Calendar calendar) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<String> lastFourDays = new ArrayList<>();
            for (int i = 3; i >= 1; i--) {
                calendar.set(Calendar.DAY_OF_MONTH, i);
                lastFourDays.add(dateFormat.format(calendar.getTime()));
            }
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastFourDays.add(dateFormat.format(calendar.getTime()));
            for (String date : lastFourDays) {
                processDataForDate(date);
            }
        } catch (Exception e) {
            System.out.println("Error in processWhenDayEquals4: " + e.getMessage());
        }
    }

    public void processWhenDayGreaterThan4(Calendar calendar) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String previousDate = dateFormat.format(calendar.getTime());
            processDataForDate(previousDate);
        } catch (Exception e) {
            System.out.println("Error in processWhenDayGreaterThan4: " + e.getMessage());
        }
    }

    public void processDataForDate(String date) {

        String sql = "SELECT * FROM import_data WHERE sanction_date = '" + Date.valueOf(date) + "'";

        List<GLAutoData> glAutoData = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(GLAutoData.class));

        generateExcelAndUpload.generateExcel(glAutoData);
        System.out.println("data: " + glAutoData);
    }
}
