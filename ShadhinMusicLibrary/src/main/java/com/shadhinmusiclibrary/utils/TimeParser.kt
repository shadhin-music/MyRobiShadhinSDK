package com.shadhinmusiclibrary.utils

internal object TimeParser {
    fun secToMin(duration: String?): String {
        var mDuration = duration
        if (mDuration == null) {
            mDuration = ""
        }
//        if (mDuration.isEmpty()) {
//            return "0:0"
//        }
        val time: Int = try {
            mDuration.toInt()
        } catch (e: NumberFormatException) {
            return duration ?: ""
        }
        val min = time / 60
        val seconds = time % 60
        val length = seconds.toString().length
        return if (length == 1) {
            "$min:0$seconds"
        } else {
            "$min:$seconds"
        }
    }

//    fun millisecToMin(duration: Int): String? {
//        val mns = duration / 60000 % 60000
//        val scs = duration % 60000 / 1000
//        return String.format("%2d:%02d", mns, scs)
//    }
}