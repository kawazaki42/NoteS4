package io.github.kawazaki42.course.anneal

import kotlin.math.exp
import kotlin.random.Random.Default.nextDouble

class AnnealSimulator(
    val f: (List<Double>) -> Double,
    val moveRadius: Double,
    var curPoint: List<Double>,
    var temperature: Double,
    val stopTemperature: Double,
): Iterator<List<Double>> {
    override fun next(): List<Double> {
        val maybeNext = curPoint.map {
            it + nextDouble(-moveRadius, moveRadius)
        }

        val dy = f(maybeNext) - f(curPoint)

        if (dy < 0) {
            curPoint = maybeNext
        } else {
            val p = exp(-dy / temperature)

            if (nextDouble() <= p)
                curPoint = maybeNext
        }

        temperature *= 0.999

        return curPoint
    }

    override fun hasNext() = temperature > stopTemperature
}