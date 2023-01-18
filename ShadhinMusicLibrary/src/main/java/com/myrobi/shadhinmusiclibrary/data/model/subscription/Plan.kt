package com.myrobi.shadhinmusiclibrary.data.model.subscription

import java.util.Date


interface Plan{
     val serviceId:String?
     val msisdn:String?
     val type:Type?
     val status:Status
     val amount:Float?
     val currency:Currency?
     val registerDate:Date?
     val isAutoRenewal: Boolean
}