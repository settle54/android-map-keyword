package campus.tech.kakao.map

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import campus.tech.kakao.map.PlaceDBHelper.Companion as comp

class PlaceDBManager(context: Context) {
    private val dbHelper = PlaceDBHelper(context)

    private fun checkPlaceExist(place: Place): Boolean {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(comp.NAME)
        val selection = "address = ?"
        val selectionArgs = arrayOf(place.address)
        val cursor = db.query(comp.TABLE_NAME, projection, selection, selectionArgs,
            null, null, null)
        db.close()
        return cursor.count > 0
    }

    fun insertPlace(place: Place) {
        if (checkPlaceExist(place)) {
            return
        }
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(comp.NAME, place.name)
            put(comp.ADDRESS, place.address)
            put(comp.CATEGORY, place.category)
        }
        db.insert(comp.TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllPlaces(): List<Place> {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM ${PlaceDBHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        val places = mutableListOf<Place>()
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(comp.NAME))
                val address = cursor.getString(cursor.getColumnIndex(comp.ADDRESS))
                val category = cursor.getString(cursor.getColumnIndex(comp.CATEGORY))

                val place = Place(name, address, category)
                places.add(place)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return places
    }

    fun deletePlace(place: Place) {
        val db = dbHelper.writableDatabase
        db.delete("${comp.TABLE_NAME}", "${comp.NAME}=?", arrayOf(place.name))
        db.close()
    }
}