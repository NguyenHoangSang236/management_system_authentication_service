package com.management_system.authentication.infrastructure.repository;

import com.management_system.authentication.entities.database.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    @Query("{'user_name': ?0, 'password': ?1}")
    Optional<Account> getAccountByUserNameAndPassword(String userName, String password);

    @Query("{'user_name': ?0}")
    Optional<Account> getAccountByUserName(String userName);
}
