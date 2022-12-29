package com.shadhinmusiclibrary.library.player.utils

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log

import com.google.android.exoplayer2.MediaItem
import com.shadhinmusiclibrary.library.player.Constants
import com.shadhinmusiclibrary.library.player.connection.ShadhinMusicServiceConnection
import com.shadhinmusiclibrary.library.player.data.model.*
import com.shadhinmusiclibrary.utils.exH
import com.shadhinmusiclibrary.utils.get
import com.shadhinmusiclibrary.utils.randomString
import com.shadhinmusiclibrary.utils.values
import java.text.SimpleDateFormat
import java.util.*


internal fun List<Music>.toServiceMediaItemList() =
    exH { this.map { song -> toServiceMediaItem(song) } }

internal fun List<Music>.toServiceMediaItemMutableList() =
    exH { this.toServiceMediaItemList()?.toMutableList() }

/*fun List<Music>.toMediaSourceList(context: Context): List<ProgressiveMediaSource> {

    val isInternetConnected = isConnectedToInternet(context)
    val sources = this.map { song ->
        val pla = song.toPlayerMediaItem()
        val dataSource = ShadhinDataSourceFactory.buildWithCache(context,song,isInternetConnected)
        ProgressiveMediaSource.Factory(dataSource)
            .createMediaSource(pla)
    }.toList()

    return sources
}*/
/*fun List<Music>.toDataSource(context: Context): ConcatenatingMediaSource {
    val isInternetConnected = isConnectedToInternet(context)
    val concatenatingMediaSource = ConcatenatingMediaSource()
    val sources = this.map { song ->
        val pla = song.toPlayerMediaItem()
        val dataSource = ShadhinDataSourceFactory.buildWithCache(context, song, isInternetConnected)
        ProgressiveMediaSource.Factory(dataSource)
            .createMediaSource(pla)
    }.toList()
    concatenatingMediaSource.addMediaSources(sources)
    return concatenatingMediaSource
}*/

internal fun MediaItem.toMusic(): Music {
    val others = this.mediaMetadata.extras?.toMusicMetaData()
    return Music(
        mediaId = this.mediaId.ifEmpty { randomString(8) },
        title = this.mediaMetadata.title.toString(),
        displayDescription = this.mediaMetadata.description.toString(),
        mediaUrl = this.mediaMetadata.mediaUri.toString(),
        displayIconUrl = this.mediaMetadata.artworkUri.toString(),
        artistName = this.mediaMetadata.artist.toString(),
        contentType = others?.contentType,
        date = others?.date,
        userPlayListId = others?.userPlayListId,
        starring = others?.starring,
        seekable = others?.seekable,
        details = others?.details,
        totalStream = others?.totalStream,
        fav = others?.fav,
        trackType = others?.trackType,
        isPaid = others?.isPaid,
    ).applyRootInfo(others)
}

fun MediaItem.toServiceMediaItem(): MediaBrowserCompat.MediaItem {
    return this.toMusic().toServiceMediaItem()
}

internal fun toServiceMediaItem(music: Music): MediaBrowserCompat.MediaItem {
    val description = MediaDescriptionCompat.Builder()
        .setMediaUri(Uri.parse(music.mediaUrl))
        .setTitle(music.title)
        .setDescription(music.displayDescription)
        .setExtras(music.toBundleMetaData("toServiceMediaItem"))
        .setSubtitle(music.artistName)
        .setMediaId(music.mediaId.toString())
        .setIconUri(Uri.parse(music.displayIconUrl)) //This string I put for display image name samsung One UI lock screen
        .build()

    return MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
}

internal fun Bundle.toMusicMetaData(): Music {
    return Music(
        artistName = this.getString(Music.ARTIST_NAME),
        date = this.getString(Music.DATE),
        contentType = this.getString(Music.CONTENT_TYPE),
        userPlayListId = this.getString(Music.USER_PLAYLIST_ID),
        episodeId = this.getString(Music.EPISODE_ID),
        details = this.getString(Music.DETAILS),
        totalStream = this.getLong(Music.TOTAL_STREAM),
        fav = this.getString(Music.FAV),
        trackType = this.getString(Music.TRACK_TYPE),
        isPaid = this.getBoolean(Music.IS_PAID),
        seekable = this.getString(Music.SEEKABLE).equals("true", true),
        rootId = this.getString(Music.ROOT_ID),
        rootType = this.getString(Music.ROOT_TYPE),
        rootTitle = this.getString(Music.ROOT_TITLE),
        rootImage = this.getString(Music.ROOT_IMAGE)
    )
}

internal fun MediaMetadataCompat.toMusic(): Music? {

    /*  this.bundle.keySet().forEach {
        Log.i("toMusicMetaData", "toMusicMetaData: $it -> ${this.bundle.get(it)}")

    }
     Log.i("toMusicMetaData", "-----------------------------------------------")*/

    val meta = this.bundle.toMusicMetaData()
    return description?.let {
        Music(
            mediaId = it.mediaId,
            title = it.title.toString(),
            artistName = meta.artistName,
            displayDescription = it.subtitle.toString(),
            displayIconUrl = it.iconUri.toString(),
            mediaUrl = it.mediaUri.toString(),
            contentType = meta.contentType,
            date = meta.date,
            userPlayListId = meta.userPlayListId,
            episodeId = meta.episodeId,
            starring = meta.starring,
            seekable = meta.seekable,
            details = meta.details,
            totalStream = meta.totalStream,
            fav = meta.fav,
            trackType = meta.trackType,
            isPaid = meta.isPaid,
        ).applyRootInfo(meta)
    }
}

internal fun MediaBrowserCompat.MediaItem.toMusic(): Music {
    val meta = description.extras?.toMusicMetaData()
    return Music(
        mediaId = description.mediaId,
        title = description.title.toString(),
        displayDescription = description.subtitle.toString(),
        displayIconUrl = description.iconUri.toString(),
        artistName = meta?.artistName,
        contentType = meta?.contentType,
        date = meta?.date,
        mediaUrl = description.mediaUri.toString(),
        details = meta?.details,
        totalStream = meta?.totalStream,
        fav = meta?.fav,
        trackType = meta?.trackType,
        isPaid = meta?.isPaid,
        starring = meta?.starring,
        seekable = meta?.seekable,
    ).applyRootInfo(meta)
}
/*fun ArtistContents.Data.toMusic() = Music(
    mediaId = contentID,
    title = title,
    displayIconUrl = CharParser.getImageFromTypeUrl(image, Constants.CONTENT_TYPE_ARTIST),
    mediaUrl = playUrl,
    artistName = artistname,
    displayDescription = labelname,
    date = releaseDate,
    contentType = contentType,


)

fun ArtistContents.Data.copy(): ArtistContents.Data {
    val data = ArtistContents.Data()
        data.artistname    = this.artistname
        data.contentID     = this.contentID
        data.contentType   = this.contentType
        data.copyright     = this.copyright
        data.duration      = this.duration
        data.image         = this.image
        data.labelname     = this.labelname
        data.playUrl       = this.playUrl
        data.releaseDate   = this.releaseDate
        data.title         = this.title
        data.albumId       = this.albumId
        data.artistId      = this.artistId
        data.totalPlay     = this.totalPlay
        data.isPlaying     = this.isPlaying
        data.isRBT         = this.isRBT
   return data
}

fun PlaylistContents.Data.copy(): PlaylistContents.Data {
    val data = PlaylistContents.Data()
    data.artist        = this.artist
    data.contentID     = this.contentID
    data.contentType   = this.contentType
    data.copyright     = this.copyright
    data.duration      = this.duration
    data.image         = this.image
    data.labelname     = this.labelname
    data.playUrl       = this.playUrl
    data.releaseDate   = this.releaseDate
    data.fav           = this.fav
    data.title         = this.title
    data.albumId       = this.albumId
    data.artistId      = this.artistId
    data.isPlaying     = this.isPlaying
    data.isRBT         = this.isRBT
    return data
}
fun AlbumContents.Data.copy(): AlbumContents.Data {
    val data = AlbumContents.Data()
    data.artist        = this.artist
    data.contentID     = this.contentID
    data.contentType   = this.contentType
    data.copyright     = this.copyright
    data.duration      = this.duration
    data.image         = this.image
    data.labelname     = this.labelname
    data.playUrl       = this.playUrl
    data.releaseDate   = this.releaseDate
    data.fav           = this.fav
    data.title         = this.title
    data.artistId      = this.artistId
    data.isPlaying     = this.isPlaying
    data.isRBT         = this.isRBT
    return data
}
fun CategoryContents.Data.copy(): CategoryContents.Data {
    val data = CategoryContents.Data()
    data.artist        = this.artist
    data.contentID     = this.contentID
    data.contentType   = this.contentType
    data.copyright     = this.copyright
    data.duration      = this.duration
    data.image         = this.image
    data.playUrl       = this.playUrl
    data.fav           = this.fav
    data.title         = this.title
    data.artistId      = this.artistId

    data.banner        = this.banner
    data.playCount     = this.playCount
    data.isPaid        = this.isPaid
    data.trackType     = this.trackType
    data.downloadId    = this.downloadId

    data.albumIdV2     = this.albumIdV2
    data.playListName  = this.playListName
    data.albumImage    = this.albumImage
    data.albumId       = this.albumId
    data.playListId    = this.playListId
    data.epsoidId      = this.epsoidId
    data.isPlaystate   = this.isPlaystate
    data.isPlaying     = this.isPlaying
    data.deleteChecked = this.deleteChecked
    data.isFavouriteArtist = this.isFavouriteArtist


    return data
}
fun PodcastShowModel.TrackList.copy():PodcastShowModel.TrackList{
    val data = PodcastShowModel.TrackList()
    data.id           = this.id
    data.name         = this.name
    data.showId       = this.showId
    data.showName     = this.showName
    data.episodeId    = this.episodeId
    data.imageUrl     = this.imageUrl
    data.playUrl      = this.playUrl
    data.starring     = this.starring
    data.duration     = this.duration
    data.seekable     = this.seekable
    data.details      = this.details
    data.ceateDate    = this.ceateDate
    data.totalStream  = this.totalStream
    data.contentType  = this.contentType
    data.fav          = this.fav
    data.sort         = this.sort
    data.trackType    = this.trackType
    data.isPaid       = this.isPaid
    data.isPlaying    = this.isPlaying
    return  data
}

fun PlaylistData.copy():PlaylistData{
    val data = PlaylistData()
    data.artist         = this.artist
    data.contentID      = this.contentID
    data.contentType    = this.contentType
    data.copyright      = this.copyright
    data.duration       = this.duration
    data.fav            = this.fav
    data.image          = this.image
    data.duration       = this.duration
    data.labelname      = this.labelname
    data.playUrl        = this.playUrl
    data.releaseDate    = this.releaseDate
    data.title          = this.title
    data.contentType    = this.contentType
    data.fav            = this.fav
    data.userPlayListId = this.userPlayListId
    data.isPlaying      = this.isPlaying
    return  data
}*/


/*
fun List<PlaylistContents.Data>?.playListAddItem(index:Int,newData: PlaylistContents.Data?): List<PlaylistContents.Data> {

    if(newData == null && this!= null){
        return this
    }

    val newList = this?.map { data -> data.copy() }?.toMutableList()?:ArrayList()
    if(index < newList.size){
        newData?.let { newList.add(index, it) }
    }
    return newList
}

fun List<ArtistContents.Data>?.artistNewListAddItem(index:Int,newData: ArtistContents.Data?): List<ArtistContents.Data> {

    if(newData == null && this!= null){
        return this
    }

    val newList = this?.map { data -> data.copy() }?.toMutableList()?:ArrayList()
    if(index < newList.size){
        newData?.let { newList.add(index, it) }
    }
    return newList
}
fun List<AlbumContents.Data>?.albumContentsNewListAddItem(index:Int,newData: AlbumContents.Data?): List<AlbumContents.Data> {

    if(newData == null && this!= null){
        return this
    }

    val newList = this?.map { data -> data.copy() }?.toMutableList()?:ArrayList()
    if(index < newList.size){
        newData?.let { newList.add(index, it) }
    }
    return newList
}
*/


/*fun List<ArtistContents.Data>?.artistNewList(mediaId: String?): List<ArtistContents.Data> {
    val newList:MutableList<ArtistContents.Data> = ArrayList()
    if(mediaId == null && this!= null){
        return this
    }
    this?.forEach {
        if(it.contentID == mediaId || it.isPlaying){
            val newItem = it.clone()
            newItem.isPlaying = it.contentID == mediaId || !it.isPlaying
            newList.add(newItem)
        }else{
            newList.add(it)
        }

    }
    return newList
}
fun List<PlaylistContents.Data>?.playlistNewList(mediaId: String?): List<PlaylistContents.Data> {
    val newList:MutableList<PlaylistContents.Data> = ArrayList()
    if(mediaId == null && this!= null){
        return this
    }
    this?.forEach {
        if(it.contentID == mediaId || it.isPlaying){
            val newItem = it.copy()
            newItem.isPlaying = it.contentID == mediaId || !it.isPlaying
            newList.add(newItem)
        }else{
            newList.add(it)
        }
    }
    return newList
}
fun List<AlbumContents.Data>?.albumNewList(mediaId: String?): List<AlbumContents.Data> {
    val newList:MutableList<AlbumContents.Data> = ArrayList()
    if(mediaId == null && this!= null){
        return this
    }
    this?.forEach {
        if(it.contentID == mediaId || it.isPlaying){
            val newItem = it.copy()
            newItem.isPlaying = it.contentID == mediaId || !it.isPlaying
            newList.add(newItem)
        }else{
            newList.add(it)
        }
    }
    return newList
}

fun List<CategoryContents.Data>?.categoryNewList(mediaId: String?): List<CategoryContents.Data> {
    val newList:MutableList<CategoryContents.Data> = ArrayList()
    if(mediaId == null && this!= null){
        return this
    }
    this?.forEach {
        if(it.contentID == mediaId || it.isPlaying){
            val newItem = it.copy()
            newItem.isPlaying = it.contentID == mediaId || !it.isPlaying
            newList.add(newItem)
        }else{
            newList.add(it)
        }
    }
    return newList
}
fun List<PodcastShowModel.TrackList>?.trackNewList(mediaId: String?): List<PodcastShowModel.TrackList> {
    val newList:MutableList<PodcastShowModel.TrackList> = ArrayList()

    if(mediaId == null && this!= null){
        return this
    }
    this?.forEach {
        if(it.id.toString() == mediaId || it.isPlaying){
            val newItem = it.copy()
            newItem.isPlaying = it.id.toString() == mediaId || !it.isPlaying
            newList.add(newItem)
        }else{
            newList.add(it)
        }
    }
    return newList
}
fun List<PlaylistData>?.dataNewList(mediaId: String?): List<PlaylistData> {
    val newList:MutableList<PlaylistData> = ArrayList()
    if(mediaId == null && this!= null){
        return this
    }
    this?.forEach {
        if(it.contentID == mediaId || it.isPlaying){
            val newItem = it.copy()
            newItem.isPlaying = it.contentID == mediaId || !it.isPlaying
            newList.add(newItem)
        }else{
            newList.add(it)
        }
    }
    return newList
}

fun PodcastShowModel.TrackList.toMusic(): Music {
    val podType = if (this.trackType != null && this.trackType.equals(
            Constants.LIVE_CONTENT,
            ignoreCase = true
        )
    ) "@" + this.contentType else this.contentType
    return Music(
        mediaId = id.toString(),
        artistName = showName,
        title = name,
        displayIconUrl = CharParser.getImageFromTypeUrl(imageUrl, ""),
        mediaUrl = playUrl,
        displayDescription = name,
        date = ceateDate,
        contentType = podType,
        userPlayListId = episodeId,
        episodeId = episodeId,
        starring = starring,
        seekable = seekable,
        details = details,
        totalStream = totalStream,
        fav = fav,
        trackType = trackType,
        isPaid = isPaid,
    )
}
fun PodcastExplore.InsideData.toMusic(): Music {
    val podType = if (trackType != null && trackType.equals(
            Constants.LIVE_CONTENT, ignoreCase = true
        )
    ) "@$contentType" else contentType
    val artistName = if (showName.isEmpty()) episodeName else showName
    return Music(
        mediaId = tracktId.toString(),
        title = trackName,
        displayDescription = about,
        displayIconUrl = CharParser.getImageFromTypeUrl(imageUrl, ""),
        mediaUrl = playUrl,
        artistName = artistName,
        date = ceateDate,
        contentType = podType,
        userPlayListId = episodeId,
        episodeId = episodeId,
        seekable = seekable,
        fav = fav,
        trackType = trackType,
        isPaid = isPaid,
    )
}
fun AlbumContents.Data.toMusic() = Music(
    mediaId = contentID,
    artistName = artist,
    title = title,
    displayDescription = labelname,
    displayIconUrl = CharParser.getImageFromTypeUrl(image, Constants.CONTENT_TYPE_ALBUM),
    mediaUrl = playUrl,
    date = releaseDate,
    contentType = contentType,
    fav = fav,
).applyRootInfo(
    rootId = rootId,
    rootType = rootType,
)
fun PlaylistContents.Data.toMusic(): Music {
    return Music(
        mediaId = contentID,
        title = title,
        displayDescription = labelname,
        displayIconUrl = CharParser.getImageFromTypeUrl(image, Constants.CONTENT_TYPE_PLAYLIST),
        mediaUrl = playUrl,
        artistName = artist,
        date = releaseDate,
        contentType = contentType,
        fav = fav,

    ).applyRootInfo(
        rootId = rootId,
        rootType = rootType,
        rootTitle = rootTitle,
        rootImage = rootImage
    )
}
fun CategoryContents.Data.toMusic(): Music {
    return Music(
        mediaId = contentID,
        title = title,
        displayDescription = "",
        date = "",
        displayIconUrl = CharParser.getImageFromTypeUrl(image, contentType),
        mediaUrl = playUrl,
        artistName = artist,
        contentType = contentType,
        userPlayListId = albumId,
        fav = fav,
        trackType = trackType,
        isPaid = isPaid,
    ).applyRootInfo(
        rootId = rootId,
        rootType = rootType,
        rootTitle = rootTitle,
        rootImage = rootImage
    )
}
fun Music.toCategoryContentsData():CategoryContents.Data{
   return CategoryContents.Data().apply {
        this.contentID = this@toCategoryContentsData.mediaId
        this.title = this@toCategoryContentsData.title
        this.image = this@toCategoryContentsData.displayIconUrl
        this.artist = this@toCategoryContentsData.artistName
        this.contentType = this@toCategoryContentsData.contentType
        this.albumId = this@toCategoryContentsData.userPlayListId
        this.playUrl = this@toCategoryContentsData.mediaUrl
        this.trackType = this@toCategoryContentsData.trackType
        this.isPaid = this@toCategoryContentsData.isPaid ==true
        this.fav = this@toCategoryContentsData.fav
    }
}
fun List<ArtistContents.Data>.artistCopyList(): MutableList<ArtistContents.Data> {

    val newList:MutableList<ArtistContents.Data> = ArrayList()
    this.forEach { data ->
        newList.add(data.copy())
    }
    return newList
}
fun List<AlbumContents.Data>.albumCopyList(): MutableList<AlbumContents.Data> {

    val newList:MutableList<AlbumContents.Data> = ArrayList()
    this.forEach { data ->
        newList.add(data.copy())
    }
    return newList
}
fun List<PlaylistContents.Data>.playlistCopyList(): MutableList<PlaylistContents.Data> {

    val newList:MutableList<PlaylistContents.Data> = ArrayList()
    this.forEach { data ->
        newList.add(data.copy())
    }
    return newList
}
fun List<ArtistContents.Data>.artistFindIndexByID(contentID:String): Int {
    return this.indexOfFirst { data ->
        data.contentID == contentID
    }
}
fun List<AlbumContents.Data>.albumTrackFindIndexByID(contentID:String): Int {
    return this.indexOfFirst { data ->
        data.contentID == contentID
    }
}
fun List<PlaylistContents.Data>.playlistFindIndexByID(contentID:String): Int {
    return this.indexOfFirst { data ->
        data.contentID == contentID
    }
}

fun List<CategoryContents.Data>.copyList(): MutableList<CategoryContents.Data> {

    val newList:MutableList<CategoryContents.Data> = ArrayList()
    this.forEach { data ->
        newList.add(data.copy())
    }
    return newList
}
fun List<*>.toCategoryContentsList(): List<CategoryContents.Data?> {
    return map { data ->
        if(data is Music) data.toCategoryContentsData() else null
    }
}
fun PlaylistData.toMusic(): Music {
    return Music(
        mediaId = contentID,
        title = title,
        displayDescription = labelname,
        date = releaseDate,
        displayIconUrl = CharParser.getImageFromTypeUrl(image, contentType),
        mediaUrl = playUrl,
        artistName = artist,
        contentType = contentType,
        userPlayListId = userPlayListId,
    )
}
fun List<*>.toMusicPlayList(rootId:String?=null,rootType:String?=null,rootTitle:String?=null,rootImage:String?=null): MusicPlayList {
    val list = this.map { song ->
        val music = song.anyToMusic()
        music.setRootInfo(rootId = rootId, rootType = rootType, rootTitle = rootTitle, rootImage = rootImage)
        music
    }
    return MusicPlayList(list)
}
fun List<*>.toMusicPlayList(): MusicPlayList {
    val list = this.map { song -> song.anyToMusic() }
    return MusicPlayList(list)
}*/
/*fun Any?.anyToMusic(): Music {
    if(this == null){
        return  Music()
    }
    return when(this){
        is ArtistContents.Data -> this.toMusic()
        is PodcastShowModel.TrackList -> this.toMusic()
        is PodcastExplore.InsideData -> this.toMusic()
        is AlbumContents.Data -> this.toMusic()
        is PlaylistContents.Data -> this.toMusic()
        is CategoryContents.Data -> this.toMusic()
        is PlaylistData -> this.toMusic()
        is MediaBrowserCompat.MediaItem -> this.toMusic()
        is MediaMetadataCompat -> this.toMusic() ?: Music()
        is RecommendedSongsContents.RecommendedSong -> this.toMusic()
        else -> Music()
    }
}

private fun RecommendedSongsContents.RecommendedSong.toMusic(): Music {
  return  convertToCategoryContent(this).toMusic()
}*/

internal fun Bundle.toMusicPlayList(command: ShadhinMusicServiceConnection.Command): MusicPlayList? {
    val serializable = this.getSerializable(command.dataKey)
    if (serializable is MusicPlayList) {
        return serializable
    }
    return null

}

internal fun Bundle?.toPlayerProgress(key: String): PlayerProgress? {
    val serializable = this?.getSerializable(key)
    if (serializable is PlayerProgress) {
        return serializable
    }
    return null
}

internal fun List<MediaBrowserCompat.MediaItem>.toMusicList() =
    this.map { mediaItem -> mediaItem.toMusic() }

inline val PlaybackStateCompat.isPrepare: Boolean
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING ||
            state == PlaybackStateCompat.STATE_STOPPED ||
            state == PlaybackStateCompat.STATE_ERROR ||
            state == PlaybackStateCompat.STATE_PAUSED

inline val PlaybackStateCompat.isPlaying
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PLAYING

inline val PlaybackStateCompat.isPaused
    get() = state == PlaybackStateCompat.STATE_BUFFERING ||
            state == PlaybackStateCompat.STATE_PAUSED

inline val PlaybackStateCompat.isEndOfQueue
    get() = state == PlaybackStateCompat.ERROR_CODE_END_OF_QUEUE


inline val PlaybackStateCompat.isBuffering
    get() = state == PlaybackStateCompat.STATE_BUFFERING


internal fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun Long.toSecond(): Long {
    return this / 1000 % 60
}

fun Long.toDateTimeString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault())
    return format.format(date)
}

/*fun newDownloadedSong(oldDownload:List<CategoryContents.Data>, newDownlaod:List<CategoryContents.Data>): List<CategoryContents.Data> {
    if(oldDownload.isNullOrEmpty()){
        return newDownlaod
    }
   return newDownlaod.filter { new ->
       val index =  oldDownload.indexOfFirst { old -> old.contentID == new.contentID }
       index == -1
   }
}*/

/*fun List<CategoryContents.Data>.isSame(list:List<CategoryContents.Data>): Boolean {
    if(this.isNullOrEmpty() && list.isNullOrEmpty()){
        return true
    }
    this.forEach { data ->
        val index = list.indexOfFirst { s ->  s.contentID == data.contentID}
        if(index == -1){
            return false
        }
    }
    return true
}*/
fun String?.isLocalUrl(): Boolean {
    return this.isMp3LocalUrl() || this.isMp4LocalUrl()
}

fun String?.isMp3LocalUrl(): Boolean {
    return this.isMp3Url() //&& this?.contains(BuildConfig.APPLICATION_ID,true) == true
}

fun String?.isMp4LocalUrl(): Boolean {
    return this.isMp4Url() //&& this?.contains(BuildConfig.APPLICATION_ID,true) == true
}

fun String?.isMp3Url(): Boolean {
    return this?.let { Regex(regexMp3Url).matches(it) } == true
}

fun String?.isMp4Url(): Boolean {
    return this?.let { Regex(regexMp4Url).matches(it) } == true
}

fun String?.makeValidUrl(): String {
    return when {
        this.isNullOrEmpty() -> Constants.FILE_BASE_URL + "demo.mp3"
        this.isLocalUrl() -> this
        //   this.isEncryptedUrl() -> this.decryptStr()?:this
        this.isMp3Url() && this.isMp4Url() -> "${Constants.FILE_BASE_URL}${this}"
        else -> this
    }
}

fun String.makeValidMp4Url(): String {
    return when {
        // this.isEncryptedUrl() -> this.decryptStr()?:this
        this.isMp4LocalUrl() -> this
        this.isMp4Url() -> "${Constants.FILE_BASE_URL}${this}"
        else -> this
    }
}