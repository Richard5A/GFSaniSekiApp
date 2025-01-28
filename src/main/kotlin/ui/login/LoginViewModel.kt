package ui.login

import androidx.lifecycle.ViewModel
import data.datasources.credentials.CredentialsDataSource
import data.datasources.credentials.FFAgentAPICredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private var _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState("", "", ""))

    val uiState: StateFlow<LoginUiState> get() = _uiState.asStateFlow()

    fun setWebApiToken(apiToken: String) {
        _uiState.value = _uiState.value.copy(webApiToken = apiToken)
    }

    fun setAccessToken(accessToken: String) {
        _uiState.value = _uiState.value.copy(accessToken = accessToken)
    }

    fun setWebApiKey(webApiKey: String) {
        _uiState.value = _uiState.value.copy(webApiKey = webApiKey)
    }

    fun save(): Boolean {
        // TODO Add verification of codes
        try {
            CredentialsDataSource.setCredentials(
                FFAgentAPICredentials(
                    webApiToken = _uiState.value.webApiToken,
                    accessToken = _uiState.value.accessToken,
                    webApiKey = _uiState.value.webApiKey,
                    selectiveCallCode = "push"
                )
            )
            return true
        } catch (e: Exception) {
            return false
        }
    }
}