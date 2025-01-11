package data.datasources.credentials

import kotlinx.serialization.Serializable

@Serializable
data class FFAgentAPICredentials(
    val webApiToken: String,
    val accessToken: String,
    val selectiveCallCode: String,
    val webApiKey: String,
)