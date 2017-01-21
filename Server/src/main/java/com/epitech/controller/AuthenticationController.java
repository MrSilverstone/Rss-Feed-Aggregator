package com.epitech.controller;

import com.epitech.Service.UserDetailsServiceImpl;
import com.epitech.model.User;
import com.epitech.model.requests.AuthenticationRequest;
import com.epitech.model.requests.AuthenticationResponse;
import com.epitech.model.requests.RegisterResponse;
import com.epitech.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.epitech.utils.JwtUtils.generateHMACToken;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     *  Authenticate user to get token
     *  @param authenticationRequest request body
     *  @return OK: JSON object with token
     *  @see com.epitech.model.requests.AuthenticationRequest for body structure
     *  @see com.epitech.model.requests.AuthenticationResponse for response structure
     * */
    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest)
            throws AuthenticationException, IOException, JOSEException {

        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        // throws authenticationException if it fails !
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String secret = "ksfdlkvopiurutueijflkdsvf,cjnjnxjnvsoifoiropiezaropioezkvf,k,c kv,ckvdkfjgvorieoigfopziefpozepfiezikfozkfldsmvflkvdldvl,fdvk,fdkv,dkfgkjfdgkjvicooiviuzieopiztpoirpotimldkflg,vlkfckjshayauiueruergregierpogipdfogiklxvcjnxvjnfvjdsmkfmslfklgkfgoirgjitrooritjg";
        int expirationInMinutes = 24*60;

        String token = generateHMACToken(username, authentication.getAuthorities(), secret, expirationInMinutes);

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    /**
     *  Register user
     *  @param authenticationRequest request body
     *  @return OK, otherwise BAD_REQUEST
     *  @see com.epitech.model.requests.AuthenticationRequest for body structure
     * */
    @RequestMapping(value = "/register", method = POST)
    public ResponseEntity<RegisterResponse> registerRequest(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
	
        RegisterResponse registerResponse = new RegisterResponse();
	
        if (username.isEmpty() || password.isEmpty()){
            registerResponse.setSuccess(false);
            registerResponse.setMessage("Invalid credentials");
            return  new ResponseEntity<RegisterResponse>(registerResponse, HttpStatus.OK);
        }
	
        User u = userRepository.findByEmail(username);
        if (u != null) {
            registerResponse.setSuccess(false);
            registerResponse.setMessage("Email already taken");
            return  new ResponseEntity<RegisterResponse>(registerResponse, HttpStatus.OK);
        }

        userDetailsService.createUser(username, password);
	
        registerResponse.setSuccess(true);
        registerResponse.setMessage("Success");
	
        return new ResponseEntity<>(registerResponse, HttpStatus.OK);
    }
}
