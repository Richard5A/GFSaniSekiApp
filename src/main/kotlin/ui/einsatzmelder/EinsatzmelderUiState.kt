package ui.einsatzmelder

data class EinsatzmelderUiState(
    val description: String,
    val note: String,
    val place: String,
    val keyword: String,
    val type: String,
    val loading: Boolean,
    val requestResultError: Boolean,
    val isDebug: Boolean,
)
