package com.firstproject.firstproject.service;

import com.firstproject.firstproject.repo.UserRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/* so the test first run the spring application so that the beans can initialize */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    UserRepo userRepo;

//    used to initialize something which is important for the test
//    @BeforeAll
//    @BeforeEach

//    used to do something after each test is over
//    @AfterAll
//    @AfterEach

    @Disabled
    @Test
    public void testFindByUserName() {
        assertNotNull(userRepo.findByUserName("divy12"));
    }

    @Disabled
    @ParameterizedTest
    @ValueSource( ints = {
            1,2,3,
            10,2,12,
            1, 2, 5
    })
    public void test(int a, int b, int expected) {
        assertEquals(expected, a+b);
    }

    @ParameterizedTest
    @CsvSource({
            "ram",
            "divy12",
            "originalAdmin",
            "shyam",
            "admin12"
    })
    public void testUsers(String name) {
        assertNotNull(userRepo.findByUserName(name), "failed for : " + name);
        // msg to see which test is failed
    }
}
