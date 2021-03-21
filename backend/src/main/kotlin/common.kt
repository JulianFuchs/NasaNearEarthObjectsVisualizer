import nasaNeoApiData.CloseApproachData
import nasaNeoApiData.NasaNeoData
import java.time.LocalDate

fun NasaNeoData.getCloseApproachDataForNearestMiss(): CloseApproachData {
    var nearestMissCloseApproachData = this.close_approach_data[0]
    for (closeApproachData in this.close_approach_data) {
        val currentMissDistance = nearestMissCloseApproachData.miss_distance.kilometers.toDouble()
        val newMissDistance = closeApproachData.miss_distance.kilometers.toDouble()
        if (newMissDistance < currentMissDistance) {
            nearestMissCloseApproachData = closeApproachData
        }
    }
    return nearestMissCloseApproachData
}

fun NasaNeoData.getEstimatedDiameterMean(): Double {
    val diameterKilometers = estimated_diameter.kilometers
    return (diameterKilometers.estimated_diameter_min + diameterKilometers.estimated_diameter_max)/2
}

/*
Nasa Neo api only returns data for ranges for a maximum of 8 days, so we need to split the date range
into 8 day intervals
*/
fun splitDateRangeInto8DayIntervals(from: LocalDate, to: LocalDate): List<Pair<LocalDate, LocalDate>> {

    val intervals = mutableListOf<Pair<LocalDate, LocalDate>>()

    var intervalStart = from
    var intervalEnd = intervalStart.plusDays(7)

    while (intervalEnd.isBefore(to)) {
        intervals.add(Pair(intervalStart, intervalEnd))
        intervalStart = intervalEnd.plusDays(1)
        intervalEnd = intervalStart.plusDays(7)
    }

    intervals.add(Pair(intervalStart, to))

    return intervals
}