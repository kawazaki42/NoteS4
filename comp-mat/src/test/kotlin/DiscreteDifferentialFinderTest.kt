import io.github.kawazaki42.course.differential.DiscreteDifferentialFinder
import org.junit.jupiter.api.Test

class DiscreteDifferentialFinderTest {
    @Test
    fun getFiniteDifferenceSequence() {
        val f = DiscreteDifferentialFinder(1.0, 2.0, listOf(2.0, 7.0, -1.0, 3.0))
//        f.finiteDifferencesByOrder.also(::println)
        val actual = f.finiteDifferenceSequence.toList()
        val expected = listOf(
            listOf(2.0, 7.0, -1.0, 3.0),
            listOf(5.0, -8.0, 4.0),
            listOf(-13.0, 12.0),
            listOf(25.0),
        )
    }

}