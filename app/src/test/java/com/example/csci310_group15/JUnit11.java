package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit11 {
    /* Testing "class" class : valid entry, large id */
    @Test
    public void validClassIsCorrect() {
        Class emptyClass  = new Class("1525252525", "Computer Science", "CS270", "Aaron Cote", "14:00 - 15:50");
        assertEquals("id value empty string","1525252525", emptyClass.getId());
        assertEquals("department value empty string","Computer Science", emptyClass.getDepartment());
        assertEquals("class num value empty string","CS270", emptyClass.getNum());
        assertEquals("instructor value empty string","Aaron Cote", emptyClass.getInstructor());
        assertEquals("time value empty string","14:00 - 15:50", emptyClass.getTime());
    }
}
