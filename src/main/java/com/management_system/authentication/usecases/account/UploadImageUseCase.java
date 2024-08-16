package com.management_system.authentication.usecases.account;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.database.PersonalInfo;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.database.TokenInfo;
import com.management_system.utilities.utils.FirebaseUtils;
import com.management_system.utilities.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class UploadImageUseCase extends UseCase<UploadImageUseCase.InputValue, ApiResponse> {
    @Autowired
    FirebaseUtils firebaseUtils;

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            String uploadedImageUrl = firebaseUtils.upload(input.multipartFile(), input.fileName());

            TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());
            Optional<Account> accountOptional = accountRepo.getAccountByUserName(tokenInfo.getUserName());

            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                PersonalInfo personalInfo = account.getPersonalInfo();
                personalInfo.setImage(uploadedImageUrl);
                account.setPersonalInfo(personalInfo);

                accountRepo.save(account);

                return ApiResponse.builder()
                        .result("success")
                        .message("Update profile successfully")
                        .content(uploadedImageUrl)
                        .status(HttpStatus.OK)
                        .build();
            } else {
                return ApiResponse.builder()
                        .result("failed")
                        .message("This account does not exist")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .message("Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    public record InputValue(HttpServletRequest request, MultipartFile multipartFile,
                             String fileName) implements UseCase.InputValue {
    }
}
