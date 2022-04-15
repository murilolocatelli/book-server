package com.bookserver.business.impl;

import com.bookserver.business.UserBusiness;
import com.bookserver.exception.EntityAlreadyExistsException;
import com.bookserver.exception.EntityNotFoundException;
import com.bookserver.model.User;
import com.bookserver.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessImpl implements UserBusiness {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> findAll(final String name, final Pageable pageable) {
        final List<User> users = new ArrayList<>();

        Page<User> page;

        if (name == null) {
            page = this.userRepository.findAll(pageable);
        } else {
            page = this.userRepository.findByName(name, pageable);
        }

        page.forEach(users::add);

        return new PageImpl<>(users, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public Optional<User> create(User user) {
        boolean userExists = this.userRepository.existsByName(user.getName());

        if (userExists) {
            throw new EntityAlreadyExistsException("User");
        }

        user = this.userRepository.save(user);

        return Optional.of(user);
    }

    @Override
    public Optional<User> findById(final Long id) {
        final Optional<User> optionalUser = this.userRepository.findById(id);

        return Optional.ofNullable(optionalUser.orElse(null));
    }

    @Override
    public Optional<User> update(final Long id, final User user) {
        final Optional<User> optionalUserSaved = this.userRepository.findById(id);

        optionalUserSaved.orElseThrow(() -> new EntityNotFoundException("User"));

        user.setId(id);

        this.userRepository.save(user);

        return Optional.of(user);
    }

    @Override
    public void deleteById(final Long id) {
        final Optional<User> optionalUser = this.userRepository.findById(id);

        optionalUser.orElseThrow(() -> new EntityNotFoundException("User"));

        this.userRepository.deleteById(id);
    }

}
