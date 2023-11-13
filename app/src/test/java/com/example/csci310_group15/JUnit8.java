package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit8 {
    /* Testing "class" class : valid entry */
    @Test
    public void validClassIsCorrect() {
        Class emptyClass  = new Class("1", "Computer Science", "CS310", "Chao Wang", "10:00 - 11:50");
        assertEquals("id value empty string","1", emptyClass.getId());
        assertEquals("department value empty string","Computer Science", emptyClass.getDepartment());
        assertEquals("class num value empty string","CS310", emptyClass.getNum());
        assertEquals("instructor value empty string","Chao Wang", emptyClass.getInstructor());
        assertEquals("time value empty string","10:00 - 11:50", emptyClass.getTime());
    }
}
