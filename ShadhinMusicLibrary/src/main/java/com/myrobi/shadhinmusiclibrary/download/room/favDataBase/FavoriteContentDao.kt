package com.myrobi.shadhinmusiclibrary.download.room.favDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel


@Dao
internal interface FavoriteContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(favData: MutableList<FavDataModel>?)


    @Query("SELECT * FROM FavData WHERE fav=1 ORDER By createDate DESC")
    fun getAllFavorites(): List<FavDataModel>

    @Query("SELECT * FROM  FavData where content_Id = :id AND fav=1 ")
    fun getFavoriteById(id: String): List<FavDataModel>

    @Query("SELECT * FROM FavData WHERE content_Type='V'  ORDER By createDate DESC ")
    fun getAllVideosFav(): List<FavDataModel>

    @Query("SELECT * FROM FavData WHERE content_Type='S'  ORDER By createDate DESC ")
    fun getAllSongsFav(): List<FavDataModel>

    @Query("SELECT * FROM FavData WHERE content_Type='A'  ORDER By createDate DESC")
    fun getArtistFav(): List<FavDataModel>

    @Query("SELECT * FROM FavData WHERE content_Type='R'  ORDER By createDate DESC ")
    fun getAlbumsFav(): List<FavDataModel>

    @Query("SELECT * FROM FavData WHERE content_Type='P'  ORDER By createDate DESC")
    fun getPlaylistFav(): List<FavDataModel>

    @Query("SELECT * FROM FavData WHERE content_Type LIKE 'PD%'  ORDER By createDate DESC")
    fun getAllPodcastFav(): List<FavDataModel>

    //
//    @Query("SELECT * FROM DownloadedContent where contentId = :id")
//    fun getDownloadById(id:String):List<DownloadedContent>
//
//    @Query("SELECT * FROM DownloadedContent where rootId = :albumId")
//    fun getDownloadsByAlbumId(albumId:String):List<DownloadedContent>
//
    @Query("DELETE FROM FavData WHERE content_Id = :id ")
    fun deleteFavoriteById(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favData: FavDataModel)
//    @Query("DELETE FROM DownloadedContent WHERE contentId = :albumId")
//    fun deleteDownloadByAlbumIdId(albumId:String):Unit
//
//    @Query("SELECT COUNT(*) FROM DownloadedContent WHERE type IS NULL")
//    fun getPendingDownloadCount():Int
//
//    @Query("UPDATE DownloadedContent SET track = :path  WHERE contentId =:id")
//    fun setPath(id:String,path:String)
//
////    @Query("SELECT * from DownloadedContent WHERE path IS NOT NULL GROUP BY albumId ORDER By createTime DESC ")
////    fun getAllAlbums():List<DownloadedContent>
//
//    @Query("SELECT track FROM DownloadedContent where contentId = :contentId  AND isDownloaded = 1")
//    fun getTrackById(contentId:String):String
//
//    @Query("SELECT * FROM DownloadedContent where  isDownloaded = 1")
//    fun getAllDownloadedTrackById():List<DownloadedContent>
//
//    @Query("SELECT isDownloaded FROM downloadedcontent WHERE  contentId = :contentId AND isDownloaded =1 LIMIT 1")
//    fun downloadedContent(contentId: String):Boolean?
//
//    @Query("SELECT isDownloaded FROM downloadedcontent WHERE type = 'V' AND contentId = :contentId AND isDownloaded =1 LIMIT 1")
//     fun downloadedVideoContent(contentId: String):Boolean?
//
//     @Query("UPDATE downloadedcontent SET isDownloaded = 1 WHERE contentId = :contentId")
//     fun downloadCompleted(contentId: String)

//
//    @Query("SELECT track FROM DownloadedContent where contentId = :contentId & isDownloaded = 1")
//    fun getDownloadedTrackById(contentId:String):String

//    @Query("DELETE FROM DownloadedContent")
//    fun deleteAllDownloads():Unit
}