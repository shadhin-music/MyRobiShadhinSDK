package com.myrobi.shadhinmusiclibrary.data.model.subscription


import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.myrobi.shadhinmusiclibrary.utils.format
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
    override val isAutoRenewal: Boolean? = false,
    override val amount: Float? = null,
    override val type: Type? = null,
    override val currency: Currency = Currency.BDT,
    override val extraVatText: String? = null
) :Plan{
    override val title: String?
        get() = type?.name?:"Other"
    override val duration: String?
        get(){
           val days =  type?.duration?.inWholeDays?:return null
           return if(days<=1) "$days day" else "$days days"
        }
    override val amountWithCurrency: String?
        get() = "${currency.symbol}${amount?.format(2)}"
    override val status: Status
        get() = if(regStatus.equals("Subscribed",true)) Status.SUBSCRIBED else Status.UNSUBSCRIBED

    override val registerDate: Date?
        get() = kotlin.runCatching {
            SimpleDateFormat("yyyy-MM-ddTHH:mm:ss",Locale.getDefault()).parse(regDate?:"")
        }.getOrNull()

}