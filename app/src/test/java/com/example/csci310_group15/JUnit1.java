package com.example.csci310_group15;

import org.junit.Test;
import static org.junit.Assert.*;

public class JUnit1 {
    /** A JUnit test method to test the Message class */
    @Test
    public void testMessage() {
        Message message1 = new Message();
        assertEquals("null message",null, message1.message);
        assertEquals("null uid", null, message1.uid);

        Message message2 = new Message("Hey", "123");
        assertEquals("message value Hey", "Hey", message2.message);
        assertEquals("uid value 123", "123", message2.uid);
    }
}
