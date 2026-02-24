package io.github.kawazaki42.course.classgui

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ComplexTest {
    @Test
    fun equals() {
        assertEquals(
            Complex(2.0, 3.0),
            Complex(2.0, 3.0),
        )

        assertNotEquals(
            Complex(1.0, 2.0),
            Complex(1.0, 3.0),
        )
    }

    @Test
    fun init() {
        val c = Complex(3.0, 4.0)

        assertEquals(3.0,c.real)
        assertEquals(4.0, c.imag)
    }

    @Test
    fun init_omit() {
        val c = Complex(imag = 6.7)

        assertEquals(0.0, c.real)
        assertEquals(6.7, c.imag)
    }

    @Test
    fun plus() {
        val a = Complex(3.0, 2.0)
        val b = Complex(1.0, 7.0)

        val actual = a + b
        val expected = Complex(4.0, 9.0)

        assertEquals(expected, actual)
    }

    @Test
    fun plus_double() {
        val actual = Complex(3.0, 2.0) + 5.0
        val expected = Complex(8.0, 2.0)

        assertEquals(expected, actual)
    }

    @Test
    fun minus() {
        val a = Complex(5.0, 3.0)
        val b = Complex(2.0, 1.0)

        val actual = a - b
        val expected = Complex(3.0, 2.0)

        assertEquals(expected, actual)
    }

    @Test
    fun times() {
        val a = Complex(2.0, 3.0)
        val b = Complex(4.0, 5.0)

        val actual = a * b
        val expected = Complex(
            2.0 * 4.0 - 3.0 * 5.0,
            2.0 * 5.0 + 3.0 * 4.0,
        )
        val expected_static = Complex(-7.0, 22.0)

        assertEquals(expected, actual)
        assertEquals(expected_static, actual)
    }

    @Test
    fun times_double() {
        val actual = Complex(3.0, -5.0) * -1.0
        val expected = Complex(-3.0, 5.0)

        assertEquals(expected, actual)
    }

    @Test
    fun abs() {
        val actual = Complex(3.0, 4.0).abs()
        val expected = 5.0

        assertEquals(expected, actual)

        assertEquals(0.0, Complex().abs())
        assertEquals(+5.0, Complex(-5.0).abs())
    }

    @Test
    fun arg() {
        assertEquals(kotlin.math.PI/4.0, Complex(1.0, 1.0).arg())
        assertEquals(kotlin.math.PI, Complex(-1.0).arg())
        assertEquals(0.0, Complex().arg())
    }
}