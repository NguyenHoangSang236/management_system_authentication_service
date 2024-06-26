package com.management_system.authentication.usecases;

import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.utilities.constant.enumuration.TokenType;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.ApiResponse;
import com.management_system.utilities.entities.TokenInfo;
import com.management_system.utilities.repository.RefreshTokenRepository;
import com.management_system.utilities.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        Account account = accountRepo.getAccountByUserNameAndPassword(reqAccount.getUsername(), reqAccount.getPassword());

        if (account != null) {
            TokenInfo tokenInfo = TokenInfo.builder()
                    .userName(account.getUsername())
                    .roles(Arrays.asList(account.getRole()))
                    .build();

            String newJwtToken = jwtUtils.generateJwt(tokenInfo, TokenType.JWT);
            System.out.println("JWT: " + newJwtToken);
            jwtUtils.createRefreshTokenForAccount(account.getUsername(), account.getRole());

            return ApiResponse.builder()
                    .result("success")
                    .message(newJwtToken)
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
