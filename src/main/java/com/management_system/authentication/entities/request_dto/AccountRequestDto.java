package com.management_system.authentication.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.authentication.entities.database.PersonalInfo;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {
    @JsonProperty("user_name")
    String userName;

    String password;

    String role;

    PersonalInfo personalInfo;
}
