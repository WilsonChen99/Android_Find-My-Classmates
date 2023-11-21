package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit14 {
    /** A JUnit test method to test the User setters */
    @Test
    public void setUser() {
        User user = new User();
        user.setName("Jesse");
        user.setEmail("jesse@science.org");
        user.setStanding("undergrad");
        user.setUri("bbb");
        user.setUscID("321");
        user.setUid("111");
        assertEquals("name value Jesse","Jesse", user.getName());
        assertEquals("email value jesse@science.org","jesse@science.org", user.getEmail());
        assertEquals("standing value undergrad","undergrad", user.getStanding());
        assertEquals("uri value bbb","bbb", user.getUri());
        assertEquals("usc id value 321","321", user.getUscID());
        assertEquals("uid value 111","111", user.getUid());
    }
}
