package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel

internal interface DownloadClickCallBack {
    fun clickOnDownload(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnWatchLater(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnMyPlaylist(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnMyFavorite(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnPodcast(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnArtist(selectedHomePatchItem: HomePatchItemModel)

}