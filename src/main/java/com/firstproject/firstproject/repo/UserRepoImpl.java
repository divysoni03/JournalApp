package com.firstproject.firstproject.repo;

import java.util.List;
import com.firstproject.firstproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class UserRepoImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSA() {
        Query query = new Query();
//      query.addCriteria(Criteria.where("name").is("divy303"));
        query.addCriteria(Criteria.where("email").regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+\\.[A-Z|a-z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<User> users = mongoTemplate.find(query, User.class);


        return users;
    }
}
