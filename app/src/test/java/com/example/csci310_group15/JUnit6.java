package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit6 {
    /** A JUnit test method to test the Rating class */
    @Test
    public void testRating() {
        // Complete constructor
        Rating rating2 = new Rating("Yes", "5", "No", "No", "bruh", 0);
        assertEquals("workload value Yes","Yes", rating2.getWorkload());
        assertEquals("score value 5","5", rating2.getScore());
        assertEquals("attendance value No","No", rating2.getAttendance());
        assertEquals("lateHW value No","No", rating2.getLateHW());
        assertEquals("comment value bruh","bruh", rating2.getComment());
        assertEquals("vote value 0",0, rating2.getVote());
    }
}
