package com.myrobi.shadhinmusiclibrary.utils.share

import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.utils.fromBase64
import com.myrobi.shadhinmusiclibrary.utils.toBase64

class ShareRC(override val code: String) : Share {
    private val token: List<String>? by lazy { decodeToken() }

    override val id: String?
        get() = token?.first()
    override val type: String?
        get() = token?.last()

    override val category: ShareCategory
        get() = when{
            type?.contains("PD",true) == true -> ShareCategory.PODCAST
            type?.contains("Patch",true) == true -> ShareCategory.PATCH
            else -> ShareCategory.CONTENTS
        }

    val podcastSubType: String?
        get() = if (category == ShareCategory.PODCAST && (type?.length ?: 0) >= 4)
            type?.substring(2, (type?.length ?: 0))
        else null

    private fun decodeToken(): List<String>? {
        val str = code.fromBase64()
        val tokens = str.split("_")
        if (tokens.size == 2) {
            return tokens
        }
        return null
    }

    override fun toString(): String {
        return "ShareRC(code='$code', id=$id, type=$type, category=$category, podcastSubType=$podcastSubType)"
    }


    companion object {
        @JvmStatic
        fun generate(iMusicModel: IMusicModel): ShareRC {
            return generate(iMusicModel.content_Id, iMusicModel.content_Type ?: "")
        }

        @JvmStatic
        fun generate(contentId: String?, contentType: String?): ShareRC {
            val token = "${contentId ?: ""}_${contentType ?: ""}".toBase64()
            return ShareRC(token)
        }
        @JvmStatic
        fun generate(patchCode:String?): ShareRC {
            val token = "${patchCode ?: ""}_Patch".toBase64()
            return ShareRC(token)
        }
    }

}

