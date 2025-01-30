package ui.login

data class LoginUiState(
    val webApiToken: String,
    val accessToken: String,
    val webApiKey: String,
    val leaderName: String,
    val isFreeVersion: Boolean
)
