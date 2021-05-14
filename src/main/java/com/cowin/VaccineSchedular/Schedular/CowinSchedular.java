package com.cowin.VaccineSchedular.Schedular;

import com.cowin.VaccineSchedular.DTO.Response;
import com.cowin.VaccineSchedular.DTO.Sessions;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CowinSchedular {
    private Logger logger = LoggerFactory.getLogger(CowinSchedular.class);

    RestTemplate restTemplate = new RestTemplate();
    @Scheduled(cron = "0 */2 * * * *")
    public void run() {
        String date = getNextDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Host", "cdn-api.co-vin.in");
        headers.set("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Safari/605.1.15");
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=110094&date=" + date;
        ResponseEntity<Response> responseEntity = restTemplate.exchange(url, HttpMethod.GET,request, Response.class);
        logger.info("Response of cowin : {}, url : {}", responseEntity.getBody(),url);
        Response response = responseEntity.getBody();
        List<Sessions> sessions = response.getSessions();
        for (Sessions session: sessions) {
            if(session.getAvailableCapacity()>=10 && session.getMinAgeLimit() < 40) {
                String mailUrl = "https://hooks.zapier.com/hooks/catch/10130320/by0oy1e/";
                Map<String,Object> requestBody = new HashMap<>();
                requestBody.put("email", "sachinsarswt7@gmail.com");
                requestBody.put("slots", session.getAvailableCapacity());
                requestBody.put("name", session.getName());
                requestBody.put("address", session.getAddress());
                requestBody.put("vaccine", session.getVaccine());
                ResponseEntity<JsonNode> mailResponse = restTemplate.postForEntity(mailUrl,requestBody,JsonNode.class);
                logger.info("Response : {}",mailResponse);
            }
        }
    }
    private String getNextDate(String currDate) {
        Calendar today = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String nextDate = "";
        try {
            Date date = format.parse(currDate);
            today.setTime(date);
            today.add(Calendar.DAY_OF_YEAR, 1);
            nextDate = format.format(today.getTime());
        } catch (Exception e) {
            logger.info("Exception occured : {}",e);
        }
        return nextDate;
    }
}

