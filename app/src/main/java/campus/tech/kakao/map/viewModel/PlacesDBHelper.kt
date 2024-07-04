package campus.tech.kakao.map.viewModel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.model.Place

class PlacesDBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "places.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "places_table"
        const val NAME = "NAME"
        const val ADDRESS = "ADDRESS"
        const val CATEGORY = "CATEGORY"

        private const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$NAME TEXT, $ADDRESS TEXT, $CATEGORY TEXT);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun checkPlaceExist(place: Place): Boolean {
        val db = this.readableDatabase
        val projection = arrayOf(NAME)
        val selection = "address = ?"
        val selectionArgs = arrayOf(place.address)
        val cursor = db.query(
            TABLE_NAME, projection, selection, selectionArgs,
            null, null, null
        )
        val count = cursor.count
        cursor.close()
        db.close()
        return count > 0
    }

    fun insertPlace(place: Place) {
        if (checkPlaceExist(place)) {
            return
        }
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NAME, place.name)
            put(ADDRESS, place.address)
            put(CATEGORY, place.category)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllPlaces(): List<Place> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        val places = mutableListOf<Place>()
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                val address = cursor.getString(cursor.getColumnIndex(ADDRESS))
                val category = cursor.getString(cursor.getColumnIndex(CATEGORY))

                val place = Place(name, address, category)
                places.add(place)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return places
    }

    fun deletePlace(place: Place) {
        val db = this.writableDatabase
        db.delete("$TABLE_NAME", "$NAME=?", arrayOf(place.name))
        db.close()
    }
}