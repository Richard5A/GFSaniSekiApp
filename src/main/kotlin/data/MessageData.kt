package data

import kotlinx.serialization.Serializable

@Serializable
data class MessageData(
    val type: String,
    val name: String,
    val lat: Double,
    val lng: Double,
)