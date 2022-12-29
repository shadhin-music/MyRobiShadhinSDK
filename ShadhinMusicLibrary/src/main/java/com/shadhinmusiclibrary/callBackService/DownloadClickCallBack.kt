package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.model.HomePatchItemModel

internal interface DownloadClickCallBack {
    fun clickOnDownload(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnWatchLater(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnMyPlaylist(selectedHomePatchItem: HomePatchItemModel)
    fun clickOnMyFavorite(selectedHomePatchItem: HomePatchItemModel)
}