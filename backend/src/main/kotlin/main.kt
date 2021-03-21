
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

enum class OperationMode {
    FIND_CLOSEST, FIND_LARGEST
}

fun main(args: Array<String>) {
    println("Starting Nasa Near Earth Object Visualizer Backend")

    val server: HttpServer = HttpServer.create(InetSocketAddress(8000), -1)
    server.createContext("/NasaNearEarthObjects/closest", RequestHandler(OperationMode.FIND_CLOSEST))
    server.createContext("/NasaNearEarthObjects/largest", RequestHandler(OperationMode.FIND_LARGEST))
    server.executor = null // creates a default executor

    server.start()
}

internal class RequestHandler(val operationMode: OperationMode) : HttpHandler {
    @Throws(IOException::class)
    override fun handle(httpExchange: HttpExchange) {

        try {
            val query = httpExchange.requestURI.query

            val (fromLocalDate, toLocalDate) = extractFromAndToFromQuery(query)

            val closestNearEarthObject = when(operationMode) {
                OperationMode.FIND_CLOSEST -> {
                    logger.log(Level.INFO, "Received closest object request for range $fromLocalDate to $toLocalDate")
                    findClosestNearEarthObjectToEarth(fromLocalDate, toLocalDate)!!
                }
                OperationMode.FIND_LARGEST -> {
                    logger.log(Level.INFO, "Received largest object request for range $fromLocalDate to $toLocalDate")
                    findLargestNearEarthObject(fromLocalDate, toLocalDate)
                }
            }

            val response = Gson().toJson(closestNearEarthObject)

            httpExchange.sendResponseHeaders(200, response.length.toLong())
            val outputStream = httpExchange.responseBody
            outputStream.write(response.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            // todo: doesn't work for some reason
            logger.log(Level.SEVERE, e.toString())
            throw e
        }
    }
}

private fun extractFromAndToFromQuery(query: String): Pair<LocalDate, LocalDate> {
    val rex = Regex("from=(.*)&to=(.*)")
    val m = rex.find(query)

    if (m === null) {
        logger.log(Level.SEVERE,"Received invalid query parameters. Query was: $query")
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


