package com.shadhinmusiclibrary.library.player.utils.convater

import com.shadhinmusiclibrary.data.model.ArtistContentDataModel
import com.shadhinmusiclibrary.library.player.Constants.FILE_BASE_URL
import com.shadhinmusiclibrary.library.player.data.model.Music
import com.shadhinmusiclibrary.library.player.utils.CharParser

internal class ArtistContentToMusic(private val data: ArtistContentDataModel) : MusicConverter {
    override fun convert(): Music {
        return Music(
            mediaId = data.content_Id,
            title = data.titleName,
            displayDescription = null,
            displayIconUrl = CharParser.getImageFromTypeUrl(data.imageUrl, "A"),
            mediaUrl = "${FILE_BASE_URL}${data.playingUrl}",
            artistName = data.artistName,
            date = null,
            contentType = null,
            userPlayListId = null,
            episodeId = null,
            starring = null,
            seekable = null,
            details = null,
            totalStream = null,
            fav = null,
            trackType = null,
            isPaid = null,
            rootId = null,
            rootType = null,
            rootTitle = null,
            rootImage = null,
            isPrepare = null,
            isPlaying = null
        )
    }
}