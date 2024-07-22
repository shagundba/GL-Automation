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

    public List<Student> fetchDataForPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        List<Student> data = new ArrayList<>();

        if (dayOfMonth == 4) {
            Calendar lastMonth = (Calendar) calendar.clone();
            lastMonth.add(Calendar.MONTH, -1);
            int lastMonthLength = lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

            List<Calendar> dates = Arrays.asList(
                    getLastDayOfLastMonth(lastMonth, lastMonthLength),
                    getFirstDayOfCurrentMonth(),
                    getSecondDayOfCurrentMonth(),
                    getThirdDayOfCurrentMonth()
            );

            for (Calendar date : dates) {
                data.addAll(studentRepo.findByDate(convertCalendarToLocalDate(date)));
            }
        } else {
            Calendar previousDate = getPreviousDate();
            data.addAll(studentRepo.findByDate(convertCalendarToLocalDate(previousDate)));
        }
        return data;
    }

    private Calendar getLastDayOfLastMonth(Calendar lastMonth, int lastMonthLength) {
        lastMonth.set(Calendar.DAY_OF_MONTH, lastMonthLength);
        return lastMonth;
    }

    private Calendar getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar;
    }

    private Calendar getSecondDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        return calendar;
    }

    private Calendar getThirdDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        return calendar;
    }

    private Calendar getPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar;
    }

    private LocalDate convertCalendarToLocalDate(Calendar calendar) {
        return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}
