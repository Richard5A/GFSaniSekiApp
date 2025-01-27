package data.datasources.clientCertificate

import data.datasources.config.ConfigDataSource
import okhttp3.OkHttpClient
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.*

class CertificateDataSource {
    companion object {
        private const val FILE_NAME = "ffagent-keycert.p12"

        fun getCertificate(): OkHttpClient {
            val password: String = ConfigDataSource.getConfig()?.certificatePassword ?: ""
            val keyStore = KeyStore.getInstance("PKCS12")

            try {
                FileInputStream(FILE_NAME).use { inputStream ->
                    keyStore.load(inputStream, password.toCharArray())
                }

                // Initialisiere KeyManagerFactory
                val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                keyManagerFactory.init(keyStore, password.toCharArray())

                // Initialisiere TrustManagerFactory
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(keyStore)

                // Erstelle SSLContext
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)

                // Baue den OkHttpClient mit der benutzerdefinierten SSL-Konfiguration
                return OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
                    .build()
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
                return OkHttpClient()
            }
        }
    }
}