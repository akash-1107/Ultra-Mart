package com.ultramart.backend.controller;

import java.io.IOException;
import java.util.Optional;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.apache.catalina.connector.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.UserDataHandler;

import com.ultramart.backend.dao.UserRepository;
import com.ultramart.backend.dto.AuthenticationRequest;
import com.ultramart.backend.dto.SignupRequest;
import com.ultramart.backend.dto.UserDto;
import com.ultramart.backend.entity.User;
import com.ultramart.backend.services.auth.AuthService;
import com.ultramart.backend.utils.JwtUtil;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final String HEADER_STRING = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    @Autowired
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @GetMapping("/")
    public String hello() {
        return "Hello World!";
    }

    // postmapping void method for create authentication token
    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws IOException, JSONException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);

        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        Optional<User> user = userRepository.findFirstByEmail(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if (user.isPresent()) {
            response.getWriter().write(new JSONObject()
                    .put("userId", user.get().getId())
                    .put("role", user.get().getRole())
                    .toString()

            );
            response.addHeader("Access-Control-Expose-Headers", "Authorization");
            response.addHeader("Access-Control-Expose-Headers",
                    "Authorization,X-Pingother,Content-Type,Authorization,credential,X-CustomHeader,X-Auth-Token");

            response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwt);
        }

    }

    // postmapping void method for create user
    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("User with this email already exists",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append(" "));
        return new ResponseEntity<>(errors.toString().trim(), HttpStatus.BAD_REQUEST);
    }

}
