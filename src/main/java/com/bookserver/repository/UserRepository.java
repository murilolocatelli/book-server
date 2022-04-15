package com.bookserver.repository;

import com.bookserver.model.User;
import com.fasterxml.jackson.annotation.OptBoolean;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {

    @Query
    boolean existsByName(String name);

    @Query
    Page<User> findByName(String name, Pageable pageable);

    @Query
    Optional<User> findByEmail(final String email);

}
