package com.management_system.authentication.usecases;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.utilities.constant.enumuration.TokenType;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.database.TokenInfo;
import com.management_system.utilities.repository.RefreshTokenRepository;
import com.management_system.utilities.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component
public class LoginUseCase extends UseCase<LoginUseCase.InputValue, ApiResponse> {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountRepository accountRepo;

    @Autowired
    RefreshTokenRepository refreshTokenRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        Account reqAccount = input.account();
        Optional<Account> accountOptional = accountRepo.getAccountByUserNameAndPassword(reqAccount.getUsername(), reqAccount.getPassword());

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            TokenInfo tokenInfo = refreshTokenRepo.getRefreshTokenInfoByUserName(account.getUsername());

            String newJwtToken = jwtUtils.generateJwt(
                    TokenInfo.builder().userName(account.getUsername()).roles(Collections.singletonList(account.getRole())).build(),
                    TokenType.JWT
            );
            log.info("JWT: " + newJwtToken);

            return ApiResponse.builder()
                    .result("success")
                    .jwt(newJwtToken)
                    .refreshToken(tokenInfo.getToken())
                    .content(account)
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result("failed")
                    .content("This account does not exist")
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
    }


    public record InputValue(Account account) implements UseCase.InputValue {
    }
}
