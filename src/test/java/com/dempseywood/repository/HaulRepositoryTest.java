package com.dempseywood.repository;

import com.dempseywood.model.Haul;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HaulRepositoryTest {

    @Autowired
    private HaulRepository haulRepository;
    @Before
    public void setUp() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Haul haul = new Haul();
        haul.setImei("testimei");

        Date loadTime = formatter.parse("2017-10-05T15:23:01");
        Date unloadTime = formatter.parse("2017-10-05T15:50:01");

        haul.setLoadTime(loadTime);
        haul.setUnloadTime(unloadTime);
        haulRepository.save(haul);


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findByImeiAndTimestampAfterAndTimestampBeforeAllNullParams() throws Exception {
        //List<Haul> result = haulRepository.findByImeiAndLoadTimeAfterAndUnloadTimeBefore(null,null,null);
        //assertEquals("testimei",result.get(0).getImei() );
    }

    @Test
    public void findByImeiAndTimestampAfterAndTimestampBeforeImeialone() throws Exception {
        //List<Haul> result = haulRepository.findByImeiAndLoadTimeAfterAndUnloadTimeBefore("testimei",null,null);
        //assertEquals("testimei",result.get(0).getImei() );

        //result = haulRepository.findByImeiAndLoadTimeAfterAndUnloadTimeBefore("1", null, null);
        //assertEquals("0",result.size() );
    }





}