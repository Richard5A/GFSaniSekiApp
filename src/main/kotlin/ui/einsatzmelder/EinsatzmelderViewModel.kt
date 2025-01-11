package ui.einsatzmelder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.TriggerAlarmData
import data.TriggerAlarmRepository
import data.datasources.debug.IsDebugDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ui.windows.notify
import java.awt.TrayIcon

class EinsatzmelderViewModel : ViewModel() {
    private val alarmRepository = TriggerAlarmRepository()

    private var _uiState: MutableStateFlow<EinsatzmelderUiState>
    private val isDebug: Boolean = IsDebugDataSource().isDebug()

    init {
        // run that after init of isDebug
        _uiState = MutableStateFlow(EinsatzmelderUiState(
            description = "",
            note = "",
            place = "",
            keyword = "",
            type = "",
            loading = false,
            requestResultError = false,
            isDebug = isDebug
        ))
    }

    val uiState: StateFlow<EinsatzmelderUiState>
        get() = _uiState.asStateFlow()

    fun setDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun setNotes(note: String) {
        _uiState.value = _uiState.value.copy(note = note)
    }

    fun setPlace(place: String) {
        _uiState.value = _uiState.value.copy(place = place)
    }

    fun setKeyword(keyword: String) {
        _uiState.value = _uiState.value.copy(keyword = keyword)
    }

    fun setType(type: String) {
        _uiState.value = _uiState.value.copy(type = type)
    }

    fun triggerAlarm() {
        _uiState.value = _uiState.value.copy(loading = true)
        val uiStateNow = _uiState.value
        println("triggerAlarm called")

        viewModelScope.launch(Dispatchers.IO) {
            println("Dispatcher IO running")
            val result = alarmRepository.triggerAlarm(
                TriggerAlarmData(
                    keyword = if(isDebug) uiStateNow.keyword else "RD",
                    message = uiStateNow.description,
                    note = uiStateNow.note,
                    `object` = uiStateNow.place,
                    type = uiStateNow.type,
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
                        note = uiStateNow.note,
                        `object` = uiStateNow.place,
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
                        note = "",
                        place = "",
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
                    note = "",
                    place = "",
                    keyword = "",
                    type = "",
                    description = ""
                )
            }
        }
    }
}