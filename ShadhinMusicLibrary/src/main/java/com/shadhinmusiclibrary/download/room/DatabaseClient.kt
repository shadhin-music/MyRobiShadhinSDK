package com.shadhinmusiclibrary.download.room

import android.content.Context
import androidx.room.Room
import com.shadhinmusiclibrary.download.room.downloadDataBase.DownloadDatabase
import com.shadhinmusiclibrary.download.room.watchLaterDatabase.WatchlaterDatabase
import com.shadhinmusiclibrary.download.room.favDataBase.FavoriteDatabase

class DatabaseClient {
    private var mCtx: Context? = null
    private var mInstance: DatabaseClient? = null
    private var downloadDataBase: DownloadDatabase? = null
    private var watchlaterDatabase: WatchlaterDatabase? = null
    private var favoriteContentDatabase: FavoriteDatabase? = null

    constructor(mCtx: Context) {
        this.mCtx = mCtx

        downloadDataBase = Room.databaseBuilder(mCtx, DownloadDatabase::class.java, "DownloadDb")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()

        favoriteContentDatabase =
            Room.databaseBuilder(mCtx, FavoriteDatabase::class.java, "FavoriteContentDb")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build()

        watchlaterDatabase =
            Room.databaseBuilder(mCtx, WatchlaterDatabase::class.java, "WatchlaterDb")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Synchronized
    fun getInstance(mCtx: Context): DatabaseClient? {
        if (mInstance == null) {
            mInstance = DatabaseClient(mCtx)
        }
        return mInstance
    }

    internal fun getDownloadDatabase() = downloadDataBase
    internal fun getWatchlaterDatabase() = watchlaterDatabase
    internal fun getFavoriteContentDatabase() = favoriteContentDatabase
}