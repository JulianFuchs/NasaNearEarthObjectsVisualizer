
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate


class NearEarthObjectFetcherTests {

    class SplitDateRangeTests {
        @Test
        fun splitsSingleDay() {
            val date = LocalDate.of(2020, 1, 1)
            val intervals = splitDateRangeInto8DayIntervals(date, date)
            assertEquals(1, intervals.size)
            assertEquals(intervals[0].first, intervals[0].second)
            assertEquals(date, intervals[0].first)
        }

        @Test
        fun splitsLessThan8Days() {
            val from = LocalDate.of(2020, 1, 1)
            val to = from.plusDays(4)
            val intervals = splitDateRangeInto8DayIntervals(from, to)
            assertEquals(1, intervals.size)
            assertEquals(intervals[0].first, from)
            assertEquals(intervals[0].second, to)
        }

        @Test
        fun splitExactly8Days() {
            val from = LocalDate.of(2020, 3, 30)
            val to = from.plusDays(7)
            val intervals = splitDateRangeInto8DayIntervals(from, to)
            assertEquals(1, intervals.size)
            assertEquals(from, intervals[0].first)
            assertEquals(to, intervals[0].second)
        }

        @Test
        fun split2WeeksAndOneDay() {
            val from = LocalDate.of(2020, 6, 22)
            val to = LocalDate.of(2020, 7, 8)
            val intervals = splitDateRangeInto8DayIntervals(from, to)

            assertEquals(3, intervals.size)

            assertEquals(from, intervals[0].first)
            assertEquals(LocalDate.of(2020, 6, 29), intervals[0].second)

            assertEquals(LocalDate.of(2020, 6, 30), intervals[1].first)
            assertEquals(LocalDate.of(2020, 7, 7), intervals[1].second)

            assertEquals(LocalDate.of(2020, 7, 8), intervals[2].first)
            assertEquals(LocalDate.of(2020, 7, 8), intervals[2].second)
        }

        @Test
        fun split2AndAHalfWeeks() {
            val from = LocalDate.of(2020, 10, 5)
            val to = LocalDate.of(2020, 10, 26)
            val intervals = splitDateRangeInto8DayIntervals(from, to)

            assertEquals(3, intervals.size)

            assertEquals(from, intervals[0].first)
            assertEquals(LocalDate.of(2020, 10, 12), intervals[0].second)

            assertEquals(LocalDate.of(2020, 10, 13), intervals[1].first)
            assertEquals(LocalDate.of(2020, 10, 20), intervals[1].second)

            assertEquals(LocalDate.of(2020, 10, 21), intervals[2].first)
            assertEquals(LocalDate.of(2020, 10, 26), intervals[2].second)
        }

    }

}