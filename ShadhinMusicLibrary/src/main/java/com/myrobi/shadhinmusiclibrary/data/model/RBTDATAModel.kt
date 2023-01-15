package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
internal data class RBTDATAModel(
    val pwaUrl: String,
    val pwatopchartURL: String,
    val redirectUrl: String
) : Serializable