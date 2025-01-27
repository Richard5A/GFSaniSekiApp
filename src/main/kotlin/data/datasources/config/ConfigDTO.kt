package data.datasources.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigDTO(
    val leaderName: String,
    val isFreeVersion: Boolean,
    val certificatePassword: String,
)
