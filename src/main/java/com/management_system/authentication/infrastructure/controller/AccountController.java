package com.management_system.authentication.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.request_dto.AccountRequestDto;
import com.management_system.authentication.usecases.account.*;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
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
    final RefreshJwtUseCase refreshJwtUseCase;

    @PostMapping("/unauthen/account/login")
    public CompletableFuture<ResponseEntity<ApiResponse>> login(@RequestBody Account account) throws IOException {
        return useCaseExecutor.execute(
                loginUseCase,
                new LoginUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/unauthen/account/register")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(@RequestBody Account account) throws IOException {
        return useCaseExecutor.execute(
                registerUseCase,
                new RegisterUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @PostMapping("/authen/account/updateProfile")
    public CompletableFuture<ResponseEntity<ApiResponse>> updateProfile(@RequestBody AccountRequestDto accountRequest, HttpServletRequest httpRequest) throws JsonProcessingException {
        return useCaseExecutor.execute(
                updateProfileUseCase,
                new UpdateProfileUseCase.InputValue(accountRequest, httpRequest),
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


    @GetMapping("/unauthen/account/refreshAccessToken")
    public CompletableFuture<ResponseEntity<ApiResponse>> refreshAccessToken(HttpServletRequest request) throws IOException {
        return useCaseExecutor.execute(
                refreshJwtUseCase,
                new RefreshJwtUseCase.InputValue(request),
                ResponseMapper::map
        );
    }
}
