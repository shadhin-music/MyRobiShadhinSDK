package com.shadhinmusiclibrary.utils


internal object AppConstantUtils {

    const val requestType = "RequestType"

    // public static final String API_URL = "http://api.shadhin.co/api/";
    var BASE_URL = "https://shadhinmusic.com/api/"
    var BASE_URL_API_shadhinmusic = "https://api.shadhinmusic.com/api/v5/"
    const val LAST_FM_API_URL = "https://ws.audioscrobbler.com/2.0/"
    const val BILLBOARD_IMAGE_URL = "https://shadhinmusiccontent.sgp1.digitaloceanspaces.com/BillBoard/Web/"

    const val SingleDataItem = "single_data_item"
    const val PatchItem = "patch_item"

    const val Artist = "artist"
    const val Album = "album"
    const val PatchDetail = "patch_detail"
    const val PatchDataItem = "patch_data_item"
    const val SongDetail = "songDetail"
    const val commonData = "data"
    const val SelectedIndex = "selected_index"
    const val SelectedPatchIndex = "selected_patch_index"
    const val LAST_FM_MIN_BIO_CHAR = 100
    const val LAST_FM_API_KEY = "55dd9a0fd0790ee3219022141a8cdf39"
    const val LAST_FM_CONTENT_TYPE_JSON = "json"

    const val DataContentRequestId = "DataContentRequestId"
    const val UI_Request_Type = "UIName"
    const val Requester_Name_Home = "HomeFragment"
    const val Requester_Name_Artist_Details = "ArtistDetails"
    const val Requester_Name_Search = "SearchFragment"
    const val Requester_Name_Download = "DownloadFragment"
    const val Requester_Name_Watchlater = "WatchlaterFragment"
    const val Requester_Name_MyPlaylist = "MyPlaylistFragment"
    const val Requester_Name_MyFavorite = "MyFavoriteFragment"
    const val Requester_Name_CreatePlaylist = "CreatePlaylistFragment"
    const val Requester_Name_CreatedPlaylistDetails = "CreatedPlaylistDetails"
    const val Requester_Name_API = "AllMusic"
    const val RequesterHomePatch= "all_home_patch"
    const val RequesterRC = "RC_CODE"
    const val PlaylistId = "PlaylistId"
    const val PlaylistName = "PlaylistName"
    const val PlaylistGradientId = "PlaylistGradientId"
}