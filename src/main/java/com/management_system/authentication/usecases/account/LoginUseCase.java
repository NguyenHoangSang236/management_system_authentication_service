package com.management_system.authentication.usecases.account;

import com.management_system.authentication.entities.api.ApiResponse;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.authentication.usecases.UseCase;
import com.management_system.ultilities.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class LoginUseCase extends UseCase<LoginUseCase.InputValue, ApiResponse>{
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountRepository accountRepo;


    @Override
    public ApiResponse execute(InputValue input) {
        try {
            Account reqAccount = input.account();
            Account account = accountRepo.getAccountByUserNameAndPassword(reqAccount.getUsername(), reqAccount.getPassword());

            String jwt = jwtUtils.generateJwt(account);
            System.out.println(jwt);

            if (account != null) {
                return ApiResponse.builder()
                        .result("success")
                        .content(account)
                        .status(HttpStatus.OK)
                        .build();
            }
            else {
                return ApiResponse.builder()
                        .result("failed")
                        .content("This account does not exist")
                        .status(HttpStatus.NO_CONTENT)
                        .build();
            }
        }
        catch (Exception e) {
            e.printStackTrace();

            return ApiResponse.builder()
                    .result("failed")
                    .content("Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    public record InputValue(Account account) implements UseCase.InputValue {}
}
