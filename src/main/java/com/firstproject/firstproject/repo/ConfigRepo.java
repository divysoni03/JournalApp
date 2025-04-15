package com.firstproject.firstproject.repo;

import com.firstproject.firstproject.entity.ConfigJournalApp;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepo extends MongoRepository<ConfigJournalApp, ObjectId> {
}
