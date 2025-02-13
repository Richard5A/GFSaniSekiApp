package data.datasources.places

import data.json
import java.io.File

class PlaceDataSource {
    companion object {
        const val PLACES_FILE_NAME = "places.json"
    }

    fun getPlaces(): List<Place.PredefinedPlace>? {
        val file = File(PLACES_FILE_NAME)
        if (file.exists()) {
            return try {
                json.decodeFromString<List<Place.PredefinedPlace>>(file.readText())
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            println("Places file does not exist")
            return null
        }
    }
}