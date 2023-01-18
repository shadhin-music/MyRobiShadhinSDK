package com.myrobi.shadhinmusiclibrary.data.repository

import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.remote.ApiLoginService
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class AuthRepository(private val apiLoginSer: ApiLoginService) {
    suspend fun login(token: String): Pair<Boolean, String?> {
        val response = safeApiCall { apiLoginSer.login("Bearer $token") }

        return if (response.status == Status.SUCCESS) {
            appToken = response.data?.data?.token
            Log.e("AuthRepository: ALS", "login: $appToken")
            Pair(true, response.message)
        } else {
            Log.e("RAA", "onResponse: " + response.message)
            appToken = null
            Pair(false, response.message)
        }
    }

    companion object {
        //TODO this time static it will dynamic
        var appToken: String? = "2NgSg3qF8fuaA3u/vH/6VXNQqxhzpW7M7fIbirIXhJ5E6i11OOt95EZkVyuWtFdl6utHTvxLwWg+L/dS3k9wmQPLMOx1zojScpssR7/GJLTInCY9cTepHi79BuLcRbO0TOQsCCzsp8DydwXqBr2BxIIbCfdCaHJnvaWGhOIYevUbGX1JFyx/X41nGFneyqRymKsnvzXkKqOpzNOIZKhpkoONpeUBdcnGk7j7zS4flP4kzDtNlIkr85RIdkRbbIIc0VBJVNu0LKnXpOzRaoSqkkoexsfvMIEaGtESfkJ1PTqbfDd+yMPUYOfJulJS9j53sxZdlQihvTC3mwavF+GsntrEINeBAbkfk5eYs8A3qn9EnFqlY1QdXMTr/Ip5XIEf5vm1LSV9RqCKe+5ia0ttfox44Es+ruYIQFkWcJkaC0sNLBPoY6njMFJ53Lpt//fPVVvJ3Kl/9T95ktqEMbKIWMEEbspppvR0bgcxaCqing7nYcJWBsHbtY8X+JmCaO1m/7y/jIP3EkRxUb1K4GQYg4xT95CHEvmory4iJ42ViACuuieFeSfSeNQgYfdYlewjrJjkJu5q19B83Q08gzXf+ceoUsxnaUd48QPqK9/ARmxzSKtGSzatPXVrIxa+x7isdoj3RrKbdlNDxAF/mtfBl/szDtEZ1trBkt4xI8jglEMQxWg0drR4JhjwzhFv3xbSFMs+QbeQuGvrR2rVVYxNINjV4jgWXRC9XDBjXwgllRZyXExhNvRxRhmhwB41M35mgM+mWmIGYH5qPnIPUnlqgglwH98PwOZcIJZc3g5CD17U2M+nHgpd2EM7LQj6b7017vE23o6EKSu1lDZ82jZkkHmAbIkXF9wrWd6sPcNSUxv0Fcd9wyaGVpKGtPLsFMwn1STKhepisIrJwDETlA+LPp03acb8snGFItKJ2g4ocyqdl6YgYCrd98YMwU5GJxWdr3zi8HMTNvzsYUAEpdw/NW22EcHyzySb+RWvHvleDoyxExu2z2qnFjrpXKbdZ4VabKAq+I5F1aMEDyxxjZDDeJc75H40f1I+5rR27AyndGlfO8iD3Rynl4/YPtEoKiyFj5FMM5wlmN5Qaz5d9F/O8CW1wu1c7g70T84TwMKo1voa5NkeXBAr58kDNCmzAPJRVqLEZXvAWGOOQY7e/yeTZcEFfwY/aBj7xwO0M8xWSJQk2u48SEKvYl8P7z3JoH8uSlOCGM2iGU1jfZTn9HUlrRGjMsmQGRFFVcTjJDP/O/TVstm3PEXVEoOwpumvL5NcSvT7n1Eo3VsPiIHAEKM3/B7gZCeJCQSPtqVTyRwijzpDlqh4vi/geT7JrT4fsKqu:TAWSg1YfXs/EO2nXuVe4rA=="
    }
}