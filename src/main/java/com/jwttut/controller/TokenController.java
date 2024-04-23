package com.jwttut.controller;

import com.jwttut.helper.JwtUtil;
import com.jwttut.model.JwtRequest;
import com.jwttut.model.JwtResponse;
import com.jwttut.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/generateToken")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest);
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        }
        catch (UsernameNotFoundException exception){
            exception.printStackTrace();
            throw new Exception("Bad Credentials");
        }
        catch (BadCredentialsException exception){
            exception.printStackTrace();
            throw new Exception("Bad Credentials");
        }
        // this code will run when credentials match
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        System.out.println("JWT: " + token);

        return ResponseEntity.ok(new JwtResponse(token));

    }


}
