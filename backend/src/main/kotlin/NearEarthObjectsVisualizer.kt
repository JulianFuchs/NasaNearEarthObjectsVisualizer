import java.time.LocalDate

fun findClosestNearEarthObjectToEarth(fromDate: LocalDate, toDate: LocalDate): NearEarthObject {
    return findOptimizedNearEarthObject(fromDate, toDate, ::getCloserNearEarthObject)
}

fun findLargestNearEarthObject(fromDate: LocalDate, toDate: LocalDate): NearEarthObject {
    return findOptimizedNearEarthObject(fromDate, toDate, ::getBiggerNearEarthObject)
}

private fun getCloserNearEarthObject(o1: NearEarthObject, o2: NearEarthObject): NearEarthObject {
    return if (o1.missDistanceKm <= o2.missDistanceKm) {
        o1
    } else {
        o2
    }
}

private fun getBiggerNearEarthObject(o1: NearEarthObject, o2: NearEarthObject): NearEarthObject {
    return if (o1.estimatedDiameterMeanKm >= o2.estimatedDiameterMeanKm) {
        o1
    } else {
        o2
    }
}

private fun findOptimizedNearEarthObject(
    fromDate: LocalDate,
    toDate: LocalDate,
    optimizeForFunc: (NearEarthObject, NearEarthObject) -> NearEarthObject
): NearEarthObject {

    val mapDateToNearEarthObjects = readNearEarthObjectsDataFromCacheOrFetchFromApi(fromDate, toDate)

    var minNearEarthObject: NearEarthObject? = null

    for ((_, listOfNearEarthObjects) in mapDateToNearEarthObjects) {
        for (nearEarthObject in listOfNearEarthObjects) {
            minNearEarthObject = if (minNearEarthObject === null) {
                nearEarthObject
            } else {
                optimizeForFunc(minNearEarthObject, nearEarthObject)
            }
        }
    }

    return minNearEarthObject!!
}


private fun readNearEarthObjectsDataFromCacheOrFetchFromApi(
    fromDate: LocalDate,
    toDate: LocalDate
): Map<LocalDate, List<NearEarthObject>> {

    return if (InMemoryCache.containsDateRange(fromDate, toDate)) {
        InMemoryCache.getCachedData(fromDate, toDate)
    } else {
        NearEarthObjectsFetcher.fetchNearEarthObjectsFromNasa(fromDate, toDate)
            .also { InMemoryCache.updateCache(it, fromDate, toDate) }
    }
}

