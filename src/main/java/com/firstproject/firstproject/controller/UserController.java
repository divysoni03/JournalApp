package com.firstproject.firstproject.controller;

import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity< List<User> > findAll() {
        try {
            List<User> users = userService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editJournal(@RequestBody User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();

            User oldUser = userService.findByUserName(userName);
            if(oldUser != null) {
                // Update username if provided
                if(user.getUserName() != null && !user.getUserName().isEmpty()) {
                    oldUser.setUserName(user.getUserName());
                }
                
                // Update password if provided
                if(user.getPassword() != null && !user.getPassword().isEmpty()) {
                    oldUser.setPassword(user.getPassword()); // Password will be encrypted in UserService.saveUser()
                }

                userService.saveNewUser(oldUser); // This will encrypt the password
                return new ResponseEntity<>(oldUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User oldUser = userService.findByUserName(userName);
        if(oldUser != null) {
            userService.deleteByid(oldUser.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
