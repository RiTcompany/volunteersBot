package org.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static Date convertDate(String birthdayStr) {
        try {
            Date date = DATE_FORMAT.parse(birthdayStr);
            String dateStr = DATE_FORMAT.format(date);
            if (dateStr.equals(birthdayStr)) return date;
        } catch (ParseException ignored) {
        }

        return null;
    }

    public static String convertDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static int getYearCountByDate(Date date) {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Moscow");
        Calendar calDate = Calendar.getInstance(timeZone);
        calDate.setTime(date);
        Calendar calToday = Calendar.getInstance(timeZone);
        calToday.setTime(new Date());

        int yearsDifference = calToday.get(Calendar.YEAR) - calDate.get(Calendar.YEAR);
        if (!wasBirthdayThisYear(calDate, calToday)) {
            yearsDifference--;
        }

        return yearsDifference;
    }

    private static boolean wasBirthdayThisYear(Calendar birthday, Calendar today) {
        if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
            return false;
        }

        if (today.get(Calendar.MONTH) > birthday.get(Calendar.MONTH)) {
            return true;
        }

        return today.get(Calendar.DAY_OF_MONTH) >= birthday.get(Calendar.DAY_OF_MONTH);
    }
}
