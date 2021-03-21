
fun mockNearEarthObject(
    id: String = "1",
    missDistance: Double = 1000000.333,
    approachDate: String = "2020-01-01",
    estimatedDiameterMeanKm: Double = 0.0999
): NearEarthObject {
    return NearEarthObject(
        id,
        id,
        missDistance,
        approachDate,
        "$approachDate 15:00",
        "Earth",
        50000.329478778,
        estimatedDiameterMeanKm,
        "http://ssd.jpl.nasa.gov/sbdb.cgi?sstr=$id"
    )
}
