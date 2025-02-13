package data.datasources.places

import kotlinx.serialization.Serializable

@Serializable
sealed class Place{
    abstract val name: String
    abstract val lat: Double
    abstract val lng: Double

    @Serializable
    class MapPlace(
        override val name: String,
        override val lat: Double,
        override val lng: Double,
    ): Place()

    @Serializable
    class PredefinedPlace(
        override val name: String,
        override val lat: Double,
        override val lng: Double,
        val isPrefix: Boolean? = null,
        val aliases: List<String>? = null,
        val description: String?
    ): Place()
}


