package com.management_system.authentication.usecases.employee;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.database.PersonalInfo;
import com.management_system.authentication.entities.request_dto.EmployeeFilterOptions;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FilterEmployeesUseCase extends UseCase<FilterEmployeesUseCase.InputValue, ApiResponse> {
    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        List<Account> accounts = dbUtils.filterData(input.request, Account.class);

        List<PersonalInfo> personalInfoList = new ArrayList<>();
        accounts.stream().map(Account::getPersonalInfo).forEach(personalInfoList::add);

        return ApiResponse.builder()
                .result("success")
                .content(personalInfoList)
                .status(HttpStatus.OK)
                .build();
    }

    public record InputValue(FilterRequest<EmployeeFilterOptions> request) implements UseCase.InputValue {
    }
}
