package com.firstproject.firstproject.service;

import com.firstproject.firstproject.api.QuoteObj;
import com.firstproject.firstproject.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class QuoteService {

//    -> getting from the application.ymp
//    @Value("${quotes.api}")
//    protected String api;

//    protected static String api = "https://dummyjson.com/quotes/random";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    public String getQuote() {
        // getting the api from the app cache
        String api = appCache.APP_CACHE.get("quotes_api");

        // making the response and storing data which is get by restTemplate call
        ResponseEntity<QuoteObj> response = restTemplate.exchange(api, HttpMethod.GET, null, QuoteObj.class);
        return response.getBody().getQuote() != null?response.getBody().getQuote():"NO Quote today!";
    }
}

/*
*
    @JsonProperty('observation_time')
    private String observationTime;

    * IF the names in json file and our pojo are different, then use this annotation to connect them
    * which are the best practices for any company IMP**
* */