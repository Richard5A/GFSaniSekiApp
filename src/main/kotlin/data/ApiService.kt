package data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import data.datasources.clientCertificate.CertificateDataSource
import data.datasources.config.ConfigDataSource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@OptIn(ExperimentalSerializationApi::class)
val json = Json {
    ignoreUnknownKeys = true
    allowTrailingComma = true
}

private val isFreeVersion = ConfigDataSource.getConfig()?.isFreeVersion

private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(if(isFreeVersion == true) "https://free.api.service.ff-agent.com/v1/WebService/" else "https://api.service.ff-agent.com/v1/WebService/")
    .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
    .client(if (isFreeVersion == false) CertificateDataSource.getCertificate() else OkHttpClient())
    .build()


interface ApiService {

    @POST("triggerAlarm")
    fun triggerAlarm(
        @Header("hmac") hmac: String,
        @Header("webApiToken") webApiToken: String,
        @Header("accessToken") accessToken: String,
        @Header("selectiveCallCode") selectiveCallCode: String,
        @Body body: TriggerAlarmData
    ): Call<Unit>
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}