
import com.google.gson.Gson
import java.net.InetSocketAddress

import com.sun.net.httpserver.HttpServer

import java.io.IOException

import com.sun.net.httpserver.HttpExchange

import com.sun.net.httpserver.HttpHandler
import java.lang.Exception

import java.time.LocalDate
import java.util.logging.Level
import java.util.logging.Logger

val logger: Logger = Logger.getLogger("Global Logger").also{ it.level = Level.ALL }

fun main(args: Array<String>) {
    println("Starting Nasa Near Earth Object Visualizer Backend")

    val server: HttpServer = HttpServer.create(InetSocketAddress(8000), -1)
    server.createContext("/NasaNearEarthObjects/closest", ClosestObjectsHttpHandler())
    server.executor = null // creates a default executor

    server.start()

//    val nearEarthObjectsFetcher = NearEarthObjectsFetcher()
//    nearEarthObjectsFetcher.fetchNearEarthObjects(LocalDate.of(2015, 12, 19), LocalDate.of(2015, 12, 26))

}

internal class ClosestObjectsHttpHandler : HttpHandler {
    @Throws(IOException::class)
    override fun handle(httpExchange: HttpExchange) {

        try {
            val inputStream = httpExchange.requestBody

            val query = httpExchange.requestURI.query // "from=2020-01-01&to=2020-01-02"

            val (fromLocalDate, toLocalDate) = extractFromAndToFromQuery(query)

            logger.log(Level.INFO, "Received closest object request for range $fromLocalDate to $toLocalDate")

            // todo: fix !!
            val closestNearEarthObject = findClosestObjectToEarthBetweenDates(fromLocalDate, toLocalDate)!!

            val response = Gson().toJson(closestNearEarthObject)

            httpExchange.sendResponseHeaders(200, response.length.toLong())
            val outputStream = httpExchange.responseBody
            outputStream.write(response.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            // todo: doesn't work for some reason
            println(e)
            throw e
        }
    }

    private fun extractFromAndToFromQuery(query: String): Pair<LocalDate, LocalDate> {
        val rex = Regex("from=(.*)&to=(.*)")
        val m = rex.find(query)

        if (m === null) {
            // todo: add logger, do proper error logging
            println("Received invalid query parameters. Query was: $query")
            throw Error("Received invalid query parameters. Query was: $query")
        }

        val fromString = m.groups[1]?.value
        val toString = m.groups[2]?.value

        if (fromString.isNullOrBlank() || toString.isNullOrBlank()) {
            throw Error("Received invalid query parameters. Query was: $query")
        }

        val fromLocalDate = LocalDate.parse(fromString)
        val toLocalDate = LocalDate.parse(toString)

        return Pair(fromLocalDate, toLocalDate)
    }
}


