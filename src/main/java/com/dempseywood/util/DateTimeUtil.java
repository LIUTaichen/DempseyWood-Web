package com.dempseywood.util;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    private static DateTimeUtil ourInstance = new DateTimeUtil();

    public static DateTimeUtil getInstance() {
        return ourInstance;
    }

    private DateTimeUtil() {
    }

    public Date getTimeOfBeginningOfToday(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getTimeOfEndOfToday(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
