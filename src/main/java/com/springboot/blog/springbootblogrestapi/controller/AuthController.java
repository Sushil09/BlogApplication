package com.springboot.blog.springbootblogrestapi.controller;

import com.springboot.blog.springbootblogrestapi.model.Roles;
import com.springboot.blog.springbootblogrestapi.model.User;
import com.springboot.blog.springbootblogrestapi.payload.JWTAuthResponse;
import com.springboot.blog.springbootblogrestapi.payload.LogInDto;
import com.springboot.blog.springbootblogrestapi.payload.SignupDto;
import com.springboot.blog.springbootblogrestapi.repository.RoleRepository;
import com.springboot.blog.springbootblogrestapi.repository.UserRepository;
import com.springboot.blog.springbootblogrestapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/auth/v1/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LogInDto logInDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInDto.getUsernameOrEmail(),logInDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //get token from token provider class
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/api/auth/v1/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupDto signupDto){

        //add check for userName exists in db.

        if(userRepository.existsByUsername(signupDto.getUsername())){
            return new ResponseEntity<>("Username already taken",HttpStatus.BAD_REQUEST);
        }

        //add check for email exists in db

        if(userRepository.existsByEmail(signupDto.getEmail())){
            return new ResponseEntity<>("Email already exists",HttpStatus.BAD_REQUEST);
        }

        //create user object

        User user = new User();

        user.setName(signupDto.getName());
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        Roles roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);

        return new ResponseEntity<>("User successfully registered",HttpStatus.OK);
    }

}
