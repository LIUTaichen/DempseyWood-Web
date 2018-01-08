package com.dempseywood.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {
    private static DateTimeUtil ourInstance = new DateTimeUtil();

    public static DateTimeUtil getInstance() {
        return ourInstance;
    }

    private static final String CLIENT_TIME_ZONE ="NZ";

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

    public Date convertToClientLocalDate(Date time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();

        sdf.setTimeZone(TimeZone.getTimeZone(CLIENT_TIME_ZONE));
        String clientTime = sdf.format(time);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.parse(clientTime);
    }

    public Date getLastNZMidnight(Date serverLocalTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(serverLocalTime);
        cal.setTimeZone(TimeZone.getTimeZone("NZ"));
        cal.get(Calendar.HOUR_OF_DAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Date getTimeOneDayBefore(Date inputTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputTime);
        cal.add(Calendar.HOUR_OF_DAY, -24);
        return cal.getTime();
    }

    public Date getTimeOneDayLater(Date inputTime){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputTime);
        cal.add(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    public Date getNextNZMidNight(Date utcTime) {
        Date lastNzTimeMidnightInUTC = this.getLastNZMidnight(utcTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastNzTimeMidnightInUTC);
        cal.add(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }
}
