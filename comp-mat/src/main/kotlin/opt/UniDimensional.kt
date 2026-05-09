package io.github.kawazaki42.opt

const val DEFAULT_PRECISION = 1e-6

typealias Range = Pair<Double, Double>

val Pair<Double, Double>.avg get() = (first + second) / 2

fun Pair<Double, Double>.sorted() = if (first < second) this else Pair(second, first)

fun Triple<Double, Double, Double>.isHump() = second >= first && second >= third
fun Triple<Double, Double, Double>.isPit() = second <= first && second <= third
fun Triple<Double, Double, Double>.isIncreasing() = second in first..third
fun Triple<Double, Double, Double>.isDecreasing() = second in third..first

fun Triple<Double, Double, Double>.map(transform: (Double) -> Double) = Triple(
    transform(first),
    transform(second),
    transform(third)
)

//class UniDimensional(
//    val x0: Double = 0.0,
//    val step: Double = 1.0,
//    val epsilon: Double = DEFAULT_PRECISION,
//    val f: (Double) -> Double,
//) {
//    fun localize(): Sequence<Triple<Double, Double, Double>> {
//
//    }
//}

fun seekSequence(
    x0: Double,
    step: Double,
    f: (Double) -> Double,
): Sequence<Triple<Double, Double, Double>> {
    val x = Triple(
        x0 - step,
        x0,
        x0 + step,
    )

    val y = x.map(f)

    require(!x.isHump()) {
        "function must be unimodal"
    }

    var step = step

    //        isDecreasing(a, b, c) -> x = x0 = 2.pow(k-1) * d
//        y.isPit() -> return Pair(x.first, x.third)
    if (y.isIncreasing()) step *= -1
//        y.isDecreasing() -> Unit

//    val (r, c, l) =
    return generateSequence(x) { x ->
        if (x.map(f).isPit()) return@generateSequence null
        Triple(x.second, x.third, x.third + step).also {
            step *= 2
        } // .also(::println)
    }

//    return Pair(minOf(l, r), maxOf(l, r))
}

fun seek(
    x0: Double,
    step: Double,
    f: (Double) -> Double,
) = seekSequence(x0, step, f)
    .last()
    .let { (l, c, r) ->
        Pair(l, r).sorted()
    }

fun confine(
    range: Pair<Double, Double>,
    epsilon: Double,
    f: (Double) -> Double,
) = generateSequence(range) { bounds ->
    if (bounds.second - bounds.first <= 2 * epsilon)
        return@generateSequence null

    val avg = bounds.avg
    val mid = Triple(
        Pair(bounds.first, avg).avg,
        avg,
        Pair(avg, bounds.second).avg,
    )

    val y = mid.map(f)

    val (l, r) = bounds

    when {
        y.isPit() -> Pair(mid.first, mid.third)
        y.isDecreasing() -> Pair(mid.second, r)
        y.isIncreasing() -> Pair(l, mid.second)
        else -> error(y.toString())
    }
}

fun findMin(
    range: Range,
    epsilon: Double,
    f: (Double) -> Double,
) = confine(range, epsilon, f)
    .last()
    .avg

fun findMin(
    begin: Double = 0.0,
    step: Double = 1.0,
    epsilon: Double = 1e-6,
    f: (Double) -> Double,
) = findMin(seek(begin, step, f), epsilon, f)
