package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep
import java.io.Serializable
import java.util.Date

@Keep
internal interface Plan  {
     val title:String?
     val duration:String?
     val serviceId:String?
     val type:Type?
     val status:Status
     val amount:Float?
     val amountWithCurrency:String?
     val currency:Currency?
     val registerDate:Date?
     val isAutoRenewal: Boolean?
     val extraVatText:String?
     val desc: String?
}