package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit2 {
    /** A JUnit test method to test the User class */
    @Test
    public void testUserEmpty() {
        // Empty constructor
        User user1 = new User();
        assertEquals("null name",null, user1.getName());
        assertEquals("null email",null, user1.getEmail());
        assertEquals("null standing",null, user1.getStanding());
        assertEquals("null uri",null, user1.getUri());
        assertEquals("null usc id",null, user1.getUscID());
        assertEquals("null uid",null, user1.getUid());
    }
}
