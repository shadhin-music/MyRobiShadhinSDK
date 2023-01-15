package com.myrobi.shadhinmusiclibrary.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T> Map<String, Any>.toObject(): T {
    return convert()
}

fun <T> T.toMap(): Map<String, Any> {
    return convert()
}

inline fun <T, reified R> T.convert(): R {
    val json = Gson().toJson(this)
    return Gson().fromJson(json, object : TypeToken<R>() {}.type)
}