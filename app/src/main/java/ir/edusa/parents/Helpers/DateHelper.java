package ir.edusa.parents.Helpers;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static String formatDate(Date date, String delimiter) {
        PersianCalendar calendar = new PersianCalendar(date.getTime());
        return calendar.getPersianYear() + delimiter + increaseToTwoDigit((calendar.getPersianMonth() + 1) + "") + delimiter + increaseToTwoDigit(calendar.getPersianDay() + "");
    }

    public static String formatTime(Date date, boolean hasSeocnds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String output = increaseToTwoDigit(calendar.get(Calendar.HOUR_OF_DAY) + "") + "" + increaseToTwoDigit(calendar.get(Calendar.MINUTE) + "");
        if (hasSeocnds) {
            return output + increaseToTwoDigit(calendar.get(Calendar.SECOND) + "");
        }
        return output;
    }

    public static String formatTime(int hour, int minute, String delimiter) {
        String output = increaseToTwoDigit(hour + "") + delimiter + increaseToTwoDigit(minute + "");
        return output;
    }

    public static String formatFullDateTime(Date date) {
        PersianCalendar persianCalendar = new PersianCalendar(date.getTime());
        return persianCalendar.getPersianShortDateTime();
    }

    public static PersianCalendar parseDate(String date) {
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.parse(date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8));
        return persianCalendar;
    }

    public static String increaseToTwoDigit(String input) {
        if (input.length() < 2) {
            input = "0" + input;
        }
        return input;
    }
}
