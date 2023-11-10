package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit3 {
    /** A JUnit test method to test the Rating class */
    @Test
    public void testRatingEmpty() {
        // Empty constructor
        Rating rating1 = new Rating();
        assertEquals("null workload",null, rating1.getWorkload());
        assertEquals("null score",null, rating1.getScore());
        assertEquals("null attendance",null, rating1.getAttendance());
        assertEquals("null lateHW",null, rating1.getLateHW());
        assertEquals("null comment",null, rating1.getComment());
        assertEquals("0 vote",0, rating1.getVote());
    }
}
