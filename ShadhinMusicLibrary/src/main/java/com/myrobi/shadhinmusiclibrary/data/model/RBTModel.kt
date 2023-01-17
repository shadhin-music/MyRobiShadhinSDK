package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep

@Keep
internal data class RBTModel(
    val `data`: RBTDATAModel,
    val message: String,
    val status: String
)