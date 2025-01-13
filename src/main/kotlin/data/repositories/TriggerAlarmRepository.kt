package data.repositories

import data.Api
import data.TriggerAlarmData
import data.datasources.credentials.CredentialsDataSource
import data.json
import retrofit2.Response
import retrofit2.awaitResponse
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class TriggerAlarmRepository {

    @OptIn(ExperimentalStdlibApi::class)
    private fun calculateHMAC(
        text: String,
        key: String,
    ): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key.toByteArray(), "HmacSHA256"))
        return mac.doFinal(text.toByteArray()).toHexString()
    }

    suspend fun triggerAlarm(data: TriggerAlarmData): Response<Unit> {
        val credentials = CredentialsDataSource.getCredentials()!!
        return Api.retrofitService.triggerAlarm(
            hmac = calculateHMAC("${credentials.webApiToken}${credentials.selectiveCallCode}${credentials.accessToken}${json.encodeToString(data)}", credentials.webApiKey),
            webApiToken = credentials.webApiToken,
            accessToken = credentials.accessToken,
            selectiveCallCode = credentials.selectiveCallCode,
            body = data
        ).awaitResponse()
    }

    suspend fun triggerTestAlarm(): Response<Unit> {
        val credentials = CredentialsDataSource.getCredentials()!!
        val body = TriggerAlarmData(
            operationSchedule = null,
            operationResources = null,
            note = null,
            type = null,
            alarmDate = null,
            alarmMessage = null,
            `object` = null,
            district = null,
            location = null,
            keyword = "TEST",
            message = null,
        )
        return Api.retrofitService.triggerAlarm(
            hmac = calculateHMAC("${credentials.webApiToken}${credentials.selectiveCallCode}${credentials.accessToken}${json.encodeToString(body)}", credentials.webApiKey),
            webApiToken = credentials.webApiToken,
            accessToken = credentials.accessToken,
            selectiveCallCode = credentials.selectiveCallCode,
            body = body
        ).awaitResponse()
    }

}