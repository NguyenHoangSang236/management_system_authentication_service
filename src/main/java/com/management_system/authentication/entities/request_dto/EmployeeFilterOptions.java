package com.management_system.authentication.entities.request_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.management_system.utilities.core.filter.FilterOption;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeFilterOptions extends FilterOption implements Serializable {
    String name;

    String id;

    String role;

    @JsonProperty(value = "user_name")
    String userName;

    @JsonProperty(value = "phone_number")
    String phoneNumber;

    String email;

    String address;

    @JsonProperty(value = "working_branch")
    String workingBranch;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("from_start_working_date")
    Date fromStartWorkingDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonProperty("to_start_working_date")
    Date toStartWorkingDate;


    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("user_name", this.userName);
        map.put("role", this.role);
        map.put("personal_info.name", this.name);
        map.put("personal_info.id", this.id);
        map.put("personal_info.phone_number", this.phoneNumber);
        map.put("personal_info.email", this.email);
        map.put("personal_info.address", this.address);
        map.put("personal_info.working_branch", this.workingBranch);

        return map;
    }
}
