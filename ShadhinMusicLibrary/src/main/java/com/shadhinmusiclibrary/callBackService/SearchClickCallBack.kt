package com.shadhinmusiclibrary.callBackService

import com.shadhinmusiclibrary.data.model.HomePatchItemModel

internal interface SearchClickCallBack {
    fun clickOnSearchBar(selectedHomePatchItem: HomePatchItemModel)
}