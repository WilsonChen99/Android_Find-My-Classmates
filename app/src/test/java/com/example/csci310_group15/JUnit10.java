package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit10 {
    /* Testing "class" class : valid entry */
    @Test
    public void validClassIsCorrect() {
        Class emptyClass  = new Class("3", "Computer Science", "CS104", "Andrew Goodney", "08:00 - 09:50");
        assertEquals("id value empty string","3", emptyClass.getId());
        assertEquals("department value empty string","Computer Science", emptyClass.getDepartment());
        assertEquals("class num value empty string","CS104", emptyClass.getNum());
        assertEquals("instructor value empty string","Andrew Goodney", emptyClass.getInstructor());
        assertEquals("time value empty string","08:00 - 09:50", emptyClass.getTime());
    }
}
