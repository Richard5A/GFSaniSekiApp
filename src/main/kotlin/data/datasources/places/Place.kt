package data.datasources.places

import kotlinx.serialization.Serializable

@Serializable
open class Place(
    open val name: String,
    val isPrefix: Boolean? = null,
    val aliases: List<String>? = null,
    val description: String?,
    open val lat: Double,
    open val lng: Double,
) {
    data class MapPlace(
        override val name: String,
        override val lat: Double,
        override val lng: Double,
    ): Place(name, null, null, null, lat, lng)
}


