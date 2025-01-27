package ui.einsatzmelder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.TriggerAlarmData
import data.datasources.config.ConfigDataSource
import data.repositories.TriggerAlarmRepository
import data.datasources.debug.IsDebugDataSource
import data.repositories.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.windows.notify
import java.awt.TrayIcon

class EinsatzmelderViewModel : ViewModel() {
    private val alarmRepository = TriggerAlarmRepository()
    private val placeRepository = PlaceRepository()

    private var _uiState: MutableStateFlow<EinsatzmelderUiState>
    private val isDebug: Boolean = IsDebugDataSource().isDebug()

    init {
        // run that after init of isDebug
        _uiState = MutableStateFlow(EinsatzmelderUiState(
            description = "",
            details = "",
            placeInput = "",
            place = null,
            keyword = "",
            type = "",
            loading = false,
            requestResultError = false,
            isDebug = isDebug,
            notifyLeader = false,
            leaderName = ConfigDataSource.getConfig()?.leaderName
        ))
    }

    val uiState: StateFlow<EinsatzmelderUiState>
        get() = _uiState.asStateFlow()

    fun setDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun setDetails(details: String) {
        _uiState.value = _uiState.value.copy(details = details)
    }

    fun setPlace(place: String) {
        val placeForString = placeRepository.findPlace(place)
//        println("Place $placeForString found for $place")
        _uiState.value = _uiState.value.copy(placeInput = place, place = placeForString)
    }

    fun setKeyword(keyword: String) {
        _uiState.value = _uiState.value.copy(keyword = keyword)
    }

    fun setType(type: String) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun setNotifyLeader(newValue: Boolean) {
        _uiState.value = _uiState.value.copy(notifyLeader = newValue)
    }

    fun triggerAlarm() {
        _uiState.value = _uiState.value.copy(loading = true)
        val uiStateNow = _uiState.value
        println("triggerAlarm called")

        viewModelScope.launch(Dispatchers.IO) {
            val lat = uiStateNow.place?.lat
            val lng = uiStateNow.place?.lng
            println("Dispatcher IO running")
            val result = alarmRepository.triggerAlarm(
                TriggerAlarmData(
                    keyword = if(isDebug) uiStateNow.keyword else "FR" + if(uiStateNow.notifyLeader) " + K" else "",
                    message = uiStateNow.description,
                    details = uiStateNow.details,
                    `object` = (uiStateNow.place?.name ?: uiStateNow.placeInput) +
                            if(uiStateNow.place?.description != null) " | " + uiStateNow.place.description else "",
                    type = uiStateNow.type,
                    lat = lat,
                    lng = lng,
                )
            )
            println("Call resulted with status code: ${result.code()}")

            if(result.code() != 200) {
                println("Call unsuccessful, trying backup")
                // The type will be overwritten by the System, so adding it to message.
                val backupResult = alarmRepository.triggerAlarm(
                    TriggerAlarmData(
                        keyword = "TEST",
                        message = "TYP: ${uiStateNow.type}\nMESSAGE:${uiStateNow.description}",
                        details = uiStateNow.details,
                        `object` = uiStateNow.placeInput,
                        lat = lat,
                        lng = lng,
                    )
                )
                println("Backup-call resulted with status code: ${backupResult.code()}")

                if(backupResult.code() != 200) {
                    println("Backup-call unsuccessful")
                    notify("Backup-Einsatz nicht gemeldet!", TrayIcon.MessageType.ERROR)
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        requestResultError = true
                    )

                } else {
                    println("Backup-call successful")
                    notify("Backup-Einsatz erfolgreich gemeldet!")
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        requestResultError = false,
                        details = "",
                        placeInput = "",
                        place = null,
                        keyword = "",
                        type = "",
                        description = ""
                    )
                }
            } else {
                notify("Einsatz erfolgreich gemeldet!")
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    requestResultError = false,
                    details = "",
                    placeInput = "",
                    place = null,
                    keyword = "",
                    type = "",
                    description = ""
                )
            }
        }
    }
}