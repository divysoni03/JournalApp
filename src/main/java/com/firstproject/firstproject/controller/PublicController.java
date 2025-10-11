package com.firstproject.firstproject.controller;

import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.service.QuoteService;
import com.firstproject.firstproject.service.UserDetailsServiceImpl;
import com.firstproject.firstproject.service.UserService;
import com.firstproject.firstproject.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    UserService userService;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{
            /* step.1 this authentication manager internally uses userDetailsServiceImpl to verify the user */
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

            /* step.2 get user details */
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());

            /* step.3 make JWT token with help of this userDetails */
            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            return new ResponseEntity<String>(jwt, HttpStatus.OK);

        } catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Incorrect Username of Password", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/greet")
    public ResponseEntity<?> greetings() {
        return new ResponseEntity<>("Hi " + "Quote of the day :" +quoteService.getQuotes(), HttpStatus.OK);
    }
}
