package ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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
        Text(
            text = "Login",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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

        TextField(
            value = uiState.leaderName,
            label = { Text("Leader Name") },
            onValueChange = { newValue ->
                viewModel.setLeaderName(newValue)
            }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(checked = uiState.isFreeVersion, onCheckedChange = viewModel::setIsFreeVersion)
            Text(
                "Free Version",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Button(
            onClick = { viewModel.save() },
            enabled = uiState.webApiKey.isNotBlank() &&
                    uiState.webApiToken.isNotBlank() &&
                    uiState.accessToken.isNotBlank() &&
                    uiState.leaderName.isNotBlank(),
        ) {
            Text("Login")
        }
    }
}