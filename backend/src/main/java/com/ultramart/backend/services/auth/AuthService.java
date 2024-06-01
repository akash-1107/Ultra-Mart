package com.ultramart.backend.services.auth;

import com.ultramart.backend.dto.SignupRequest;
import com.ultramart.backend.dto.UserDto;

public interface AuthService {
     UserDto createUser(SignupRequest signupRequest);

     boolean hasUserWithEmail(String email);
}
