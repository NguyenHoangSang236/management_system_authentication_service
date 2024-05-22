package com.management_system.authentication.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.authentication.entities.api.ApiResponse;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.database.PersonalInfo;
import com.management_system.authentication.infrastructure.config.ResponseMapper;
import com.management_system.authentication.usecases.UseCaseExecutor;
import com.management_system.authentication.usecases.account.LoginUseCase;
import com.management_system.authentication.usecases.account.RegisterUseCase;
import com.management_system.authentication.usecases.account.UpdateProfileUseCase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/unauthen/account")
@AllArgsConstructor
public class AccountController {
    final UseCaseExecutor useCaseExecutor;
    final LoginUseCase loginUseCase;
    final RegisterUseCase registerUseCase;
    final UpdateProfileUseCase updateProfileUseCase;


    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<ApiResponse>> login(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                loginUseCase,
                new LoginUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                registerUseCase,
                new RegisterUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/updateProfile")
    public  CompletableFuture<ResponseEntity<ApiResponse>> updateProfile(@RequestBody String json, HttpServletRequest httpRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PersonalInfo personalInfo= objectMapper.readValue(json, PersonalInfo.class);

        return useCaseExecutor.execute(
                updateProfileUseCase,
                new UpdateProfileUseCase.InputValue(personalInfo, httpRequest),
                ResponseMapper::map
        );
    }
}
