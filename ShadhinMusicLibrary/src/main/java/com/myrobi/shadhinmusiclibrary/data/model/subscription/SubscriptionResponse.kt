package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.Keep

@Keep
internal sealed class SubscriptionResponse(val redirectURL:String) {
    class Robi(redirectURL:String):SubscriptionResponse(redirectURL)
    class Grameenphone(redirectURL:String):SubscriptionResponse(redirectURL)
    class Banglalink(redirectURL:String):SubscriptionResponse(redirectURL)
}