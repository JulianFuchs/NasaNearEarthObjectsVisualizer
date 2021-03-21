import com.beust.klaxon.Klaxon
import nasaNeoApiData.CloseApproachData
import nasaNeoApiData.NasaNeoApiData
import nasaNeoApiData.NasaNeoData
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.LocalDate
import java.util.logging.Level

class NearEarthObjectsFetcher {

    private val nasaNeoFeedUrl = "https://api.nasa.gov/neo/rest/v1/feed"
    // todo: (opt) read this from an environment variable
    private val apiKey = "ik3yhAzzg4zF6zYtBlry5Ti6NhIDxbAEdaSF4h8I"

    private val httpClient: HttpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(30))
        .build()

    fun fetchNearEarthObjectsFromNasa(from: LocalDate, to: LocalDate): Map<LocalDate, List<NearEarthObject>> {

        logger.log(Level.INFO, "Fetching data from Nasa NEO Api for date range $from to $to" )

        val allMaps = mutableMapOf<LocalDate, List<NearEarthObject>>()

        val intervals = splitDateRangeInto8DayIntervals(from, to)

        for (interval in intervals) {
            val map = callNasaNeoApi(interval.first, interval.second)
            allMaps.putAll(map)
        }

        return allMaps
    }

    private fun callNasaNeoApi(from: LocalDate, to: LocalDate): Map<LocalDate, List<NearEarthObject>> {
        val uri = URI.create("$nasaNeoFeedUrl?start_date=$from&end_date=$to&api_key=$apiKey")

        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(uri)
            .build()

        val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() != 200) {
            throw Error("Received status code ${response.statusCode()} with message: ${response.body()}")
        }

        val nasaNeoApiData = Klaxon().parse<NasaNeoApiData>(response.body())

        if (nasaNeoApiData === null) {
            throw Error("Could not parse data received from the Nasa Neo Api")
        }

        return convertNasaNeoApiDataToDateToNearEarthObjectsMap(nasaNeoApiData)
    }

    private fun convertNasaNeoApiDataToDateToNearEarthObjectsMap(
        nasaNeoApiData: NasaNeoApiData): Map<LocalDate, List<NearEarthObject>> {

        val resultMap = mutableMapOf<LocalDate, MutableList<NearEarthObject>>()

        for ((date, arrayOfNasaNeos) in nasaNeoApiData.near_earth_objects) {
            val nearEarthObjects = mutableListOf<NearEarthObject>()

            for (nasaNeo in arrayOfNasaNeos) {
                val closeApproachData = nasaNeo.getCloseApproachDataForNearestMiss()

                val nearEarthObject = NearEarthObject(
                    nasaNeo.id,
                    nasaNeo.name,
                    closeApproachData.miss_distance.kilometers.toDouble(),
                    closeApproachData.close_approach_date,
                    closeApproachData.close_approach_date_full,
                    closeApproachData.orbiting_body,
                    closeApproachData.relative_velocity.kilometers_per_hour.toDouble(),
                    nasaNeo.getEstimatedDiameterMean(),
                    nasaNeo.nasa_jpl_url
                )
                nearEarthObjects.add(nearEarthObject)
            }

            resultMap[LocalDate.parse(date)] = nearEarthObjects
        }

        return resultMap
    }
}