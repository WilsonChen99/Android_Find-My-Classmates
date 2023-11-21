package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit15 {
    /** A JUnit test method to test the Message setters */
    @Test
    public void testMessage() {
        Message message = new Message();
        message.message = "Sup";
        message.uid = "321";
        assertEquals("message value Sup", "Sup", message.message);
        assertEquals("uid value 321", "321", message.uid);
    }
}
