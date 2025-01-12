package com.management_system.authentication.infrastructure.controller;

import com.management_system.authentication.entities.request_dto.EmployeeFilterOptions;
import com.management_system.authentication.usecases.employee.FilterEmployeesUseCase;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.request.FilterRequest;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Employee", description = "Operations related to managing account")
@RestController
@RequestMapping(value = "/authen/employee", consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class EmployeeController {
    final UseCaseExecutor useCaseExecutor;
    final FilterEmployeesUseCase filterEmployeesUseCase;

    @Operation(summary = "Get employees by filters")
    @PostMapping("/filterEmployees")
    public CompletableFuture<ResponseEntity<ApiResponse>> filterEmployees(@RequestBody FilterRequest<EmployeeFilterOptions> filterRequest) throws IOException {
        return useCaseExecutor.execute(
                filterEmployeesUseCase,
                new FilterEmployeesUseCase.InputValue(filterRequest),
                ResponseMapper::map
        );
    }
}
