package com.myrobi.shadhinmusiclibrary.download.room.favDataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel


@Database(
    entities = [
        FavDataModel::class
    ],
    version = 2,
    exportSchema = false
)

internal abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun FavoriteContentDao(): FavoriteContentDao
}