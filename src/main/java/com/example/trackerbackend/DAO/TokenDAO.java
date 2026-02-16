package com.example.trackerbackend.DAO;

import com.example.trackerbackend.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TokenDAO extends JpaRepository<BlacklistedToken,Integer> {
    boolean existsByToken(String token);
    void deleteAllByExpiryDateBefore(LocalDateTime time);
}