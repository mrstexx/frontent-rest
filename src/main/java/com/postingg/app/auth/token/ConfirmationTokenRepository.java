package com.postingg.app.auth.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "UPDATE ConfirmationToken t SET t.confirmedAt = :confAt WHERE t.token = :token")
    void updateConfirmedAt(@Param("token") String token, @Param("confAt") LocalDateTime confirmedAt);
}
