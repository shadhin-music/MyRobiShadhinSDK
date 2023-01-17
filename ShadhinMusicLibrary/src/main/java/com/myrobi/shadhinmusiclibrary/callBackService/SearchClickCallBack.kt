package com.myrobi.shadhinmusiclibrary.callBackService

import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel

internal interface SearchClickCallBack {
    fun clickOnSearchBar(selectedHomePatchItem: HomePatchItemModel)
}