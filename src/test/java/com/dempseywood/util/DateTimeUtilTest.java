package com.dempseywood.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeUtilTest {
    @Test
    public void getNZMidnight() throws Exception {
        Date input = new Date();
        Date result = DateTimeUtil.getInstance().getLastNZMidnight(input);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("NZ"));
        cal.setTime(result);
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(0, cal.get(Calendar.MILLISECOND));

    }

    @Test
    public void getTimeOneDayBefore() throws Exception {
        Date input = new Date();
        Date result = DateTimeUtil.getInstance().getTimeOneDayBefore(input);
        assertEquals(86400000,input.getTime() - result.getTime());
    }

    @Test
    public void convertToClientLocalDate() throws Exception {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

        System.out.println(DateTimeUtil.getInstance().convertToClientLocalDate(date));
    }

}