package data.repositories

import data.datasources.places.Place
import data.datasources.places.PlaceDataSource

class PlaceRepository {
    private val dataSource: PlaceDataSource = PlaceDataSource()
    private val map: HashMap<String, Place> = hashMapOf()

    init {
        dataSource.getPlaces()?.forEach { place: Place ->
            map[place.name] = place
            for(alias in place.aliases) {
                map[alias] = place
            }
        }
    }

    /**
     * returns the place for that exact string.
     */
    fun findPlace(name: String): Place? {
        return map[name]
    }
}