package com.ultramart.backend.services.jwt;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ultramart.backend.dao.UserRepository;
import com.ultramart.backend.entity.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   @Autowired
   private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    Optional<User> user = userRepository.findFirstByEmail(username);

    if(user.isEmpty()) {
        throw new UsernameNotFoundException("User not found with username: " + username);
    } 

    return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), new ArrayList<>());
    }

    



}
