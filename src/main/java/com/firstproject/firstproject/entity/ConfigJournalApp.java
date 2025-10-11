package com.firstproject.firstproject.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configJournalApp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigJournalApp {

    private String key;
    private String value;
}