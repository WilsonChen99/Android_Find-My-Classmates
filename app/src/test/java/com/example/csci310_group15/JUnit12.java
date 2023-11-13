package com.example.csci310_group15;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JUnit12 {
    /* Testing "class" class : valid entry, different department */
    @Test
    public void validClassIsCorrect() {
        Class emptyClass  = new Class("5", "Aerospace and Mechanical Engineering", "AME-302", "Yang, Bingen", "TTH 14:00 - 15:20");
        assertEquals("id value empty string","5", emptyClass.getId());
        assertEquals("department value empty string","Aerospace and Mechanical Engineering", emptyClass.getDepartment());
        assertEquals("class num value empty string","AME-302", emptyClass.getNum());
        assertEquals("instructor value empty string","Yang, Bingen", emptyClass.getInstructor());
        assertEquals("time value empty string","TTH 14:00 - 15:20", emptyClass.getTime());
    }
}
