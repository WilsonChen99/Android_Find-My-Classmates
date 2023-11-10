package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit5 {
    /** A JUnit test method to test the User class */
    @Test
    public void testUser() {
        // Complete constructor
        User user2 = new User("Walter", "walter@science.org", "grad", "aaa", "123", "000");
        assertEquals("name value Walter","Walter", user2.getName());
        assertEquals("email value walter@science.org","walter@science.org", user2.getEmail());
        assertEquals("standing value grad","grad", user2.getStanding());
        assertEquals("uri value aaa","aaa", user2.getUri());
        assertEquals("usc id value 123","123", user2.getUscID());
        assertEquals("uid value 000","000", user2.getUid());
    }
}
