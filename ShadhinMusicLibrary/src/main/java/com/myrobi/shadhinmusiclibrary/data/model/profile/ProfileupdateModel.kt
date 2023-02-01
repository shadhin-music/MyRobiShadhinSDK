package com.myrobi.shadhinmusiclibrary.data.model.profile

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import java.io.File

@Keep
internal data class ProfileupdateModel(var name: String, var birthday: String, var gender: String, var profileImageFile: File)

