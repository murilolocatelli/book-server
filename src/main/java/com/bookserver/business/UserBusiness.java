package com.bookserver.business;

import com.bookserver.model.User;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserBusiness {

    Page<User> findAll(final String name, final Pageable pageable);

    Optional<User> create(User user);

    Optional<User> findById(final Long id);

    Optional<User> update(final Long id, final User user);

    void deleteById(final Long id);

}
