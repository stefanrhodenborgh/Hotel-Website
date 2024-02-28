package com.stefanrhodenborgh.royalfruitresorts.repository;

import java.util.Optional;

import com.stefanrhodenborgh.royalfruitresorts.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM user WHERE email = ?1 AND account_id IS NOT NULL", nativeQuery = true)
    User getAccountIdFromEmail(String email);

    Optional<User> findByEmailAndAccountIsNotNull(String email);
}
