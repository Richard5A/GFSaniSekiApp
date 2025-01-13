package data.datasources.places

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val name: String,
    val aliases: List<String>,
    val description: String?,
    val lat: Double,
    val lng: Double,
)
