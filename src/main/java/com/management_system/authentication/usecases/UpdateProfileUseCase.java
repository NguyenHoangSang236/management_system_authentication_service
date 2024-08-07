package com.management_system.authentication.usecases;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.database.TokenInfo;
import com.management_system.utilities.utils.DbUtils;
import com.management_system.utilities.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
            TokenInfo tokenInfo = jwtUtils.getTokenInfoFromHttpRequest(input.request());
            Optional<Account> accountOptional = accountRepo.getAccountByUserName(tokenInfo.getUserName());

            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                account.setPersonalInfo(dbUtils.mergeObjectFromRequest(account.getPersonalInfo(), input.account().getPersonalInfo()));

                accountRepo.save(dbUtils.mergeMongoEntityFromRequest(accountOptional.get(), account));

                return ApiResponse.builder()
                        .result("success")
                        .message("Update profile successfully")
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
                    .content("Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public record InputValue(Account account, HttpServletRequest request) implements UseCase.InputValue {
    }
}
