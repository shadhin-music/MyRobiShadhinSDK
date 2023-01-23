package com.myrobi.shadhinmusiclibrary.data.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
data class HomePatchItemModel(
    val Code: String,
    var ContentType: String,
    var Data: List<HomePatchDetailModel>,
    var Design: String,
    val Name: String,
    val Sort: Int,
    val Total: Int,
    var customData:Any?=null
) : Serializable{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomePatchItemModel

        if (Code != other.Code) return false
        if (ContentType != other.ContentType) return false
        if (Design != other.Design) return false
        if (Name != other.Name) return false
        if (Sort != other.Sort) return false
        if (Total != other.Total) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Code.hashCode()
        result = 31 * result + ContentType.hashCode()
        result = 31 * result + Design.hashCode()
        result = 31 * result + Name.hashCode()
        result = 31 * result + Sort
        result = 31 * result + Total
        return result
    }
}