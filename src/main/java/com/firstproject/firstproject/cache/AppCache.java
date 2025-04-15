package com.firstproject.firstproject.cache;

import com.firstproject.firstproject.entity.ConfigJournalApp;
import com.firstproject.firstproject.repo.ConfigRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigRepo configRepo;

    public Map<String, String> APP_CACHE;


    /*HERE-> post construct is an annotation which is used on any method, now when the class bean gets initialized then ,
    * this annotated method will be called automatically
    * it is commonly used to configure data from db one time only.*/
    @PostConstruct
    public void init() {
        APP_CACHE = new HashMap<>();
        List<ConfigJournalApp> allAPIs= configRepo.findAll();

        for(ConfigJournalApp i: allAPIs) {
            APP_CACHE.put(i.getKey(), i.getValue());
        }
    }
}
