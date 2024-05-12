package com.management_system.authentication.usecases.account;

import com.management_system.authentication.entities.api.ApiResponse;
import com.management_system.authentication.entities.database.Account;
import com.management_system.authentication.infrastructure.repository.AccountRepository;
import com.management_system.authentication.usecases.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RegisterUseCase extends UseCase<RegisterUseCase.InputValue, ApiResponse> {
    @Autowired
    AccountRepository accountRepo;

    @Override
    public ApiResponse execute(InputValue input) {
        try {
            Account reqAccount = input.account();

            reqAccount.setCreationDate(new Date());
            reqAccount.getPersonalInfo().setStartWorkingDate(new Date());
            reqAccount.setOnline(false);


            accountRepo.save(reqAccount);

            return ApiResponse.builder()
                    .result("success")
                    .content("Register successfully")
                    .status(HttpStatus.OK)
                    .build();
        }
        catch (DuplicateKeyException dupExp) {
            return ApiResponse.builder()
                    .result("failed")
                    .content("This account has been existed")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
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
