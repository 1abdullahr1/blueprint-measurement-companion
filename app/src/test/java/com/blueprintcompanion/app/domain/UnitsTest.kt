package com.blueprintcompanion.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UnitsTest {

    @Test
    fun areaAndPerimeterFromLengthAndWidth() {
        val result = calculate(15.0, 12.0, null)
        assertEquals(180.0, result.areaSqFt!!, 0.001)
        assertEquals(54.0, result.perimeterFt!!, 0.001)
        assertNull(result.volumeCuFt)
    }

    @Test
    fun volumeNeedsHeight() {
        val result = calculate(15.0, 12.0, 8.0)
        assertEquals(1440.0, result.volumeCuFt!!, 0.001)
    }

    @Test
    fun metersConvertToFeet() {
        val feet = toFeet(1.0, LengthUnit.METERS)
        assertEquals(3.280839895, feet, 0.0001)
    }

    @Test
    fun inchesConvertToFeet() {
        assertEquals(1.0, toFeet(12.0, LengthUnit.INCHES), 0.0001)
    }

    @Test
    fun missingWidthOmitsArea() {
        val result = calculate(10.0, null, 8.0)
        assertNull(result.areaSqFt)
        assertNull(result.volumeCuFt)
    }
}
