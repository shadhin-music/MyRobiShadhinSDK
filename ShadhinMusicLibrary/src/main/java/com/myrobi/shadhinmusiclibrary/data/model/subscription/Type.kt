package com.myrobi.shadhinmusiclibrary.data.model.subscription

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import com.myrobi.shadhinmusiclibrary.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/*sub.title.contains("Daily", true)       ->  R.drawable.ic_daily_sub
sub.title.contains("Monthly", true)     ->  R.drawable.ic_monthly_sub
sub.title.contains("Half Yearly", true) ->  R.drawable.ic_half_yearly_sub
sub.title.contains("Yearly", true)      ->  R.drawable.ic_yearly_sub
else                                                   ->  R.drawable.ic_daily_sub*/


@Keep
internal sealed class Type(val name:String,@DrawableRes val icon:Int,val duration: Duration) {
    object Daily:Type("Daily", R.drawable.ic_daily_sub,1.days)
    object Monthly:Type("Monthly",R.drawable.ic_monthly_sub,30.days)
    object HalfYearly:Type("HalfYearly",R.drawable.ic_half_yearly_sub,182.days)
    object Yearly:Type("Yearly",R.drawable.ic_yearly_sub,365.days)

    class Coupon(duration: Duration):Type("Coupon",R.drawable.ic_daily_sub,duration)
}


