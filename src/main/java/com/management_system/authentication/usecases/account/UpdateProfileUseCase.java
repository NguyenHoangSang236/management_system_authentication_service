package com.management_system.authentication.usecases.account;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.entities.database.PersonalInfo;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class UpdateProfileUseCase extends UseCase<UpdateProfileUseCase.InputValue, ApiResponse> {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    DbUtils dbUtils;

    @Autowired
    AccountRepository accountRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            String jwt = jwtUtils.getJwtFromRequest(input.request());
            TokenInfo tokenInfo = jwtUtils.getRefreshTokenInfoFromJwt(jwt);
            Account account = accountRepo.getAccountByUserName(tokenInfo.getUserName());

            dbUtils.updateSpecificFields(account.getId(), input.personalInfo().toSubMap(), Account.class);

            return ApiResponse.builder()
                    .result("success")
                    .content("Update profile successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content("Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue(PersonalInfo personalInfo, HttpServletRequest request) implements UseCase.InputValue {
    }
}
