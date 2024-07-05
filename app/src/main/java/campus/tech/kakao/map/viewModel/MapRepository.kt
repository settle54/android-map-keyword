package campus.tech.kakao.map.viewModel

import android.content.Context
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.viewModel.PlacesDBHelper

class MapRepository(context: Context) {
    private val localDB: PlacesDBHelper = PlacesDBHelper(context)

    init {
        val dbFile = context.getDatabasePath("${PlacesDBHelper.TABLE_NAME}")
        if (!dbFile.exists()) {
            insertInitialData()
        }
    }

    private fun insertInitialData() {
        val places = arrayListOf<Place>()
        for (i in 1..30) {
            val cafe = Place("카페$i", "서울 성동구 성수동 $i", "카페")
            val drugStore = Place("약국$i", "서울 강남구 대치동 $i", "약국")
            places.add(cafe)
            places.add(drugStore)
        }
        localDB.insertPlaces(places)
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