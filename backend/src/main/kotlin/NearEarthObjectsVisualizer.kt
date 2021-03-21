import java.time.LocalDate
import java.util.logging.Level

val nearEarthObjectsFetcher = NearEarthObjectsFetcher()

// todo: split logic out of this function, so the finding smallest object function can be tested
fun findClosestObjectToEarthBetweenDates(fromDate: LocalDate, toDate: LocalDate): NearEarthObject? {

    val mapDateToNearEarthObjects = if(InMemoryCache.containsDateRange(fromDate, toDate)) {
        InMemoryCache.getCachedData(fromDate, toDate)
    } else {
        nearEarthObjectsFetcher.fetchNearEarthObjectsFromNasa(fromDate, toDate)
            .also { InMemoryCache.updateCache(it, fromDate, toDate) }
    }

    var closestNearEarthObject: NearEarthObject? = null

    for ((_, listOfNearEarthObjects) in mapDateToNearEarthObjects) {
        for (nearEarthObject in listOfNearEarthObjects) {
            if (closestNearEarthObject === null) {
                closestNearEarthObject = nearEarthObject
            } else {
                if (nearEarthObject.missDistanceKm < closestNearEarthObject.missDistanceKm) {
                    closestNearEarthObject = nearEarthObject
                }
            }
        }
    }

    return closestNearEarthObject
}

