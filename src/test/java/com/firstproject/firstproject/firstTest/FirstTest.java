package com.firstproject.firstproject.firstTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/*
* which function is for testing then annotate that function with @Test annotation
* and add the assert____ function which is used to check ig*/

public class FirstTest {
    @Test
    public void testAdd() {
        assertEquals(4, 2+2);
    }
}
