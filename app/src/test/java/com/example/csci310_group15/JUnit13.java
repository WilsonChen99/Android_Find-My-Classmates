package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit13 {
    /** A JUnit test method to test the Rating setters */
    @Test
    public void setRating() {
        Rating rating = new Rating();
        rating.setWorkload("Heavy");
        rating.setScore("1");
        rating.setAttendance("Yes");
        rating.setLateHW("Yes");
        rating.setComment("cookie");
        rating.setVote(1);
        assertEquals("workload value Heavy","Heavy", rating.getWorkload());
        assertEquals("score value 1","1", rating.getScore());
        assertEquals("attendance value Yes","Yes", rating.getAttendance());
        assertEquals("lateHW value Yes","Yes", rating.getLateHW());
        assertEquals("comment value cookie","cookie", rating.getComment());
        assertEquals("vote value 1",1, rating.getVote());
    }
}
