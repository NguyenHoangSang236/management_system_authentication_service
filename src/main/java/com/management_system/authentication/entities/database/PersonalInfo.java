package com.management_system.authentication.entities.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.utilities.core.validator.EmailConstraint;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("personal_info")
@Builder
public class PersonalInfo {
    @Id
    @JsonProperty("personal_certificate_id")
    String id;

    @Field(name = "name")
    String name;

    @JsonProperty("birth_date")
    @Field(name = "birth_date")
    String birthDate;

    @JsonProperty("phone_number")
    @Field(name = "phone_number")
    String phoneNumber;

    @EmailConstraint
    @Field(name = "email")
    String email;

    @Field(name = "address")
    String address;

    @Field(name = "image")
    String image;

    @JsonProperty("working_branch")
    @Field(name = "working_branch")
    String workingBranch;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("start_working_date")
    @Field(name = "start_working_date")
    Date startWorkingDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("end_working_date")
    @Field(name = "end_working_date")
    Date endtWorkingDate;


    public Map<String, Object> toSubMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(this, Map.class);
        Map<String, Object> resMap = new HashMap<>();

        // add 'personal_info.' before each key in map
        for (String key : map.keySet()) {
            if (map.get(key) != null) {

                String newKey = "personal_info." + key;

                resMap.put(newKey, map.get(key));
            }
        }

        return resMap;
    }
}


