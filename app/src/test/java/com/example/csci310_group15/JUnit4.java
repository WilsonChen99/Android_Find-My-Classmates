package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;

public class JUnit4 {
    /** A JUnit test method to test the ChatAdapter class */
    @Test
    public void testEmptyChatAdapter() {
        ChatAdapter chatAdapter = new ChatAdapter( new ArrayList<>());
        assertEquals("Empty adapter", 0, chatAdapter.getItemCount());
    }
}
