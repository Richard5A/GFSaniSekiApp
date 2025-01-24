package data.repositories

import data.datasources.places.Place
import data.datasources.places.PlaceDataSource

class PlaceRepository {
    private val dataSource: PlaceDataSource = PlaceDataSource()
    private val map: HashMap<String, Place> = hashMapOf()
    private val prefixMap: MutableMap<String, Place> = hashMapOf()

    init {
        dataSource.getPlaces()?.forEach { place: Place ->
            if(place.isPrefix != true){
                map[place.name] = place
                for(alias in place.aliases ?: emptyList()) {
                    map[alias] = place
                }

            } else {
                prefixMap[place.name] = place
            }
        }
        println(prefixMap)
    }

    /**
     * returns the place for that exact string.
     */
    fun findPlace(name: String): Place? {
        return map[name] ?: prefixMap.keys.find { key -> name.startsWith(key) }?.let { prefixMap[it] }
    }
}