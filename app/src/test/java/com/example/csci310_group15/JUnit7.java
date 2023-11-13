package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit7 {
    /* Testing "class" class : empty entry */
    @Test
    public void emptyClassIsCorrect() {
        Class emptyClass  = new Class();
        assertEquals("id value empty string","", emptyClass.getId());
        assertEquals("department value empty string","", emptyClass.getDepartment());
        assertEquals("class num value empty string","", emptyClass.getNum());
        assertEquals("instructor value empty string","", emptyClass.getInstructor());
        assertEquals("time value empty string","", emptyClass.getTime());
    }
}
