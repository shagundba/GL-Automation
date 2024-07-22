package com.GL._Automation_System.ServiceImpl;

import com.GL._Automation_System.Utility.FetchDataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class ServiceImpl implements com.GL._Automation_System.Service.Service {

    @Autowired
    private FetchDataQuery fetchDataQuery;
//                      S M H D MO W
    @Scheduled(cron = "10 * * * * *")
    public void extractDataGenerateExcel() {
        try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, 4);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = dateFormat.format(calendar.getTime());
                int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                if (currentDayOfMonth == 4) {
                    fetchDataQuery.processWhenDayEquals4(calendar);
                } else if (currentDayOfMonth > 4) {
                    fetchDataQuery.processWhenDayGreaterThan4(calendar);
                } else {
                    System.out.println("Invalid day of month: " + currentDate);
                }
            } catch (Exception e) {
                System.out.println("Error in monthly process: " + e.getMessage());
            }
    }
}