package com.myrobi.shadhinmusiclibrary.fragments.create_playlist


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Keep
internal data class UserPlaylistData(
    @SerializedName("Data")
    var `data`: List<PlaylistContent>?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?
):Serializable