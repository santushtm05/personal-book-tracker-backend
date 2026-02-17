package com.example.trackerbackend.DAO;

import com.example.trackerbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndDeletedAtIsNull(String username);
    Optional<User> findByIdAndDeletedAtIsNull(Integer id);
    boolean existsByUsernameAndDeletedAtIsNull(String username);
    @Modifying
    @Query("UPDATE User u SET u.deletedAt=CURRENT_TIMESTAMP WHERE u.id=:id AND u.deletedAt=NULL")
    void deleteByIdAndDeletedAtIsNull(Integer id);
    User save(User user);
    Optional<User> findById(Integer Id);
}