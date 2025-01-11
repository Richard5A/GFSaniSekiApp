package ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        TextField(
            value = uiState.webApiKey,
            label = { Text("Web API Key") },
            onValueChange = { newValue ->
                viewModel.setWebApiKey(newValue)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        TextField(
            value = uiState.accessToken,
            label = { Text("Access Token") },
            onValueChange = { newValue ->
                viewModel.setAccessToken(newValue)
            }
        )

        TextField(
            value = uiState.webApiToken,
            label = { Text("Web Api Token") },
            onValueChange = { newValue ->
                viewModel.setWebApiToken(newValue)
            }
        )

        Button(
            onClick = { viewModel.save() },
        ) {
            Text("Login")
        }
    }
}