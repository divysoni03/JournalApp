package com.firstproject.firstproject.controller;

import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.service.QuoteService;
import com.firstproject.firstproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    UserService userService;

    @Autowired
    private QuoteService quoteService;

    @PostMapping("/add")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/greet")
    public ResponseEntity<?> greetings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>("Hi " + auth.getName() + ", Quote of the day :" +quoteService.getQuote(), HttpStatus.OK);
    }
}
