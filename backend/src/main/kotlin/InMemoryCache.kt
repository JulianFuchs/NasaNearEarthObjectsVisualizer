import java.time.LocalDate
import java.util.logging.Level

/**
This is a placeholder implementation for a very basic cache. Would be cool to replace this with for example Redis at
some point.
 */
class InMemoryCache {

    companion object Cache {
        private val cache = mutableMapOf<LocalDate, List<NearEarthObject>>()

        fun updateCache(map: Map<LocalDate,List<NearEarthObject>>, from: LocalDate, to: LocalDate) {
            logger.log(Level.INFO, "Updating cache with data in range of $from to $to" )
            cache.putAll(map)
        }

        // since the cache is always updated in ranges, we just need to check if both the start date and end date
        // are contained in the cache, and if so, we know we have cached the data already
        fun containsDateRange(from: LocalDate, to: LocalDate): Boolean {
            return cache.containsKey(from) && cache.containsKey(to)
        }

        fun getCachedData(from: LocalDate, to: LocalDate): Map<LocalDate,List<NearEarthObject>> {
            logger.log(Level.INFO, "Fetching data from cache for data from $from to $to" )
            val mutableMap = mutableMapOf<LocalDate,List<NearEarthObject>>()

            var date = from

            while(date.isBefore(to.plusDays(1))) {
                mutableMap[date] = cache[date]!!
                date = date.plusDays(1)
            }

            return mutableMap
        }

        fun clearCache() {
            cache.clear()
        }
    }
}