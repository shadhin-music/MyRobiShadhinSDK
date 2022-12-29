package com.shadhinmusiclibrary.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
internal data class DownloadingItem(val contentId:String, val progress:Float ):Parcelable
