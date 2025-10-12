package com.firstproject.firstproject.controller;

import com.firstproject.firstproject.cache.AppCache;
import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs")
public class AdminController {
    @Autowired
    UserService userService;

    @Autowired
    AppCache appCache;

    @GetMapping("/getAllUsers")
    public ResponseEntity< List<User> > getAllUser() {
        try{
            List<User> all = userService.getUsers();
            if(!all.isEmpty()) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
            return new ResponseEntity<>(all, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /* only one admin can create another admin, first admin will be created manually */
    @PostMapping("/create-admin")
    public ResponseEntity<User> createAdminUser(@RequestBody User user) {
        try {
            userService.saveAdminUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("clear-app-cache")
    public void clearAppCache() {
        //clearing appCache and reInitializing if we have some new data in the database.
        appCache.init();
    }
}
