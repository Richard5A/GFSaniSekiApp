package data.datasources.config

import data.json
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class ConfigDataSource {
    companion object {
        private const val FILE_NAME = "config.json"

        val configFlow = MutableStateFlow<ConfigDTO?>(getConfig())

        fun getConfig(): ConfigDTO? {
            val file = File(FILE_NAME)
            if (file.exists()) return try {
                json.decodeFromString<ConfigDTO>(file.readText())
            } catch (e: Exception) {
                null
            }
            return null
        }

        fun setConfig(data: ConfigDTO) {
            val file = File(FILE_NAME)
            if (!file.exists()) file.createNewFile()
            file.writer().use { fileWriter ->
                fileWriter.write(json.encodeToString(data))
            }
            configFlow.value = data
        }
    }
}