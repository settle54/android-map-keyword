package campus.tech.kakao.map

import android.content.Context

class MapRepository(context: Context) {
    private var localDB: PlacesDBHelper

    init {
        localDB = PlacesDBHelper(context)
        val dbFile = context.getDatabasePath("PlaceTable.db")
        if (!dbFile.exists())
            insertInitialData()
    }

    private fun insertInitialData() {
        for (i in 1..30) {
            val cafe = Place("카페" + i, "서울 성동구 성수동 " + i, "카페")
            val drugStore = Place("약국" + i, "서울 강남구 대치동 " + i, "약국")
            localDB.insertPlace(cafe)
            localDB.insertPlace(drugStore)
        }
    }

    fun getAllPlaces(): List<Place> {
        return localDB.getAllPlaces()
    }

    fun insertPlace(name: String, address: String, category: String) {
        val place = Place(name, address, category)
        localDB.insertPlace(place)
    }

    fun deletePlace(name: String, address: String, category: String) {
        val place = Place(name, address, category)
        localDB.deletePlace(place)
    }
}