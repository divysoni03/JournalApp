package com.firstproject.firstproject.repo;

import com.firstproject.firstproject.entity.Journal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryRepo extends MongoRepository<Journal, ObjectId>{}

/* -> HERE WHEN WE EXTEND OUR INTERFACE WITH MONGOREPO THEN WE PASS TWO PARAMETER <POJO, DATATYPE OF ID>
*   POJO ->
        In Spring Boot, a POJO (Plain Old Java Object) is a simple Java object that does not adhere to any specific framework conventions or implement any framework-specific interfaces. It's a standard Java class used to represent data or functionality without dependencies on external frameworks or libraries. POJOs are characterized by their simplicity and focus on data encapsulation, typically containing private fields and public getter and setter methods to access and modify the data.
        POJOs are commonly used in Spring Boot applications for various purposes, including:
        Data Transfer Objects (DTOs): Representing data structures for transferring data between different layers of the application.
        Entities: Mapping to database tables using frameworks like JPA (Java Persistence API).
        Request and Response Objects: Handling data in RESTful APIs.
        Configuration Objects: Defining application settings and properties.
        POJOs promote code reusability, testability, and maintainability by keeping the business logic separate from framework-specific concerns. They are a fundamental building block in Spring Boot development, enabling developers to create clean, modular, and easily understandable applications.
*
* */