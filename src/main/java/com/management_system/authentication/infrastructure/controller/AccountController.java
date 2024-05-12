package com.management_system.authentication.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.authentication.entities.api.ApiResponse;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.infrastructure.config.ResponseMapper;
import com.management_system.authentication.usecases.UseCaseExecutor;
import com.management_system.authentication.usecases.account.LoginUseCase;
import com.management_system.authentication.usecases.account.RegisterUseCase;
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
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    final UseCaseExecutor useCaseExecutor;
    final LoginUseCase loginUseCase;
    final RegisterUseCase registerUseCase;


    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<ApiResponse>> login(@RequestBody String json, HttpServletRequest httpRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                loginUseCase,
                new LoginUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(@RequestBody String json, HttpServletRequest httpRequest) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                registerUseCase,
                new RegisterUseCase.InputValue(account),
                ResponseMapper::map
        );
    }
}
