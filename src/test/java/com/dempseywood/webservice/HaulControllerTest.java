package com.dempseywood.webservice;

import com.dempseywood.Application;
import com.dempseywood.model.Haul;
import com.dempseywood.repository.HaulRepository;
import com.dempseywood.service.HaulService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class HaulControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private HaulRepository haulRespository;

    @MockBean
    private HaulService haulService ;

    @Before
    public void setup() throws Exception {
        Haul haul = new Haul();
        haul.setId(100);
        haul.setUuid("uuid");
        given(this.haulRespository.findOne(100))
                .willReturn(haul);

        given(this.haulRespository.findOne(99999))
                .willReturn(null);

        List<Haul> hauls = new ArrayList<>();
        hauls.add(haul);

        given(this.haulRespository.findAll())
                .willReturn(hauls);

    }



    @Test
    public void getHauls() throws Exception {
        mockMvc.perform(get("/api/hauls"))
                .andExpect(status().isOk())
                .andDo(document("home"));
    }

    @Test
    public void getHaul() throws Exception {
        mockMvc.perform(get("/api/hauls/99999"))
                .andExpect(status().isNotFound())
                .andDo(document("home"));
        mockMvc.perform(get("/api/hauls/100"))
                .andExpect(status().isOk())
                .andDo(document("home"));

    }

    @Test
    public void startHaulValid() throws Exception {
        Haul haul = new Haul();
        haul.setUuid("test uuid");
        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(null);
        given(this.haulService.isValidHaul(any()))
                .willReturn(true);
        String json = "{\n" +

                "\"equipment\": test,\n" +
                "\"task\": test,\n" +
                "\"operator\": test,\n" +
                "\"loadLatitude\": 1,\n" +
                "\"loadLongitude\": 1,\n" +
                //"\"unloadLatitude\": null,\n" +
                //"\"unloadLongitude\": null,\n" +
                "\"loadTime\": 2012-04-23T18:25:43.511Z,\n" +
                //"\"unloadTime\": null,\n" +
                "\"imei\": 1111,\n" +
                "\"uuid\": \"test uuid\"\n" +
                "}";
        mockMvc.perform(post("/api/hauls/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

    }
    @Test
    public void startHaulExistsError() throws Exception {
        Haul haul = new Haul();
        haul.setUuid("test uuid");
        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(haul);

        String json = "{\n" +

                "\"equipment\": test,\n" +
                "\"task\": test,\n" +
                "\"operator\": test,\n" +
                "\"loadLatitude\": 1,\n" +
                "\"loadLongitude\": 1,\n" +
                //"\"unloadLatitude\": null,\n" +
                //"\"unloadLongitude\": null,\n" +
                "\"loadTime\": 2012-04-23T18:25:43.511Z,\n" +
                //"\"unloadTime\": null,\n" +
                "\"imei\": 1111,\n" +
                "\"uuid\": \"test uuid\"\n" +
                "}";
        mockMvc.perform(post("/api/hauls/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());
    }

    @Test
    public void startHaulInvalidParametersError() throws Exception {
        Haul haul = new Haul();
        haul.setUuid("test uuid");
        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(null);
        String json = "{\n" +

                "\"equipment\": test,\n" +
                "\"task\": test,\n" +
                "\"operator\": test,\n" +
                "\"loadLatitude\": 1,\n" +
                "\"loadLongitude\": 1,\n" +
                //"\"unloadLatitude\": null,\n" +
                //"\"unloadLongitude\": null,\n" +
                "\"loadTime\": 2012-04-23T18:25:43.511Z,\n" +
                //"\"unloadTime\": null,\n" +
                "\"imei\": 1111,\n" +
                "\"uuid\": \"test uuid\"\n" +
                "}";
        mockMvc.perform(post("/api/hauls/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update() throws Exception {

        Haul haul = new Haul();
        haul.setUuid("test uuid");
        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(null);
        String json = "{\n" +
                "\"id\": 1,\n" +
                "\"equipment\": testEquipment,\n" +
                "\"task\": testTask,\n" +
                "\"operator\": testOperator,\n" +
                "\"loadLatitude\": 1,\n" +
                "\"loadLongitude\": 1,\n" +
                "\"unloadLatitude\": null,\n" +
                "\"unloadLongitude\": null,\n" +
                "\"loadTime\": null,\n" +
                "\"unloadTime\": null,\n" +
                "\"imei\": null,\n" +
                "\"uuid\": null\n" +
                "}";
        mockMvc.perform(put("/api/hauls/100").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isAccepted());

        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(haul);
        mockMvc.perform(put("/api/hauls/99999").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void validateTask() throws Exception {
        Haul haul = new Haul();
        haul.setUuid("test uuid");
        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(null);
        String json = "{\n" +
                "\"id\": 1,\n" +
                "\"equipment\": null,\n" +
                "\"task\": null,\n" +
                "\"operator\": null,\n" +
                "\"status\": null,\n" +
                "\"loadLatitude\": null,\n" +
                "\"loadLongitude\": null,\n" +
                "\"unloadLatitude\": null,\n" +
                "\"unloadLongitude\": null,\n" +
                "\"loadTime\": null,\n" +
                "\"unloadTime\": null,\n" +
                "\"imei\": null,\n" +
                "\"uuid\": \"test uuid\"\n" +
                "}";
        mockMvc.perform(post("/api/hauls/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        given(this.haulRespository.findOneByUuid("test uuid"))
                .willReturn(haul);
        mockMvc.perform(post("/api/hauls/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());

    }

}