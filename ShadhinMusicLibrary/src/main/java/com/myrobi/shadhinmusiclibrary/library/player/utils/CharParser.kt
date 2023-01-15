package com.myrobi.shadhinmusiclibrary.library.player.utils

internal object CharParser {
    /**
     * Replaces <...> tag inside the url with a image size
     *
     * @param url  of the image
     * @param type of the image
     * @return replaced url
     */
    fun getImageFromTypeUrl(url: String?, type: String?): String {
        return if (url == null || url.isEmpty()) {
            ""
        } else when (type) {
            "A" -> url.replace("</?.*?>".toRegex(), "300")
            "PDB" -> url.replace("</?.*?>".toRegex(), "450")
            "M" -> url.replace("</?.*?>".toRegex(), "1200")
            "V" -> url.replace("</?.*?>".toRegex(), "1280")
            "PP" -> url.replace("</?.*?>".toRegex(), "320")
            "PL" -> url.replace("</?.*?>".toRegex(), "225")
            "PLB" -> url.replace("</?.*?>".toRegex(), "780")
            else -> url.replace("</?.*?>".toRegex(), "300")
        }
    }

    /**
     * Removes empty spaces
     *
     * @param data to be processed
     * @return replaced data
     */
    fun removeEmptySpaces(data: String): String {
        return data.replace("\\s+".toRegex(), "")
    }

    /**
     * Removes multiple spaces and replaces them with a single space
     *
     *
     * https://stackoverflow.com/questions/2932392/java-how-to-replace-2-or-more-spaces-with-single-space-in-string-and-delete-lead
     *
     * @param data to be processed
     * @return replaced data
     */
    fun replaceMultipleSpaces(data: String): String {
        return data.replace("\\s{2,}".toRegex(), " ").trim { it <= ' ' }
        //return data.replaceAll("^ +| +$|( )+", "$1");
    }
}
internal class TestCat{
    fun getName(): String {
        return "cat cat"
    }
}