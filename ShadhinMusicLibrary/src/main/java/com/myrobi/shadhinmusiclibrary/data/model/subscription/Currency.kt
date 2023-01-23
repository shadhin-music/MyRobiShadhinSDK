package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep

@Keep
internal enum class Currency(val symbol:Char) {
    BDT('৳'),
    USD('$')
}