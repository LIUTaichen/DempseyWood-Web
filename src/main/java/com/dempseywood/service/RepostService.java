package com.dempseywood.service;

import com.dempseywood.model.EquipmentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class RepostService {

    private static final Logger log = LoggerFactory.getLogger(RepostService.class);

    @Value("${config.repost}")
    private boolean needsReport;

    @Value("${config.url.repost}")
    private String url;



    public void repost(Iterable<EquipmentStatus> statusIterable) {
        if (needsReport) {

            List<EquipmentStatus> statusList = new ArrayList<>();
            statusIterable.forEach(status -> statusList.add(status));

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));

            HttpEntity<List<EquipmentStatus>> entity = new HttpEntity<List<EquipmentStatus>>(statusList, headers);

            ResponseEntity<String> loginResponse = restTemplate
                    .exchange(url, HttpMethod.POST, entity, String.class);
            if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
                statusIterable.forEach(status ->
                        log.info("equipment status reposted" + status.toString())
                );

            } else {
                log.error(" error  when reposting equipment status");
            }
        }
        else{
            log.info("repost not active. Not sending repost.");
        }
    }

}
