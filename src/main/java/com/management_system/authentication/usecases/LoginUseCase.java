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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        String newRefreshToken;
        String newJwt;
        TokenInfo tokenInfo;

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            Optional<TokenInfo> tokenInfoOptional = refreshTokenRepo.getRefreshTokenInfoByUserName(account.getUsername());

            // if there is any TokenInfo in the database -> get it
            if (tokenInfoOptional.isPresent()) {
                tokenInfo = tokenInfoOptional.get();
            }
            // if there is no TokenInfo in the database -> create a new one
            else {
                tokenInfo = TokenInfo.builder()
                        .id(UUID.randomUUID().toString())
                        .roles(account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .userName(account.getUsername())
                        .build();
            }

            // create a new Refresh token
            newRefreshToken = jwtUtils.generateJwt(tokenInfo, TokenType.REFRESH_TOKEN);
            tokenInfo.setToken(newRefreshToken);

            // save to database
            refreshTokenRepo.save(tokenInfo);

            // create a new JWT
            newJwt = jwtUtils.generateJwt(tokenInfo, TokenType.JWT);

            log.info("New Refresh Token: " + newRefreshToken);
            log.info("New JWT: " + newJwt);

            return ApiResponse.builder()
                    .result("success")
                    .message("Login successfully")
                    .jwt(newJwt)
                    .refreshToken(newRefreshToken)
                    .content(account)
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ApiResponse.builder()
                    .result("failed")
                    .message("This account does not exist")
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }


    public record InputValue(Account account) implements UseCase.InputValue {
    }
}
