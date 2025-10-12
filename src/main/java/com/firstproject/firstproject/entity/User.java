package com.firstproject.firstproject.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;

    @Indexed(unique = true) // making it unique
    @NonNull
    @Schema(description = "unique name of the user")
    private String userName;
    @NonNull // not null by lombok annotation, when we will set the value of these then the annotation will check if it's null or not
    private String password;

    private String email;
    private boolean sentimentAnalysis;

    // we are referring different table entity in different table, works like foreign key in here
    @DBRef
    private List<Journal> journalEntries = new ArrayList<>();
    private List<String> Roles;
}
