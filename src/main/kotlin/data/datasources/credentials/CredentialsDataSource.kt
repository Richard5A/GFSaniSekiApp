package data.datasources.credentials

import data.json
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class CredentialsDataSource {
    companion object {
        private const val FILE_NAME = "credentials.json"

        /**
         * Always updates when something's set.
         */
        val credentialsFlow: MutableStateFlow<FFAgentAPICredentials?> = MutableStateFlow(getCredentials())

        /**
         * should throw no exception.
         */
        fun getCredentials(): FFAgentAPICredentials? {
            val file = File(FILE_NAME)
            if (file.exists()) return try {
                json.decodeFromString<FFAgentAPICredentials>(file.readText())
            } catch (e: Exception) {
                null
            }
            return null
        }

        /**
         * returns if credentials are saved.
         */
        fun hasCredentials(): Boolean {
            val file = File(FILE_NAME)
            return file.exists() && try {
                json.decodeFromString<FFAgentAPICredentials?>(file.readText()); true
            } catch (e: Exception) {
                false
            }
        }

        fun setCredentials(credentials: FFAgentAPICredentials) {
            val file = File(FILE_NAME)
            if (!file.exists()) file.createNewFile()
            file.writer().use { fileWriter ->
                fileWriter.write(json.encodeToString(credentials))
            }
            credentialsFlow.value = credentials
        }
    }
}