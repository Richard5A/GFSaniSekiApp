package data.datasources.debug

import java.io.File

class IsDebugDataSource {
    fun isDebug(): Boolean {
        val file = File("debug.txt")
        return file.exists() && file.readText() == "true"
    }
}