package com.martin.jpa.controller;

import com.martin.jpa.dto.RequestResponse;
import com.martin.jpa.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restricted")
@CrossOrigin("*")
public class RestrictedController {

    @Autowired
    private AuthService authService;

    @GetMapping("/profile")
    public ResponseEntity<RequestResponse> getProfile(){
        return new ResponseEntity<>(authService.profile(), HttpStatus.OK);
    }

    @PutMapping("/updateprofile")
    public ResponseEntity<RequestResponse> updateProfile(@Valid @RequestBody RequestResponse updateProfileRequest){
        return new ResponseEntity<>(authService.updateProfile(updateProfileRequest), HttpStatus.OK);
    }

}
