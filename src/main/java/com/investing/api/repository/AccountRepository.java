package com.investing.api.repository;

import com.investing.api.entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(nativeQuery = true, value = "select * from accounts where uuid = :uuid")
    Account findByUUID(UUID uuid);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE accounts set email = :email where uuid = :uuid")
    Void updateEmailByUuid(@Param("uuid") String uuid, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE accounts set password = :password where uuid = :uuid")
    Void updatePasswordByUuid(@Param("uuid") String uuid, @Param("password") String password);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE from accounts where uuid = :uuid")
    Void deleteByUuid(@Param("uuid") UUID uuid);
}
