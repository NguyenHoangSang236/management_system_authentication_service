package com.management_system.authentication.entities.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("account")
@Builder
public class Account {
    @Field(name = "user_name")
    @JsonProperty("user_name")
    @Indexed(unique = true)
    String userName;

    @Field(name = "password")
    String password;

    @Field(name = "role")
    String role;

    @Field(name = "creation_date")
    Date creationDate;

    @JsonProperty("current_jwt")
    @Field(name = "current_jwt")
    String currentJwt;

    @JsonProperty("current_fcm_token")
    @Field(name = "current_fcm_token")
    String currentFcmToken;

    @Field(name = "online")
    boolean online;

    @JsonProperty("personal_info")
    @Field(name = "personal_info")
    PersonalInfo personalInfo;
}
