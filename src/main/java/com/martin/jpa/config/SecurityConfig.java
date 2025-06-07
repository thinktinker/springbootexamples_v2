package com.martin.jpa.config;


import com.martin.jpa.service.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final CustomerDetailsService customerDetailsService;
    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    // authenticates users using data access object (DAO) to retrieve user details
    // returns the user info. through a) CustomerDetailsService > CustomerRepository > DB connection
    // inject CustomerDetailsService (through constructor injection)
    public SecurityConfig(CustomerDetailsService customerDetailsService) {
        this.customerDetailsService = customerDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // csrf (prevent cross-site request forgery)
        // currently disabled, to update once a client server is established
        // e.g. https://www.frontendserver.net -> https://backendapp.azure.net

        // specify the security filters meant for authentication and authorization routes
        // return the built security configuration

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(request ->
                request.requestMatchers("/auth/api/**", "/public/api/**").permitAll()               // public pages or login/registration page
                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")                   // ADMIN role
                .requestMatchers("/user/**").hasAnyAuthority("USER")                     // USER role
                .requestMatchers("/restricted/**").hasAnyAuthority("USER", "ADMIN")      // USER and ADMIN role
                .anyRequest().authenticated())
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider()).addFilterBefore(
                jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
        );

        return httpSecurity.build();
    }

    // instantiate DaoAuthenticationProvider by providing CustomerDetailService in the constructor
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customerDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    // return a new instance of a BCryptPasswordEncoder to secure encoded passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // returns the AuthenticationManager bean for handling user authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
