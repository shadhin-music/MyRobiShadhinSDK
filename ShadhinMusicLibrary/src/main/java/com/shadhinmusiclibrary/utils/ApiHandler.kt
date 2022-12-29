package com.shadhinmusiclibrary.utils


import android.util.Log
import androidx.annotation.Keep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

@Keep
internal enum class Status {
    SUCCESS,
    ERROR, LOADING
}

@Keep
internal data class ApiResponse<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val errorCode: Int? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String, errorCode: Int? = null): ApiResponse<T> =
            ApiResponse(
                status = Status.ERROR,
                data = data,
                message = message,
                errorCode = errorCode
            )
    }

    fun ifSuccess(callback: (T) -> Unit) {
        if (status == Status.SUCCESS && data != null) {
            callback.invoke(data)
        }
    }

    fun ifError(callback: (message: String?) -> Unit) {
        if (status != Status.SUCCESS) {
            callback.invoke(message)
        }
    }
}

internal suspend fun <T> safeApiCall(responseFunction: suspend () -> T): ApiResponse<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = responseFunction.invoke()
            ApiResponse.success(response)
        } catch (e: HttpException) {
            /* val errorResponse = kotlin.runCatching {  e.response()?.errorBody()?.string()}.getOrNull()
             val error = errorResponse?.let { JSONObject(it) }

             val message = if(error?.has("Message") == true) {
                 error.get("Message").toString()
             }else{
                 e.message()
             }*/
            Log.e("AH", "safeApiCall: $e")
            ApiResponse.error(null, e.toString(), e.code())
        } catch (e: UnknownHostException) {
            ApiResponse.error(null, "Please check your internet connection")
        } catch (e: Exception) {
            ApiResponse.error(null, e.message.toString())
        }
    }
}

//inline fun <ResultType, RequestType> networkBoundResource(
//    //*Internet call*//
//    crossinline fetch: suspend () -> RequestType,
//    crossinline saveFetchResult: suspend (RequestType) -> Unit,
//    crossinline shouldFetch: (ResultType) -> Boolean = { true }
//) = flow {
//    val data = query().first()
//    val flow = if (shouldFetch(data)) {
//        emit(Resource.Loading(data))
//        try {
//            saveFetchResult(fetch())
//            query().map { Resource.Success(it) }
//        } catch (throwable: Throwable) {
////            throwable.fillInStackTrace().
//            query().map { Resource.Error(throwable, it) }
//        }
//    } else {
//        query().map { Resource.Success(it) }
//    }
//    emitAll(flow)
//}

internal sealed class Resource<T>(
    val status: Status,
    val data: T,
    val error: Throwable? = null
) {
    internal class Success<T>(data: T) : Resource<T>(Status.SUCCESS, data, null)
    internal class Loading<T>(data: T) : Resource<T>(Status.SUCCESS, data, null)
    internal class Error<T>(throwable: Throwable, data: T) :
        Resource<T>(Status.ERROR, data, throwable)
}