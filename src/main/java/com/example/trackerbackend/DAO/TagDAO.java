package com.example.trackerbackend.DAO;

import com.example.trackerbackend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagDAO extends JpaRepository<Tag,Integer> {
    Optional<Tag> findByName(String name);
    Optional<Tag> findById(Integer id);
    boolean existsByName(String name);
    List<Tag> findAllByNameIn(Collection<String> names);
}