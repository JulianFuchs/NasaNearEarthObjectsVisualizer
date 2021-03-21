import com.beust.klaxon.Klaxon
import nasaNeoApiData.NasaNeoApiData
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.logging.Level
import java.util.concurrent.Executors


class NearEarthObjectsFetcher {

    companion object ApiFetcher {
        private const val nasaNeoFeedUrl = "https://api.nasa.gov/neo/rest/v1/feed"
        // todo: (opt) read this from an environment variable
        private const val apiKey = "ik3yhAzzg4zF6zYtBlry5Ti6NhIDxbAEdaSF4h8I"

        private var threadPool = Executors.newFixedThreadPool(10)

        val httpClient: HttpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build()

        fun fetchNearEarthObjectsFromNasa(from: LocalDate, to: LocalDate): Map<LocalDate, List<NearEarthObject>> {

            logger.log(Level.INFO, "Fetching data from Nasa NEO Api for date range $from to $to" )

            val datesToNearEarthObjects = mutableMapOf<LocalDate, List<NearEarthObject>>()

            val intervals = splitDateRangeInto8DayIntervals(from, to)

            val countDownLatch = CountDownLatch(intervals.size)

            for (interval in intervals) {
                val runnable = FetchDataFromApi(
                    interval.first,
                    interval.second,
                    countDownLatch,
                    datesToNearEarthObjects)
                threadPool.execute(runnable)
            }

            countDownLatch.await()

            return datesToNearEarthObjects
        }
    }

    class FetchDataFromApi(private val from: LocalDate,
                           private val to: LocalDate,
                           private val countDownLatch: CountDownLatch,
                           private val datesToNearEarthObjects: MutableMap<LocalDate, List<NearEarthObject>>,
    ): Runnable {

        override fun run() {
            val uri = URI.create("$nasaNeoFeedUrl?start_date=$from&end_date=$to&api_key=$apiKey")

            val request: HttpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .build()

            logger.log(Level.INFO, "Sending get request to Nasa NEO Api for date range $from to $to" )

            val response: HttpResponse<String> = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

            if (response.statusCode() != 200) {
                throw Error("Received status code ${response.statusCode()} with message: ${response.body()}")
            }

            val nasaNeoApiData = Klaxon().parse<NasaNeoApiData>(response.body())

            if (nasaNeoApiData === null) {
                throw Error("Could not parse data received from the Nasa Neo Api")
            }

            val dateToNearEarthObjectsMap = convertNasaNeoApiDataToDateToNearEarthObjectsMap(nasaNeoApiData)

            datesToNearEarthObjects.putAll(dateToNearEarthObjectsMap)

            countDownLatch.countDown()
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
}