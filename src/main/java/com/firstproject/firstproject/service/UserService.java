package com.firstproject.firstproject.service;

import com.firstproject.firstproject.entity.User;
import com.firstproject.firstproject.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userRepo.save(user);
    }
    public void saveUser(User user) {
        userRepo.save(user);
    }
    public void saveAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN")); //saving both user and admin so he/she can test other apis
        userRepo.save(user);
    }

    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void deleteByid(ObjectId id) {
        userRepo.deleteById(id);
    }

    public User findByUserName(String userName) {
        return userRepo.findByUserName(userName);
    }
}
