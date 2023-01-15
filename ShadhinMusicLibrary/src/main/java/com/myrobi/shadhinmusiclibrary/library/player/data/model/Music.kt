package com.myrobi.shadhinmusiclibrary.library.player.data.model

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.Keep
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.utils.getPreloadBitmap

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.myrobi.shadhinmusiclibrary.utils.exH
import com.myrobi.shadhinmusiclibrary.utils.find

import java.io.Serializable

internal const val regexMp3Url = "\\w[A-Za-z\\S ]+(\\b.mp3\\b)"
internal const val regexMp4Url = "\\w[A-Za-z\\S ]+(\\b.mp4\\b)"

@Keep
internal data class Music(
    var mediaId: String? = null,
    var title: String? = null,
    var displayDescription: String? = null,
    var displayIconUrl: String? = null,
    var mediaUrl: String? = null,
    var artistName: String? = null,
    var date: String? = null,
    var contentType: String? = null,
    var userPlayListId: String? = null,

    var episodeId: String? = null,
    var starring: String? = null,
    var seekable: Boolean? = null,
    var details: String? = null,
    var totalStream: Long? = null,
    var fav: String? = null,
    var trackType: String? = null,
    var isPaid: Boolean? = null,

    var rootId: String? = null,
    var rootType: String? = null,
    var rootTitle: String? = null,
    var rootImage: String? = null,

    var isPrepare: Boolean? = false,
    var isPlaying: Boolean? = false,
) : Serializable {

    fun toPlayerMediaItem() = MediaItem.Builder()
        .setMediaId(mediaId.toString())
        .setUri(mediaUrl)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(title)
                .setDescription(displayDescription)
                .setExtras(toBundleMetaData("toPlayerMediaItem"))
                .setArtist(artistName)
                .setArtworkUri(Uri.parse(displayIconUrl))
                .setMediaUri(Uri.parse(mediaUrl))
                //.setMediaUri(Uri.parse("https://shadhinmusiccontent.sgp1.digitaloceanspaces.com/AudioMainFile/Oporadhi_SamzVai.mp3?AWSAccessKeyId=6PGAOEQXQZJQTM6YYXWA&Expires=1645952479&Signature=tC%2B1sIw3PftQEr5ihpUzNBftQZw%3D") )
                .build()
        )
        .build()

    fun applyRootInfo(meta: Music?): Music = applyRootInfo(
        rootId = meta?.rootId,
        rootType = meta?.rootType,
        rootTitle = meta?.rootTitle,
        rootImage = meta?.rootImage
    )

    fun applyRootInfo(
        rootId: String? = null,
        rootType: String? = null,
        rootTitle: String? = null,
        rootImage: String? = null
    ): Music {
        setRootInfo(
            rootId = rootId,
            rootType = rootType,
            rootTitle = rootTitle,
            rootImage = rootImage
        )
        return this
    }

    fun setRootInfo(
        rootId: String? = null,
        rootType: String? = null,
        rootTitle: String? = null,
        rootImage: String? = null
    ) {

        if (this.rootId.isNullOrEmpty() && rootId != null) {
            this.rootId = rootId
        }
        if (this.rootType.isNullOrEmpty() && rootType != null) {
            this.rootType = rootType
        }
        if (this.rootTitle.isNullOrEmpty() && rootTitle != null) {
            this.rootTitle = rootTitle
        }
        if (this.rootImage.isNullOrEmpty() && rootImage != null) {
            this.rootImage = rootImage
        }
    }

    fun toBundleMetaData(from: String? = "Unknown"): Bundle {
        return Bundle().apply {
            putString(CONVERT_FROM_KEY, from)
            putString(CONTENT_TYPE, contentType)
            putString(ARTIST_NAME, artistName)
            putString(USER_PLAYLIST_ID, userPlayListId)
            putString(DATE, date)
            putString(TRACK_TYPE, trackType)
            putString(EPISODE_ID, episodeId)
            putString(FAV, fav)
            putString(DETAILS, details)
            putString(MEDIA_ID, mediaId)
            putBoolean(IS_PAID, isPaid == true)

            putString(ROOT_ID, rootId)
            putString(ROOT_TYPE, rootType)
            putString(ROOT_TITLE, rootTitle)
            putString(ROOT_IMAGE, rootImage)
            putString(SEEKABLE, seekable.toString())
            totalStream?.let { putLong(TOTAL_STREAM, it) }
            // putBoolean(SEEKABLE, seekable ?: false)
            putString(
                MediaMetadataCompat.METADATA_KEY_ARTIST,
                artistName
            ) //This string I put for display artist name samsung One UI lock screen
        }
    }

    fun toServiceMediaItem(): MediaBrowserCompat.MediaItem {

        val description = MediaDescriptionCompat.Builder()
            .setMediaUri(Uri.parse(mediaUrl))
            .setTitle(title)
            .setDescription(displayDescription)
            .setExtras(toBundleMetaData("toServiceMediaItem"))
            .setSubtitle(artistName)
            .setMediaId(mediaId.toString())
            .setIconUri(Uri.parse(displayIconUrl))
            .setIconBitmap(getPreloadBitmap(mediaId.toString())) //I put for display image name samsung One UI lock screen
            .build()

        return MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }

    fun endContentType(): String? {
        if (contentType?.length == 4) {
            return contentType?.substring(2)
        }
        return contentType
    }

    fun isPodCast(): Boolean {
        return contentType != null && contentType?.contains("PD", true) == true
    }

    fun isVideo(): Boolean {
        return contentType != null && contentType?.contains("VD", true) == true
    }

    fun podcastSubType(): String? {
        if (isPodCast()) {
            return contentType?.toUpperCase()?.split("PD")?.last()
        }
        return null
    }
    fun podcastTrackType(): String? {
        if (isPodCast()) {
            return trackType?.toUpperCase()
        }
        return null
    }

    fun isLive(): Boolean {
        return trackType.equals("LM", true)
    }

    fun fileName(): String? {
        return mediaUrl?.find(regexMp3Url)?.first()?.trim()
    }

    fun filePath(): String? {
        return exH { mediaUrl?.replace(Constants.FILE_BASE_URL, "") } ?: mediaUrl
    }

    override fun toString(): String {
        return "Music(mediaId=$mediaId, title=$title , contentType = $contentType mediaUrl = $mediaUrl)"
    }

    companion object {
        const val USER_PLAYLIST_ID = "user_playlist_id"
        const val SERIALIZABLE_KEY = "serializable_object"
        const val CONVERT_FROM_KEY = "convert_from"

        const val MEDIA_ID = "music_mediaId"
        const val TITLE = "music_title"
        const val DISPLAY_DESCRIPTION = "music_displayDescription"
        const val DISPLAY_ICON_URL = "music_displayIconUrl"
        const val MEDIA_URL = "music_mediaUrl"
        const val ARTIST_NAME = "music_artistName"
        const val DATE = "music_date"
        const val CONTENT_TYPE = "music_contentType"
        const val USER_PLAY_LIST_ID = "music_userPlayListId"
        const val EPISODE_ID = "music_episodeId"
        const val STARRING = "music_starring"
        const val SEEKABLE = "music_seekable"
        const val DETAILS = "music_details"
        const val TOTAL_STREAM = "music_totalStream"
        const val FAV = "music_fav"
        const val TRACK_TYPE = "music_trackType"
        const val IS_PAID = "music_isPaid"

        const val IS_PREPARE = "music_isPrepare"
        const val IS_PLAYING = "music_isPlaying"

        const val ROOT_ID = "music_rootId"
        const val ROOT_TYPE = "music_rootType"
        const val ROOT_TITLE = "music_rootTitle"
        const val ROOT_IMAGE = "music_rootImage"
    }
}