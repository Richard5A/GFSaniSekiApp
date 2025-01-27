package ui.einsatzmelder

import data.datasources.places.Place

data class EinsatzmelderUiState(
    val description: String,
    val details: String,
    val placeInput: String,
    val latText: Double,
    val lngText: Double,
    val place: Place?,
    val keyword: String,
    val type: String,
    val loading: Boolean,
    val requestResultError: Boolean,
    val isDebug: Boolean,
    val notifyLeader: Boolean,
    val leaderName: String?,
)
