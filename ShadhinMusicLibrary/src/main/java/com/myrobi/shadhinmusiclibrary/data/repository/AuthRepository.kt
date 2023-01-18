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
        var appToken: String? = "I6eDy/CvlY/PoidYW7s8PKUMUjvxZBhSvJZB3YFnQutIwKxOxV1oBQCp7jVBYjR22gwn5LnzsHCUtWsQG1H/FyY580auki/I0r9DplEtuw3VHrjVgW0CVUIU4XedkBwCGksrLCXANUm8/oxgV9L2UYrmCoEzNngHMAZ0mVsob2NCHhhZag0Te+66UOu4e+JOXDPjqA7rPTVvRpvy04fR3Q4YBE9753FxCaIau3+sg2DKKg195ydXwnLVpXANQFJQy/lJkxinyWoF6otTzMC7TvurAXsi9mWcg7QzrCq4OlRWdG1SZcqjiql4NNY+NfNKu71iuKRrTXiOJBTsQK0OIJkCWgAUOceIXBWoUl7X6eFrv99iaa6W9uoTzt0meFrJcKm1+hflfs16aU7IXgSssYA3deA/V4swx8rG6oxhlOxcidkNMJ6SZxJJ078+u0gJLeiS4qr2RhmiqBxfmYrUV9bz5f+P5hS4rIu0DI3Yurqc6DIRdvVJHtx9guz8zR9phTni5W4+W9f72QBhhYUwD8VVOwG87BGT8F6kwI+OaiuKj97SuCsdYp3LmSh+0S+hYhhQPZ2SuMJ12ToiVArWs476KucK2GiZHndDLPdwslsiEX7YaRsouvt4jSEFJzQRnGCgLI0dyERlLoiG09EEnnotjzMw3rvfdlHYN2YSNwXjxKCPN98GGgiGFvKq+gHbxu9Mx9PUZSbhHRkrpGeMVYg6KRLSq0ovnlQjW+cZXd8ZmAHZokjb/sp/FAecFKQP5cwzSo/fgegZkJpd4tHqd2aN8ISJoACOWb/BDInjAlUM3PEMDkDrjQRmOJLudpwl8LAKtd26TiOUYwdt+Labd6uHE2aKm/YIL2ySyTC5Wtt5QCv9jBoisDLWJBB8/ySJakg8lNd6y+m3pgtxKzBvHA==:0BxeoDxBZz1DY+8IF0NROw=="
    }
}