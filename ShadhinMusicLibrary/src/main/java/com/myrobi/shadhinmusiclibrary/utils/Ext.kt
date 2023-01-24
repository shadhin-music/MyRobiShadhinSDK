package com.myrobi.shadhinmusiclibrary.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Html
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.annotation.RawRes
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.lang.reflect.Modifier
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @AUTHOR: Mehedi Hasan
 * @DATE: 2/25/2021, Thu
 */

internal fun View.hide() {
    this.visibility = View.GONE
}

internal fun tests(block: String.() -> Unit) {

}

internal fun View.showKeyboard() {
    post {
        if (this.requestFocus()) {
            val inputManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}

internal val ukey2 = "STEHLGCilw_Y9_11qcW8"

internal fun View.hideKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

internal fun Int.toDp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (16.0f * scale + 0.5f).toInt()
}

internal fun String.preContentType(): String? {
    return when (this.length) {
        4 -> this.substring(0..1)
        else -> null
    }
}

internal fun String.postContentType(): String? {
    return when (this.length) {
        4 -> this.substring(2..3)
        else -> null
    }
}

/*fun main(){
    "VDKC".preContentType().also(::println)
    "VDKC".postContentType().also(::println)
}*/
internal val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
internal val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
internal val ukey = "AIzaSyAO_FJ2SlqU8Q4"

internal val ykeyTotal = ukey + ukey2


internal fun createTimeLabel(second: Long): String? {
    var timeLabel: String? = ""
    val min = second / 60
    val sec = second % 60
    timeLabel += "$min:"
    if (sec < 10) timeLabel += "0"
    timeLabel += sec
    return timeLabel
}

internal fun randomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

internal fun delay(millis: Long, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable {
        callback.invoke()
    }, millis)
}


internal fun getProgressPercent(totalCount: Int, downloadedCount: Int): Double {
    try {
        return downloadedCount.toDouble() / totalCount.toDouble() * (100.00).toDouble()
    } catch (e: Exception) {
        Log.e("getProgressPercentErr", e.localizedMessage)
    }
    return 0.1
}


@ColorInt
internal fun Context.getColorFromAttr(resid: Int): Int {
    val typedValue = TypedValue()
    val theme = this.theme
    theme.resolveAttribute(resid, typedValue, true)
    return typedValue.data
}

internal fun TextView.setTextColorFromAttr(resid: Int) {
    val color = this.context.getColorFromAttr(resid)
    this.setTextColor(color)
}

internal fun Button.setTextColorFromAttr(resid: Int) {
    val color = this.context.getColorFromAttr(resid)
    this.setTextColor(color)
}

internal fun TextView.textColor(resid: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextColor(this.context.getColor(resid))
    } else {
        this.setTextColor(this.context.resources.getColor(resid))
    }
}

internal fun Button.textColor(resid: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextColor(this.context.getColor(resid))
    } else {
        this.setTextColor(this.context.resources.getColor(resid))
    }
}


internal fun isValidBangladeshiPhoneNumber(phoneNumber: String?): Boolean {
    return phoneNumber?.let { Regex("\\+?(88)?01[13456789]\\d{2}\\-?\\d{6}").matches(it) } ?: false
}

/**https://en.wikipedia.org/wiki/Telephone_numbers_in_Bangladesh*/
internal fun isBangladeshiPhoneNumber(phoneNumber: String?): Boolean {

    return phoneNumber?.let { Regex("\\+?(88)01[13456789]\\d{2}\\-?\\d{6}").matches(it) } ?: false
}

internal fun isBanglalink(phoneNumber: String) = Regex("\\+?(88)?01[49]\\d{8}").matches(phoneNumber)
internal fun isRobiOrArtel(phoneNumber: String) =
    Regex("\\+?(88)?01[68]\\d{8}").matches(phoneNumber)

internal fun isGP(phoneNumber: String) = Regex("\\+?(88)?01[37]\\d{8}").matches(phoneNumber)

internal fun String.removePlus() = this.replace("+", "")

internal fun String.convert11DigitBdNumber(): String? {
    val regex = Regex("01[13456789]\\d{2}\\-?\\d{6}")
    return regex.find(this)?.groupValues?.first()
}

internal fun String.parseFloatNumber(): Float? {
    return exH {
        Regex("[+-]?([0-9]*[.])?[0-9]+")
            .find(this)
            ?.groupValues
            ?.first()
            ?.toFloat()
    }
}

internal fun rangeMap(
    x: Float,
    in_min: Float,
    in_max: Float,
    out_min: Float,
    out_max: Float
): Float {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
}

internal fun calculateVideoHeight(displayWidth: Int, videoWidth: Int, videoHeight: Int): Int {
    return rangeMap(
        videoHeight.toFloat(),
        0F,
        videoWidth.toFloat(),
        0F,
        displayWidth.toFloat()
    ).toInt()
}

internal fun calculateVideoWidth(displayHeight: Int, videoHeight: Int, videoWidth: Int): Int {
    return rangeMap(
        videoWidth.toFloat(),
        0F,
        videoHeight.toFloat(),
        0F,
        displayHeight.toFloat()
    ).toInt()
}


internal inline fun <T> exH(func: () -> T): T? {
    return try {
        func.invoke()
    } catch (e: Exception) {
        null
    }
}

internal fun <T> CoroutineScope.asyncCallback(func: suspend () -> T, callbackFunc: (T) -> Unit) {
    this.launch {
        val rObj: T = func.invoke()
        withContext(Dispatchers.Main) {
            callbackFunc.invoke(rObj)
        }
    }
}

internal fun <T> CoroutineScope.processWithCoroutine(
    inputFunc: () -> T,
    callbackFunc: (T) -> Unit
) {
    this.launch {
        val rObj: T = inputFunc.invoke()
        withContext(Dispatchers.Main) {
            callbackFunc.invoke(rObj)
        }
    }
}


internal fun TextView.htmlText(res: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text =
            Html.fromHtml(this.resources.getString(res), HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        this.text =
            Html.fromHtml(this.resources.getString(res))
    }
}

internal fun TextView.htmlText(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text =
            Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        this.text =
            Html.fromHtml(text)
    }
}

internal fun Date.toTimeFormat(): String {
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(this)
}

internal fun String.toDate(): Date? {
    return exH { SimpleDateFormat("dd-M-yyyy HH:mm:ss", Locale.getDefault()).parse(this) }
}

internal fun Date.normalize(): String {
    return SimpleDateFormat("dd-M-yyyy HH:mm:ss", Locale.getDefault()).format(this)
}

internal operator fun Date.minus(month: Month): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, -month.value)
    return calendar.time
}

internal operator fun Date.minus(days: Day): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, -days.value)
    return calendar.time
}

internal operator fun Date.minus(year: Year): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.YEAR, -year.value)
    return calendar.time
}

internal operator fun Date.plus(month: Month): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MONTH, month.value)
    return calendar.time
}

internal operator fun Date.plus(days: Day): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, days.value)
    return calendar.time
}

internal operator fun Date.plus(year: Year): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.YEAR, year.value)
    return calendar.time
}

internal operator fun Date.plus(minute: Minute): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MINUTE, minute.value)
    return calendar.time
}

internal operator fun Date.minus(minute: Minute): Date {

    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MINUTE, -minute.value)
    return calendar.time
}

internal operator fun Date.plus(hour: Hour): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR, hour.value)
    return calendar.time
}

internal operator fun Date.minus(hour: Hour): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR, -hour.value)
    return calendar.time
}

internal operator fun Date.plus(minute: Millis): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MILLISECOND, minute.value)
    return calendar.time
}

internal operator fun Date.minus(minute: Millis): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.MILLISECOND, -minute.value)
    return calendar.time
}


internal class Month(val value: Int)
internal class Day(val value: Int)
internal class Year(val value: Int)
internal class Minute(val value: Int)
internal class Hour(val value: Int)
internal class Millis(val value: Int)

internal operator fun Date.minus(date: Date): TimeDistance {
    val timeOne = this.time
    val timeTwo = date.time
    val duration = timeOne - timeTwo
    return TimeDistance(duration)

}

/*operator fun Date.compareTo(date: Date):Int{
    return when{
        this.time > date.time -> 1
        this.time < date.time -> -1
        else -> 0
    }
}*/
@Deprecated("")
internal operator fun Date.plus(date: Date): TimeDistance {
    val timeOne = this.time
    val timeTwo = date.time
    return TimeDistance(timeOne + timeTwo)

}


internal fun Date.calender(): Calendar =
    Calendar.getInstance(Locale.getDefault()).apply { time = this@calender }


internal fun Date.month() = calender()[Calendar.MONTH]
internal fun Date.day() = calender()[Calendar.DAY_OF_MONTH]
internal fun Date.year() = calender()[Calendar.YEAR]
internal fun Date.hour() = calender()[Calendar.HOUR_OF_DAY]
internal fun Long.toDate() = Date(this)

/*public operator fun Date.compareTo(date: Date):Int{
    return this.compareTo(date)
}*/

internal operator fun TimeDistance.plus(time: TimeDistance): TimeDistance =
    TimeDistance(this.totalMilliseconds + time.totalMilliseconds)

internal operator fun TimeDistance.minus(time: TimeDistance): TimeDistance =
    TimeDistance(this.totalMilliseconds - time.totalMilliseconds)

@Keep
internal data class TimeDistance(val totalMilliseconds: Long) {
    val totalDays: Long = TimeUnit.DAYS.convert(totalMilliseconds, TimeUnit.MILLISECONDS)
    val totalHours: Long = TimeUnit.HOURS.convert(totalMilliseconds, TimeUnit.MILLISECONDS)
    val totalMinutes: Long = TimeUnit.MINUTES.convert(totalMilliseconds, TimeUnit.MILLISECONDS)
    val totalSeconds: Long = TimeUnit.SECONDS.convert(totalMilliseconds, TimeUnit.MILLISECONDS)

    val seconds: Long = (totalMilliseconds / 1000) % 60
    val minutes: Long = ((totalMilliseconds / (1000 * 60)) % 60)
    val hours: Long = ((totalMilliseconds / (1000 * 60 * 60)) % 24)


    val durationStr: String = "${hours.f00()}:${minutes.f00()}:${seconds.f00()}"
    override fun toString(): String {
        return durationStr
    }

}

internal fun Long.f00(): String {
    return String.format("%02d", this)
}

internal fun Long.ze(): String = if (this != 0L) this.toString() else ""
internal fun IntRange.sList() = this.map { it.toString() }.toList()
internal fun IntRange.sArray() = this.map { it.toString() }.toTypedArray()
internal fun <T> List<T>?.nullFixList() = this?.filterNotNull() ?: emptyList()

internal interface SerializableFunction0<out R> : Function0<R>, Serializable
internal interface SerializableFunction1<in P1, out R> : Function1<P1, R>, Serializable

internal fun validAdsIndex(start: Int, step: Int, itemsSize: Int): List<Int> {

    if (start >= itemsSize) {
        return listOf(start)
    }
    val range = start..itemsSize step step
    return range.filter { i -> i < itemsSize }
}

internal fun isVideoAdsExpiry(): Boolean {
    val currentDate = Date()
    val expiryDate = SimpleDateFormat("dd-M-yyyy", Locale.getDefault()).parse("13-1-2022")
    return currentDate > expiryDate
}

internal fun String.bfr(): String {
    val sBuilder = StringBuffer()
    this.forEach {
        when (it) {
            '-' -> sBuilder.append('+')
            '+' -> sBuilder.append('-')
            '<' -> sBuilder.append('>')
            '>' -> sBuilder.append('<')
            '.' -> sBuilder.append('.')
            '[' -> sBuilder.append(']')
            ']' -> sBuilder.append('[')
        }
    }
    return sBuilder.toString()
}

internal fun String.toFuskaUrl(): String {
    val sBuilder = StringBuffer()
    this.forEach {
        when (it) {
            '-' -> sBuilder.append('a')
            '+' -> sBuilder.append('b')
            '<' -> sBuilder.append('c')
            '>' -> sBuilder.append('d')
            '.' -> sBuilder.append('e')
            '[' -> sBuilder.append('f')
            ']' -> sBuilder.append('g')
        }
    }
    return "omy${sBuilder}tno"
}

internal fun String.toFuska(): String {
    val sBuilder = StringBuffer()
    this.forEach {
        when (it) {
            'a' -> sBuilder.append('-')
            'b' -> sBuilder.append('+')
            'c' -> sBuilder.append('<')
            'd' -> sBuilder.append('>')
            'e' -> sBuilder.append('.')
            'f' -> sBuilder.append('[')
            'g' -> sBuilder.append(']')
        }
    }
    return sBuilder.toString()
}


internal fun String.matchList(regex: String): List<String> {
    val list: MutableList<String> = ArrayList()
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        list.add(matcher.group())
    }
    return list
}

internal fun String.find(regex: String): List<String> {
    val listString: MutableList<String> = ArrayList()
    val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    while (matcher.find()) {
        listString.add(matcher.group())
    }
    return listString
}

internal fun String.isValidToken(): Boolean {
    // Log.i("onChildItemClick", "isValidToken:${this.length > "Bearer ".length}  $this ")
    return this.length > "Bearer ".length
}

internal fun String.replace(regex: String, replaceFunc: (string: String) -> String): String {
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    val stringBuffer = StringBuffer()
    while (matcher.find()) {
        val newString = replaceFunc(matcher.group())
        matcher.appendReplacement(stringBuffer, newString)
    }
    matcher.appendTail(stringBuffer)
    return stringBuffer.toString()
}


internal fun Long.millisToTimeLabel(): String? {
    var timeLabel: String? = ""
    val min = this / 1000 / 60
    val sec = this / 1000 % 60
    timeLabel += "$min:"
    if (sec < 10) timeLabel += "0"
    timeLabel += sec
    return timeLabel
}

internal fun ByteArray.toHex(): String =
    joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

internal fun Context.androidUniqueId(): String {
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

internal fun newDownloadKeyMap(map: HashMap<String, Double>): HashMap<String, Double> {
    return HashMap(map.filter { entry -> entry.value == 100.0 })
}

internal fun isEmailValid(email: String): Boolean {
    return Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}


internal inline operator fun <reified T> T.get(name: String): Any? {
    return T::class.java.getDeclaredField(name).apply {
        isAccessible = true
    }.get(this)
}

internal inline fun <reified T> T.values(): Map<String, Any?> {
    val hashMap: MutableMap<String, Any?> = HashMap()
    T::class.java.declaredFields.forEach { field ->
        field.isAccessible = true
        hashMap[field.name] = field.get(this)
    }
    return hashMap
}


internal inline fun <reified T> T.methods(): List<String> {

    return T::class.java.declaredMethods.map { method ->
        "${Modifier.toString(method.modifiers)} ${method.name}(${
            method.parameterTypes.joinToString(
                ","
            ) { it.name }
        }) : ${method.returnType.name}  ${method.declaringClass.name}"
    }

}


@RequiresApi(Build.VERSION_CODES.O)
internal inline fun <reified T> T.debugInfo(): String {
    val jClass = T::class.java
    val stringBuilder = StringBuilder()
    stringBuilder.appendLine("Fields")
    jClass.declaredFields.forEach {
        it.isAccessible = true
        stringBuilder.appendLine("\t${it.type.name} ${it.name} = ${it.get(this)}")
    }
    /* stringBuilder.appendLine("Method")
     jClass.declaredMethods.forEach {
         stringBuilder.appendLine("\t${it.returnType.name} ${it.name}( ${ it.parameters.map { it.name }.toString() })")
     }*/

    return stringBuilder.toString()
}


internal fun Context.readStringFromRowFile(@RawRes id: Int): String {
    val inputStream = this.resources.openRawResource(id)
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    return bufferedReader.readText()
}


internal enum class SubStatus(val iosName: String) {
    NotSignedIn("FreeNotSignedIn"), // only for firebase event
    NeverPro("FreeNeverPro"),
    OncePro("FreeOncePro"),
    Pro("Pro")
}

internal fun ViewGroup.allChildren(filter: ((child: View) -> Boolean)? = null): MutableList<View> {
    val list: MutableList<View> = ArrayList()
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i)
        if (filter != null) {
            if (filter(child)) {
                list.add(child)
            }
        } else {
            list.add(child)
        }
    }
    return list
}

internal fun String.trimWithMaxLength(size: Int): String {
    val tString = this.trim()
    if (tString.length > (size - 2)) {
        return "${tString.subSequence(0, size - 2)}.."
    }
    return tString
}

internal fun String?.nullFix(): String {
    if (this != null && this != "null") {
        return this
    }
    return ""
}

internal fun Int?.nullFix(): String {
    if (this == null) {
        return "0"
    }
    return this.toString()
}

internal fun CharSequence?.nullFix(): CharSequence {
    if (this != null && this != "null") {
        return this
    }
    return ""
}


/*class Doggy(val name:String){
    override fun toString(): String {
        return name
    }
}
fun < T> test(classType:Class<T>, string: String): T {
    return classType.getConstructor(String::class.java).newInstance(string) as T
}

fun main(){
   val doggy = test(Doggy::class.java,"Doo")
    println(doggy.toString())
}*/

/*val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()*/

/*fun rangeMap(x: Float, in_min: Float, in_max: Float, out_min: Float, out_max: Float): Float {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min
}
fun calculateVideoHeight(displayWidth:Int,videoWidth:Int,videoHeight:Int): Int {
    return rangeMap(videoHeight.toFloat(),0F,videoWidth.toFloat(),0F,displayWidth.toFloat()).toInt()
}
fun calculateVideoWidth(displayHeight:Int, videoHeight:Int, videoWidth:Int): Int {
    return rangeMap(videoWidth.toFloat(),0F,videoHeight.toFloat(),0F,displayHeight.toFloat()).toInt()
}*/

fun String.toBase64(): String {

    return Base64.encodeToString(this.toByteArray(), android.util.Base64.DEFAULT)
}


fun String.fromBase64(): String {
    return String(android.util.Base64.decode(this, android.util.Base64.DEFAULT),  StandardCharsets.UTF_8)
}
fun Float.format(pointCount:Int): String {
    return String.format("%.${pointCount}f",this)
}

inline fun <reified T>Fragment.getNavigationResult(key: String = "result") =
    this.findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    this.findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
fun toVideoMediaItem(mediaId: String,type:String,playUrl:String,title:String): MediaItem.Builder {
    val music = Music(contentType = type, mediaId = mediaId, title = title)

    return MediaItem.Builder()
        .setUri(playUrl)
        .setMediaId(mediaId)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(music.title)
                .setExtras(music.toBundleMetaData("Video"))
                .build()
        )

}