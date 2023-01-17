package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class ArtistContentModel(
    val MonthlyListener: String,
    val `data`: MutableList<ArtistContentDataModel>,
    val fav: String,
    val follow: String,
    val image: String,
    val message: String,
    val status: String,
    val total: Int,
    val type: String,
    val name:String


) {

    fun getImageUrl300Size(): String {
        return this.image.replace("<\$size\$>", "300")
    }
}