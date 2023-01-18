package com.myrobi.shadhinmusiclibrary.data.model.subscription


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@Keep
data class SubscriptionPlan(
    @SerializedName("MSISDN")
    @Expose
    override val msisdn: String? = null,
    @SerializedName("RegDate")
    @Expose
    val regDate: String? = null,
    @SerializedName("RegStatus")
    @Expose
    val regStatus: String? = null,
    @SerializedName("Score")
    @Expose
    val score: Any? = null,
    @SerializedName("ServiceId")
    @Expose
    override val serviceId: String? = null,
    @SerializedName("isAutoRenewal")
    @Expose
    override val isAutoRenewal: Boolean = false,
    override val amount: Float? = null,
    override val type: Type? = null,
    override val currency: Currency = Currency.BDT

) :Plan{

    override val status: Status
        get() = if(regStatus.equals("Subscribed",true)) Status.SUBSCRIBED else Status.UNSUBSCRIBED

    override val registerDate: Date?
        get() = kotlin.runCatching {
            SimpleDateFormat("yyyy-MM-ddTHH:mm:ss",Locale.getDefault()).parse(regDate?:"")
        }.getOrNull()

}