package com.management_system.authentication.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.usecases.LoginUseCase;
import com.management_system.authentication.usecases.RegisterUseCase;
import com.management_system.authentication.usecases.UpdateProfileUseCase;
import com.management_system.authentication.usecases.UploadImageUseCase;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(consumes = {"*/*"}, produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class AccountController {
    final UseCaseExecutor useCaseExecutor;
    final LoginUseCase loginUseCase;
    final RegisterUseCase registerUseCase;
    final UpdateProfileUseCase updateProfileUseCase;
    final UploadImageUseCase uploadImageUseCase;


    @PostMapping("/unauthen/account/login")
    public CompletableFuture<ResponseEntity<ApiResponse>> login(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                loginUseCase,
                new LoginUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/unauthen/account/register")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(@RequestBody String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                registerUseCase,
                new RegisterUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/authen/account/updateProfile")
    public CompletableFuture<ResponseEntity<ApiResponse>> updateProfile(@RequestBody String json, HttpServletRequest httpRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = objectMapper.readValue(json, Account.class);

        return useCaseExecutor.execute(
                updateProfileUseCase,
                new UpdateProfileUseCase.InputValue(account, httpRequest),
                ResponseMapper::map
        );
    }

    @PostMapping("/authen/account/uploadAvatar")
    public CompletableFuture<ResponseEntity<ApiResponse>> uploadImage(@RequestParam("file") MultipartFile fileUpload,
                                                                      @RequestParam("fileName") String fileName,
                                                                      HttpServletRequest request) {
        return useCaseExecutor.execute(
                uploadImageUseCase,
                new UploadImageUseCase.InputValue(request, fileUpload, fileName),
                ResponseMapper::map
        );
    }

}
