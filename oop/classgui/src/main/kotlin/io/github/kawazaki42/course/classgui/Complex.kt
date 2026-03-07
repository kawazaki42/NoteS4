package io.github.kawazaki42.course.classgui

import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.sign

data class Complex(val real: Double = 0.0, val imag: Double = 0.0) {

    override fun toString(): String {
        val imagSigned = if (imag < 0)
            "- ${imag.absoluteValue}"
        else
            "+ $imag"

        return "$real ${imagSigned}i"
    }

    operator fun plus(other: Complex) = Complex(
        real + other.real,
        imag + other.imag,
    )

    operator fun plus(other: Double) = copy(real = real + other)

    operator fun minus(other: Complex) = Complex(
        real - other.real,
        imag - other.imag
    )

    operator fun minus(other: Double) = copy(real = real - other)

//    operator fun times(other: Complex): Complex {
//        val real = this.real * other.real - this.imag * other.imag
//        val imag = this.real * other.imag + this.imag * other.real
//
//        return Complex(real, imag)
//    }

    operator fun times(other: Double) = Complex(
        real * other,
        imag * other,
    )

    operator fun times(other: Complex) = Complex(
        this.real * other.real - this.imag * other.imag,
        this.real * other.imag + this.imag * other.real,
    )

    fun abs() = hypot(real, imag)
    fun arg() = atan2(imag, real)
}