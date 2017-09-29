package com.dempseywood;

import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@RunWith(MockitoJUnitRunner.class)
public class TimeZoneTest {

    @Test
    public void timeZone(){
        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("NZ"));


        System.out.println(sdf.format(date));

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        System.out.println(cal.getTime());
        cal.setTimeZone(TimeZone.getTimeZone("NZ"));
        //cal.
        System.out.println(cal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        System.out.println(cal.getTime());
        cal.add(Calendar.HOUR_OF_DAY, -24);
        System.out.println(cal.getTime());
        System.out.println(DateUtil.getExcelDate(date));
    }
}
