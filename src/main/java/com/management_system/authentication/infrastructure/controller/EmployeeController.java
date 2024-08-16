package com.management_system.authentication.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.management_system.authentication.entities.request_dto.EmployeeFilterOptions;
import com.management_system.authentication.usecases.employee.FilterEmployeesUseCase;
import com.management_system.utilities.core.deserializer.FilterOptionsDeserializer;
import com.management_system.utilities.core.filter.FilterOption;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/authen/employee", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class EmployeeController {
    final UseCaseExecutor useCaseExecutor;
    final FilterEmployeesUseCase filterEmployeesUseCase;


    @PostMapping("/filterEmployees")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterEmployees(@RequestBody FilterRequest<EmployeeFilterOptions> filterRequest) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//
//        module.addDeserializer(FilterOption.class, new FilterOptionsDeserializer(EmployeeFilterOptions.class));
//        objectMapper.registerModule(module);
//
//        FilterRequest filterRequest = objectMapper.readValue(json, FilterRequest.class);

        return useCaseExecutor.execute(
                filterEmployeesUseCase,
                new FilterEmployeesUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }
}
