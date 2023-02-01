package com.myrobi.shadhinmusiclibrary.data.model.profile


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Data(
    @SerializedName("BirthDate")
    var birthDate: Any? = null,
    @SerializedName("City")
    var city: Any? = null,
    @SerializedName("Country")
    var country: Any? = null,
    @SerializedName("CountryCode")
    var countryCode: Any? = null,
    @SerializedName("Gender")
    var gender: Any? = null,
    @SerializedName("PhoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("RegisterWith")
    var registerWith: List<String>? = null,
    @SerializedName("UserFullName")
    var userFullName: Any? = null,
    @SerializedName("UserPic")
    var userPic: Any? = null
)