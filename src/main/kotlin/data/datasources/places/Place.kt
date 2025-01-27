package data.datasources.places

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val name: String,
    val isPrefix: Boolean? = null,
    val aliases: List<String>? = null,
    val description: String?,
    val lat: Double,
    val lng: Double,
)
