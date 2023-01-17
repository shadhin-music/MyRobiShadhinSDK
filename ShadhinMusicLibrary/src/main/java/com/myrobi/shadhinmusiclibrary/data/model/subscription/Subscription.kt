package com.myrobi.shadhinmusiclibrary.data.model.subscription

import java.util.Date


interface Subscription{
     val serviceId:String?
     val msisdn:String?
     val plan:Plan?
     val status:Status
     val amount:Float?
     val currency:Currency?
     val registerDate:Date?
     val isAutoRenewal: Boolean
}