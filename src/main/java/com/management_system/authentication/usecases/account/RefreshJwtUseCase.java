package com.management_system.authentication.usecases.account;

import com.management_system.utilities.constant.enumuration.TokenType;
import com.management_system.utilities.core.usecase.UseCase;
import com.management_system.utilities.entities.api.response.ApiResponse;
import com.management_system.utilities.entities.database.TokenInfo;
import com.management_system.utilities.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RefreshJwtUseCase extends UseCase<RefreshJwtUseCase.InputValue, ApiResponse> {
    private final JwtUtils jwtUtils;

    public RefreshJwtUseCase(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public ApiResponse execute(InputValue input) {
        String refreshToken = jwtUtils.getRefreshTokenFromRequest(input.request());

        if (refreshToken.isBlank()) {
            return ApiResponse.builder()
                    .result("failed")
                    .message("Refresh token must not be null")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else {
            boolean isRefreshTokenExpired = jwtUtils.isTokenExpired(refreshToken);

            if (isRefreshTokenExpired) {
                log.info("Refresh token has been expired");

                return ApiResponse.builder()
                        .result("failed")
                        .message("Refresh token has been expired, you must login")
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }

            TokenInfo tokenInfo = jwtUtils.getRefreshTokenInfoFromJwt(refreshToken);
            String newJwt = jwtUtils.generateJwt(tokenInfo, TokenType.JWT);

            return ApiResponse.builder()
                    .result("success")
                    .message("Refresh JWT successfully")
                    .content(newJwt)
                    .status(HttpStatus.OK)
                    .build();
        }

    }


    public record InputValue(HttpServletRequest request) implements UseCase.InputValue {
    }
}
