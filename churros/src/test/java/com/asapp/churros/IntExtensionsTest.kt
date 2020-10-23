package com.asapp.churros

import org.junit.Assert.assertEquals
import org.junit.Test

class IntExtensionsTest {
    @Test
    fun testToOrdinalNumeral() {
        assertEquals("0th", 0.toOrdinalNumeral())
        assertEquals("1st", 1.toOrdinalNumeral())
        assertEquals("2nd", 2.toOrdinalNumeral())
        assertEquals("3rd", 3.toOrdinalNumeral())
        assertEquals("4th", 4.toOrdinalNumeral())
        assertEquals("11th", 11.toOrdinalNumeral())
        assertEquals("12th", 12.toOrdinalNumeral())
        assertEquals("13th", 13.toOrdinalNumeral())
        assertEquals("21st", 21.toOrdinalNumeral())
        assertEquals("22nd", 22.toOrdinalNumeral())
        assertEquals("23rd", 23.toOrdinalNumeral())
    }
}
