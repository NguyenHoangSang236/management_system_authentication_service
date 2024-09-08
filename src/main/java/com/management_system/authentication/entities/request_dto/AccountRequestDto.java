package com.management_system.authentication.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.authentication.entities.database.PersonalInfo;
import com.management_system.utilities.entities.api.request.ApiRequest;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto extends ApiRequest implements Serializable {
    @JsonProperty("user_name")
    String userName;

    String password;

    String role;

    PersonalInfo personalInfo;
}
