package com.firstproject.firstproject.service;

import com.firstproject.firstproject.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/*-> when we are having big application then initializing beans and making connections will take so much time and we can't waste that much time during testing
 	that's why we have Mockito. in this type of testing we return dummy data in any function and we can test particular part in the function
	if we are getting the user from the database and building the user using userDetails(in security) then we can skip the main part where we fetch the user from database

-> first do all the things we did in JUnit

- User.builder() is to build a new user which we will return
- where ArgumentMatchers.anyString() : used when we want to return the object to all the callers
- we can also pass any specific value then the object will be return to that specific argument
*/

@SpringBootTest
public class UserDetailsServiceImplTests {

    @InjectMocks // will insert every dependency which are in userDetailsServiceImpl
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsernameTests() {
        UserDetails userEntity = User.builder()
                .username("ram")
                .password("asehfloisuehf;oasihepq8wah")
                .build();

//        when(userRepo.findByUserName("ram")).thenReturn(userEntity); // Must return User, not UserDetails

        UserDetails userDetails = userDetailsService.loadUserByUsername("ram");

        assertNotNull(userDetails);
    }


}
