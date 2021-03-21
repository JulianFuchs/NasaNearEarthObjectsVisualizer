import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class NearEarthObjectsVisualizerTests {

    @Test
    fun testFetchesNearest() {
        val testData = mutableMapOf<LocalDate, List<NearEarthObject>>()

        val date = LocalDate.of(2020,1, 1)

        val o1 = mockNearEarthObject(id = "1", missDistance = 1000000.24118616)
        val o2 = mockNearEarthObject(id = "2", missDistance = 2000000.24118616)
        val o3 = mockNearEarthObject(id = "3", missDistance = 3000000.24118616)

        testData[date] = arrayListOf(o1, o2, o3)

        InMemoryCache.updateCache(testData, date, date)

        val closest = findClosestNearEarthObjectToEarth(date, date)

        assertEquals(o1, closest)
    }

    // todo: add more tests
}