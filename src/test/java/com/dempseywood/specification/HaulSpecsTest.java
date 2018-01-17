package com.dempseywood.specification;

import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

import static org.junit.Assert.*;

public class HaulSpecsTest {
    @Test
    public void isBefore() throws Exception {
        Date date = new Date();
        Specification spec = HaulSpecs.isBefore(date);

    }

    @Test
    public void isAfter() throws Exception {
        Date date = new Date();
        Specification spec = HaulSpecs.isAfter(date);
    }

    @Test
    public void hasImei() throws Exception {
        String imei = "imei";
        Specification spec = HaulSpecs.hasImei(imei);
    }

}