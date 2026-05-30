package io.github.kawazaki42.course.classgui

import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.hypot

/** A complex number. <https://en.wikipedia.org/wiki/Complex_number> */
data class Complex(
    /** Real part. */
    val real: Double = 0.0,
    /** Imaginary part. */
    val imag: Double = 0.0,
) {
    override fun toString(): String {
        val imagSigned = if (imag < 0)
            "- ${imag.absoluteValue}"
        else
            "+ $imag"

        return "$real ${imagSigned}i"
    }

    /** Add operator for complex numbers. */
    operator fun plus(other: Complex) = Complex(
        real + other.real,
        imag + other.imag,
    )

    /** Add operator for real numbers. */
    operator fun plus(other: Double) = copy(real = real + other)

    /** Substract operator for complex numbers. */
    operator fun minus(other: Complex) = Complex(
        real - other.real,
        imag - other.imag
    )

    /** Substract operator for real numbers. */
    operator fun minus(other: Double) = copy(real = real - other)

//    operator fun times(other: Complex): Complex {
//        val real = this.real * other.real - this.imag * other.imag
//        val imag = this.real * other.imag + this.imag * other.real
//
//        return Complex(real, imag)
//    }

    /** Multiply operator for real numbers. */
    operator fun times(other: Double) = Complex(
        real * other,
        imag * other,
    )

    /** Multiply operator for complex numbers. */
    operator fun times(other: Complex) = Complex(
        this.real * other.real - this.imag * other.imag,
        this.real * other.imag + this.imag * other.real,
    )

    /** Absolute value (length of a vector). */
    fun abs() = hypot(real, imag)

    /** Argument (angle between _real_ axis and the vector). */
    fun arg() = atan2(imag, real)
}