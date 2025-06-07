package com.martin.jpa.service;

import com.martin.jpa.dto.RequestResponse;
import com.martin.jpa.exception.PasswordBlankException;
import com.martin.jpa.model.Customer;
import com.martin.jpa.repository.CustomerRepository;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    // signUp method
    public RequestResponse signUp(RequestResponse registrationRequest) throws Exception{

        RequestResponse requestResponse = new RequestResponse();

        Customer customer = new Customer();

        customer.setFirstName(registrationRequest.getFirstName());
        customer.setLastName(registrationRequest.getLastName());
        customer.setEmail(registrationRequest.getEmail());
        customer.setPhone(registrationRequest.getPhone());

        if(registrationRequest.getPassword().isBlank())
            throw new PasswordBlankException();

        customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        customer.setRole(registrationRequest.getRole());

        Customer result = customerRepository.save(customer);

        if(result != null && result.getId() > 0){
            requestResponse.setFirstName(customer.getFirstName());
            requestResponse.setLastName(customer.getLastName());
            requestResponse.setEmail(customer.getEmail());
            // option: we can generate a token for the end-user and access restricted pages immediately
            //requestResponse.setToken(jwtUtils.generateToken(customer));
            requestResponse.setMessage("Customer saved successfully.");
        }

        return requestResponse;
    }

    // signIn method
    public RequestResponse signIn(RequestResponse signinRequest){

        RequestResponse requestResponse = new RequestResponse();

        authenticationManager                                                   // TODO - manage exception for unauthorised access
                .authenticate(new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(), signinRequest.getPassword()));

        var customer = customerRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

        var jwt = jwtUtils.generateToken(customer);

        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), customer);

        requestResponse.setToken(jwt);
        requestResponse.setRefreshToken(refreshToken);
        requestResponse.setExpirationTime("24Hr");                              // TODO - consider sending in milliseconds instead
        requestResponse.setMessage("Signed in successfully");

        return requestResponse;
    }

    // refreshToken
    public RequestResponse refreshToken(RequestResponse refreshTokenRequest){

        RequestResponse requestResponse = new RequestResponse();

        String customerEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());

        Customer customer = customerRepository.findByEmail(customerEmail).orElseThrow();

        if(jwtUtils.isTokenValid(refreshTokenRequest.getToken(), customer)){

            var jwt = jwtUtils.generateToken(customer);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), customer);

            requestResponse.setToken(jwt);
            requestResponse.setRefreshToken(refreshToken);
            requestResponse.setExpirationTime("24Hr");
            requestResponse.setMessage("Refreshed token successfully");
        }

        return requestResponse;
    }

    // returning userProfile - when the user views his/her profile
    public RequestResponse profile(){

        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var customer = new Customer();
        customer = customerRepository.findByEmail(authentication.getName()).orElseThrow();

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setFirstName(customer.getFirstName());
        requestResponse.setLastName(customer.getLastName());
        requestResponse.setEmail(customer.getEmail());
        requestResponse.setPhone(customer.getPhone());
        // requestResponse.setPassword(customer.getPassword());

        return requestResponse;
    }

    // update userProfile
    public RequestResponse updateProfile(RequestResponse updateRequest){

        // the bearer token is passed in and used by authentication
        // to extract the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var customer = new Customer();
        customer = customerRepository.findByEmail(authentication.getName()).map(_customer->{
            _customer.setFirstName(updateRequest.getFirstName());
            _customer.setLastName(updateRequest.getLastName());
            _customer.setEmail(updateRequest.getEmail());
            _customer.setPhone(updateRequest.getPhone());
            _customer.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
            return customerRepository.save(_customer);
        }).orElseThrow();

         RequestResponse requestResponse = new RequestResponse();
         requestResponse.setMessage("Profile updated successfully.");
         requestResponse.setFirstName(customer.getFirstName());
         requestResponse.setLastName(customer.getLastName());
         requestResponse.setEmail(customer.getEmail());
         requestResponse.setPhone(customer.getPhone());
         requestResponse.setPassword(customer.getPassword());

        return requestResponse;
    }
}
