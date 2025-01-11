package ui.einsatzmelder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EinsatzmelderScreen(viewModel: EinsatzmelderViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn (
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                Text(
                    "Einsatz auslösen" + if(uiState.isDebug) " (Debug)" else "",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                if(uiState.requestResultError) {
                    Card(colors = CardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface
                    )) {

                    }
                }
            }

            if(uiState.isDebug){
                item {
                    TextField(
                        value = uiState.keyword,
                        onValueChange = { viewModel.setKeyword(it) },
                        label = { Text("Keyword *") },
                        modifier = Modifier.sizeIn(
                            minHeight = 64.dp,
                            minWidth = 256.dp,
                            maxWidth = 400.dp,
                            maxHeight = 256.dp
                        ).fillMaxWidth(),
                        singleLine = true,
                    )
                }
            }

            item {
                TextField(
                    value = uiState.type,
                    onValueChange = { viewModel.setType(it) },
                    label = { Text("Typ *") },
                    modifier = Modifier.sizeIn(
                        minHeight = 64.dp,
                        minWidth = 256.dp,
                        maxWidth = 400.dp,
                        maxHeight = 256.dp
                    ).fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                TextField(
                    value = uiState.place,
                    onValueChange = { viewModel.setPlace(it) },
                    label = { Text("Ort *") },
                    modifier = Modifier.sizeIn(
                        minHeight = 64.dp,
                        minWidth = 256.dp,
                        maxWidth = 400.dp,
                        maxHeight = 256.dp
                    ).fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                TextField(
                    value = uiState.description,
                    onValueChange = { viewModel.setDescription(it) },
                    label = { Text("Beschreibung (optional)") },
                    modifier = Modifier.sizeIn(
                        minHeight = 100.dp,
                        minWidth = 256.dp,
                        maxWidth = 400.dp,
                        maxHeight = 256.dp
                    ).fillMaxWidth()
                )
            }

            item {
                TextField(
                    value = uiState.note,
                    onValueChange = { viewModel.setNotes(it) },
                    label = { Text("Notizen (optional)") },
                    modifier = Modifier.sizeIn(
                        minHeight = 100.dp,
                        minWidth = 256.dp,
                        maxWidth = 400.dp,
                        maxHeight = 256.dp
                    ).fillMaxWidth(),
                    keyboardOptions = KeyboardOptions()
                )
            }

            item {
                Button(
                    onClick = { viewModel.triggerAlarm() },
                    modifier = Modifier.padding(bottom = 16.dp, top = 8.dp),
                    enabled = (!uiState.isDebug || uiState.keyword.isNotBlank()) && uiState.place.isNotBlank() && uiState.type.isNotBlank(),
                ) {
                    Text("Einsatz auslösen")
                }
            }
        }

        if (uiState.loading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x88FFFFFF))
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {}
            ) {
                CircularProgressIndicator(
                    Modifier.align(Alignment.Center)
                )
            }
        }

    }
}