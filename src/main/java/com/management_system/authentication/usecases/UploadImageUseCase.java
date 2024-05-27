package com.management_system.authentication.usecases;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.database.PersonalInfo;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.FirebaseUtils;
import com.management_system.utilities.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadImageUseCase extends UseCase<UploadImageUseCase.InputValue, ApiResponse> {
    @Autowired
    FirebaseUtils firebaseUtils;

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    DbUtils dbUtils;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            String uploadedImageUrl = firebaseUtils.upload(input.multipartFile(), input.fileName());

            TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());
            Account account = accountRepo.getAccountByUserName(tokenInfo.getUserName());
            PersonalInfo personalInfo = account.getPersonalInfo();

            personalInfo.setImage(uploadedImageUrl);
            dbUtils.updateSpecificFields(account.getId(), personalInfo.toSubMap(), Account.class);

            return ApiResponse.builder()
                    .result("success")
                    .content("Update profile successfully")
                    .message(uploadedImageUrl)
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content("Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }    }


    public record InputValue(HttpServletRequest request, MultipartFile multipartFile, String fileName) implements UseCase.InputValue {}
}
