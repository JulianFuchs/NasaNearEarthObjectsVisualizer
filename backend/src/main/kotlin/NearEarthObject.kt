

data class NearEarthObject(
    val id: String,
    val name: String,
    val missDistanceKm: Double,
    val closeApproachDate: String,
    val closeAppraochFullDate: String,
    val orbiting_body: String,
    val relativeVelocityKmPerSec: Double,
    val estimatedDiameterMeanKm: Double,
    val nasaJplUrl: String
)
