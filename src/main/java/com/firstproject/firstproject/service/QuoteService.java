package com.firstproject.firstproject.service;

import com.firstproject.firstproject.model.QuoteObj;
import com.firstproject.firstproject.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisService redisService;

    public String getQuotes() {
        // getting the api from the app cache
        String api = appCache.APP_CACHE.get("quotes_api");

        //redis cache
        QuoteObj quoteResponse = redisService.get("quote", QuoteObj.class);
        if(quoteResponse != null) {
            return quoteResponse.getQuote();
        } else {
            // making the response and storing data which is get by restTemplate call
            ResponseEntity<QuoteObj> response = restTemplate.exchange(api, HttpMethod.GET, null, QuoteObj.class);
//            return response.getBody().getQuote() != null?response.getBody().getQuote():"NO Quote today!";
            QuoteObj body = response.getBody();
            if(response != null) {
                redisService.set("quote", body, 300l); /* 300 sec lifetime of this key value pair */
            }
            return body.getQuote() + ", Author: " + body.getAuthor();
        }
    }
}

/*
*
    @JsonProperty('observation_time')
    private String observationTime;

    * IF the names in json file and our pojo are different, then use this annotation to connect them
    * which are the best practices for any company IMP**
* */