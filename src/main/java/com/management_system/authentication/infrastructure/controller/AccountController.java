package com.management_system.authentication.infrastructure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.request_dto.AccountRequestDto;
import com.management_system.authentication.usecases.account.*;
import com.management_system.utilities.core.usecase.UseCaseExecutor;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.api.response.ResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Validated
@Tag(name = "Account", description = "Operations related to managing account")
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

    @Operation(summary = "Login to the system")
    @GetMapping("/unauthen/account/login")
    public CompletableFuture<ResponseEntity<ApiResponse>> login(
            @RequestParam
            @NotNull
            @NotBlank
            @Size(max = 20, message = "User name must not be empty")
            String userName,
            @RequestParam
            @NotNull
            @NotBlank
            @Size(max = 30, message = "Password must not be empty")
            String password
    ) {
        return useCaseExecutor.execute(
                loginUseCase,
                new LoginUseCase.InputValue(userName, password),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Register to the system")
    @PostMapping("/unauthen/account/register")
    public CompletableFuture<ResponseEntity<ApiResponse>> register(@RequestBody Account account) {
        return useCaseExecutor.execute(
                registerUseCase,
                new RegisterUseCase.InputValue(account),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Update profile of an account")
    @PostMapping("/authen/account/updateProfile")
    public CompletableFuture<ResponseEntity<ApiResponse>> updateProfile(
            @RequestBody AccountRequestDto accountRequest,
            HttpServletRequest httpRequest)
    {
        return useCaseExecutor.execute(
                updateProfileUseCase,
                new UpdateProfileUseCase.InputValue(accountRequest, httpRequest),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Upload an avatar image of an account")
    @PostMapping(value = "/authen/account/uploadAvatar", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<ApiResponse>> uploadImage(
            @Parameter(description = "File to upload", required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam("file")
            MultipartFile fileUpload,
            @RequestParam("fileName")
            String fileName,
            HttpServletRequest request) {
        return useCaseExecutor.execute(
                uploadImageUseCase,
                new UploadImageUseCase.InputValue(request, fileUpload, fileName),
                ResponseMapper::map
        );
    }

    @Operation(summary = "Refresh access token", description = "Call this API when JWT token is expired")
    @GetMapping("/unauthen/account/refreshAccessToken")
    public CompletableFuture<ResponseEntity<ApiResponse>> refreshAccessToken(HttpServletRequest request) {
        return useCaseExecutor.execute(
                refreshJwtUseCase,
                new RefreshJwtUseCase.InputValue(request),
                ResponseMapper::map
        );
    }
}
