package com.shadhinmusiclibrary.data

import androidx.annotation.Keep

@Keep
interface IMusicModel {
    var content_Id: String
    var content_Type: String?
    var titleName: String?
    var bannerImage: String?
    var imageUrl: String?
    var playingUrl: String?
    var artist_Id: String?
    var artistName: String?
    var album_Id: String?
    var album_Name: String?
    var total_duration: String?
    var trackType: String?


    var rootContentId: String?
    var rootContentType: String?
    var rootImage: String?
    var isPlaying: Boolean
    var isSeekAble: Boolean?
}