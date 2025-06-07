package com.martin.jpa.controller;

import com.martin.jpa.dto.RequestResponse;
import com.martin.jpa.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    //register an account
    @PostMapping("/signup")
    public ResponseEntity<RequestResponse> signUp(@Valid @RequestBody RequestResponse signUpRequest) throws Exception {
        return new ResponseEntity<>(authService.signUp(signUpRequest), HttpStatus.CREATED);
    }

    //sign in
    @PostMapping("/signin")
    public ResponseEntity<RequestResponse> signIn(@Valid @RequestBody RequestResponse signInRequest){
        return new ResponseEntity<>(authService.signIn(signInRequest), HttpStatus.OK);
    }

    //refresh token
    @PostMapping("/refreshtoken")
    public ResponseEntity<RequestResponse> refreshToken(@Valid @RequestBody RequestResponse tokenRequest){
        // TODO
        return new ResponseEntity<>(authService.refreshToken(tokenRequest), HttpStatus.OK);
    }

}
