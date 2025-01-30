package data

import kotlinx.serialization.Serializable

@Serializable
data class WebViewMessageData(
    val type: String,
    val name: String,
    val lat: Double,
    val lng: Double,
)