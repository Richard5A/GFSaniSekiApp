package data

import kotlinx.serialization.Serializable

@Serializable
data class TriggerAlarmData(
    val alarmDate: String? = null,
    val keyword: String? = null,
    val type: String? = null,
    val message: String? = null,
    val details: String? = null,
    val operationResources: String? = null,
    val operationSchedule: String? = null,
    val `object`: String? = null,
    val location: String? = null,
    val district: String? = null,
    val alarmMessage: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
)
