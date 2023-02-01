package com.myrobi.shadhinmusiclibrary.library.player.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.download.room.DatabaseClient
import com.myrobi.shadhinmusiclibrary.download.room.DownloadedContent
import com.myrobi.shadhinmusiclibrary.download.room.WatchLaterContent
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper



internal class CacheRepository(val context: Context) {
    var sh: SharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE)
    private val databaseClient = DatabaseClient(context)
    private val watchLaterDb = databaseClient.getWatchlaterDatabase()
    private val downloadDb = databaseClient.getDownloadDatabase()
    private val favoriteContentDB = databaseClient.getFavoriteContentDatabase()
    fun insertFavoriteContent(favData: MutableList<FavDataModel>?) {
        favoriteContentDB?.FavoriteContentDao()?.insertAll(favData)
        //  Log.i("dfsfsdff", "insertFavoriteContent: ${a}")
    }

    fun insertFavSingleContent(favData: FavDataModel) {
        favoriteContentDB?.FavoriteContentDao()?.insert(favData)
    }
    fun deleteAllFavByType(type: String) {
        favoriteContentDB?.FavoriteContentDao()?.deleteAllFavByType(type)
    }
    fun deleteAllFavPodcast() = favoriteContentDB?.FavoriteContentDao()?.deleteAllFavPodcast()

    fun getAllFavoriteContent() = favoriteContentDB?.FavoriteContentDao()?.getAllFavorites()
    fun getSongsFavoriteContent() = favoriteContentDB?.FavoriteContentDao()?.getAllSongsFav()
    fun getArtistFavoriteContent() = favoriteContentDB?.FavoriteContentDao()?.getArtistFav()
    fun getAlbumsFavoriteContent() = favoriteContentDB?.FavoriteContentDao()?.getAlbumsFav()
    fun getPlaylistFavoriteContent() = favoriteContentDB?.FavoriteContentDao()?.getPlaylistFav()
    fun getVideoFavContent() = favoriteContentDB?.FavoriteContentDao()?.getAllVideosFav()
    fun getPodcastFavContent() = favoriteContentDB?.FavoriteContentDao()?.getAllPodcastFav()
    fun insertDownload(downloadedContent: DownloadedContent) {
        downloadDb?.DownloadedContentDao()?.insert(downloadedContent)
    }

    fun insertWatchLater(watchLaterContent: WatchLaterContent) {
        watchLaterDb?.WatchlaterContentDao()?.insert(watchLaterContent)
    }

    fun getFavoriteById(id: String): FavDataModel? {
        var contents = favoriteContentDB?.FavoriteContentDao()?.getFavoriteById(id)
        if (contents?.size ?: 0 > 0) {
            return contents!![0]
        }
        return null
    }

    fun deleteFavoriteById(id: String) {
        val path = favoriteContentDB?.FavoriteContentDao()?.getFavoriteById(id)
        favoriteContentDB?.FavoriteContentDao()?.deleteFavoriteById(id)
        try {
            UtilHelper.deleteFileIfExists(Uri.parse(path.toString()))
        } catch (e: NullPointerException) {

        }
    }

    fun getAllWatchlater() = watchLaterDb?.WatchlaterContentDao()?.getAllWatchLater()

    fun getWatchedVideoById(id: String): WatchLaterContent? {
        var contents = watchLaterDb?.WatchlaterContentDao()?.getWatchLaterById(id)
        if ((contents?.size ?: 0) > 0) {
            return contents!![0]
        }
        return null
    }

    //    fun deleteAllDownloads() {
//        val list=downloadDb?.DownloadedContentDao()?.getAllDownloads()
//        downloadDb?.DownloadedContentDao()?.deleteAllDownloads()
//        list?.forEach {
//            it.path?.let { uriStr->
//                FileUtil.deleteFileIfExists(Uri.parse(uriStr))
//                DownloadOrDeleteMp3Observer.notifySubscriber()
//            }
//        }
//    }
    fun getAllPendingDownloads() = downloadDb?.DownloadedContentDao()?.getAllPendingDownloads()
    fun getAllDownloads() = downloadDb?.DownloadedContentDao()?.getAllDownloads()
    fun getAllVideosDownloads() = downloadDb?.DownloadedContentDao()?.getAllVideosDownloads()
    fun getAllSongsDownloads() = downloadDb?.DownloadedContentDao()?.getAllSongsDownloads()
    fun getAllPodcastDownloads() = downloadDb?.DownloadedContentDao()?.getAllPodcastDownloads()

    fun getWatchTrackByID(contentId: String) =
        watchLaterDb?.WatchlaterContentDao()?.getWatchlaterTrackById(contentId)

    //
    fun getDownloadById(id: String): DownloadedContent? {
        var contents = downloadDb?.DownloadedContentDao()?.getDownloadById(id)
        if (contents?.size ?: 0 > 0) {
            return contents!![0]
        }
        return null
    }

    //
//    fun getPendingDownloadCount() = downloadDb?.DownloadedContentDao()?.getPendingDownloadCount()
    fun setDownloadedContentPath(id: String, path: String) =
        downloadDb?.DownloadedContentDao()?.setPath(id, path)

    //fun setDownloadedContentPath(id:String,path:String)=downloadDb?.DownloadedContentDao()?.setPath(id,path)
    fun deleteDownloadById(id: String) {
        val path = downloadDb?.DownloadedContentDao()?.getTrackById(id)
        downloadDb?.DownloadedContentDao()?.deleteDownloadById(id)
        try {
            UtilHelper.deleteFileIfExists(Uri.parse(path))
        } catch (e: NullPointerException) {

        }
    }

    fun deleteWatchlaterById(id: String) {
        val path = watchLaterDb?.WatchlaterContentDao()?.getWatchlaterTrackById(id)
        watchLaterDb?.WatchlaterContentDao()?.deleteWatchlaterById(id)
        try {
            UtilHelper.deleteFileIfExists(Uri.parse(path))
        } catch (e: NullPointerException) {

        }
        // DownloadOrDeleteObserver.notifySubscriber()
    }
//
//    fun deleteDownloadByAlbumId(albumId: String) {
//        val list=downloadDb?.DownloadedContentDao()?.getDownloadsByAlbumId(albumId)
//        downloadDb?.DownloadedContentDao()?.deleteDownloadByAlbumIdId(albumId)
//        list?.forEach {
//            it.path?.let {  uriStr->
//                FileUtil.deleteFileIfExists(Uri.parse(uriStr))
//                DownloadOrDeleteMp3Observer.notifySubscriber()
//            }
//        }
//    }

    fun isTrackDownloaded(contentId: String): Boolean {
        val path = downloadDb?.DownloadedContentDao()?.getTrackById(contentId)
        return if (path == null) {
            false
        } else {
            true
        }
    }

    fun getDownloadedContent() = downloadDb?.DownloadedContentDao()?.getAllDownloadedTrackById()

    //fun getDownlodById(contentId:String):Boolean{
//    var path = downloadDb?.DownloadedContentDao()?.getDownloadedTrackById(contentId)
//    Log.e("TAG", "TrackDownloadTrue: " + path)
//    if (path == null) {
//        Log.e("TAG", "TrackDownload: " + path)
//        return false
//    } else {
//        return true
//    }
//}
    fun isVideoDownloaded(content: String?): Boolean {
        return databaseClient.getDownloadDatabase()?.DownloadedContentDao()
            ?.downloadedVideoContent(content ?: "") ?: false
    }

    fun downloadState(content: String?, isDownloaded: Boolean) {
        databaseClient.getDownloadDatabase()?.DownloadedContentDao()
            ?.downloadState(content ?: "", if (isDownloaded) 1 else 0)
    }

    fun isDownloadCompleted(contentId: String): Boolean {
        val completed = databaseClient.getDownloadDatabase()?.DownloadedContentDao()
            ?.downloadedContent(contentId)
        if (completed?.equals(null) == true) {
            return false
        } else {
            return true
        }
    }
}