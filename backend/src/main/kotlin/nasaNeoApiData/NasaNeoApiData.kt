package nasaNeoApiData

/*
 * The classes in the nasaNeoApiData were auto-generated from the json returned by the api. In a typescript project I've
 * written for work, I solved a similar issue so that I had a script that generated the classes from an open-api specification.
 * That script would run as part of the building step. The advantage there is that generated files don't need to be
 * committed to repository. That would have been a solution I would have preferred, but I didn't find an open-api specification
 * for the nasa api, so I decided to settle with this approach.
 */
data class NasaNeoApiData(
    val element_count: Int,
    val links: Links,
    val near_earth_objects: Map<String, Array<NasaNeoData>>
)