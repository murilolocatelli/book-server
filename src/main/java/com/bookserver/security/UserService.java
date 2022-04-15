package com.bookserver.security;

import com.bookserver.model.User;
import com.bookserver.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        final User user =
            this.userRepository.findByEmail(s).orElseThrow(() ->
                new UsernameNotFoundException("User unauthorized"));

        user.setPassword("{noop}" + user.getPassword());

        return new ResourceOwner(user);
    }

}
