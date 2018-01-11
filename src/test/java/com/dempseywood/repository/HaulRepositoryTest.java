package com.dempseywood.repository;

import com.dempseywood.model.Haul;
import com.dempseywood.specification.HaulSpecs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.data.jpa.domain.Specifications.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HaulRepositoryTest {

    @Autowired
    private HaulRepository haulRepository;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    @Before
    public void setUp() throws Exception {
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
    public void findAllNoParam() throws Exception {
        List<Haul> result = new ArrayList<>();
        haulRepository.findAll().forEach(haul -> result.add(haul));
        assertEquals("testimei",result.get(0).getImei() );

    }

    @Test
    public void findAllFilterByImei() throws Exception {
        List<Haul> emptyresult = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.hasImei("non exsisten imei"))).forEach(haul -> emptyresult.add((Haul)haul));
        //List<Haul> result = haulRepository.findByImeiAndLoadTimeAfterAndUnloadTimeBefore("testimei",null,null);
        assertEquals(0,emptyresult.size() );

        List<Haul> result = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.hasImei("testimei"))).forEach(haul -> result.add((Haul)haul));
        //result = haulRepository.findByImeiAndLoadTimeAfterAndUnloadTimeBefore("1", null, null);
        assertEquals("testimei",result.get(0).getImei() );
    }



    @Test
    public void findAllFilterByFromDate() throws Exception {
        List<Haul> emptyresult = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.isAfter(formatter.parse("2017-10-05T16:23:01")))).forEach(haul -> emptyresult.add((Haul)haul));
        assertEquals(0,emptyresult.size() );

        List<Haul> nilresult = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.isAfter(formatter.parse("2017-10-05T15:23:01")))).forEach(haul -> nilresult.add((Haul)haul));
        assertEquals(0,emptyresult.size() );
        List<Haul> result = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.isAfter(formatter.parse("2017-10-05T15:23:00")))).forEach(haul -> result.add((Haul)haul));
        assertEquals("testimei",result.get(0).getImei() );
    }


    @Test
    public void findAllFilterByToDate() throws Exception {
        List<Haul> emptyresult = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.isBefore(formatter.parse("2017-10-05T12:23:01")))).forEach(haul -> emptyresult.add((Haul)haul));
        assertEquals(0,emptyresult.size() );

        List<Haul> nilresult = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.isBefore(formatter.parse("2017-10-05T15:50:01")))).forEach(haul -> nilresult.add((Haul)haul));
        assertEquals(0,emptyresult.size() );
        List<Haul> result = new ArrayList<>();
        haulRepository.findAll(where(HaulSpecs.isBefore(formatter.parse("2017-10-05T15:50:02")))).forEach(haul -> result.add((Haul)haul));
        assertEquals("testimei",result.get(0).getImei() );
    }



}