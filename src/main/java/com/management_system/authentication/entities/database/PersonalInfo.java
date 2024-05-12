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
@Document("personal_info")
@Builder
public class PersonalInfo {
    @Id
    @Indexed(unique = true)
    String id;

    @Field(name = "name")
    String name;

    @JsonProperty("birth_date")
    @Field(name = "birth_date")
    String birthDate;

    @JsonProperty("phone_number")
    @Field(name = "phone_number")
    String phoneNumber;

    @Field(name = "email")
    String email;

    @Field(name = "address")
    String address;

    @Field(name = "image")
    String image;

    @JsonProperty("working_branch")
    @Field(name = "working_branch")
    String workingBranch;

    @JsonProperty("start_working_date")
    @Field(name = "start_working_date")
    Date startWorkingDate;

    @JsonProperty("end_working_date")
    @Field(name = "end_working_date")
    Date endtWorkingDate;
}


