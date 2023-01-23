package com.myrobi.shadhinmusiclibrary.data.repository

import android.util.Log
import com.myrobi.shadhinmusiclibrary.data.model.LoginModel
import com.myrobi.shadhinmusiclibrary.data.remote.ApiLoginService
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


internal class AuthRepository(private val apiLoginSer: ApiLoginService) {
//    suspend fun login(): Pair<Boolean, String?> {
//
//            val response = safeApiCall { apiLoginSer.fetchMyRobiLogin(LoginModel()
//                .apply {
//                    msisdn = "8801861901236"
//                })}
//        return if (response.status == Status.SUCCESS) {
//            appToken = response.data.toString()
//            Log.e("AuthRepository: ALS", "login: $appToken")
//            Pair(true, response.message)
//        } else {
//            Log.e("RAA", "onResponse: " + response.message)
//            appToken = null
//            Pair(false, response.message)
//        }
//    }


    suspend fun login(token: String): Pair<Boolean, String?> {
        val response = safeApiCall { apiLoginSer.login("Bearer $token") }
        return if (response.status == Status.SUCCESS) {
            appToken = response.data?.data?.Token
            Log.e("AuthRepository: ALS", "login: $appToken")
            Pair(true, response.message)
        } else {
            Log.e("RAA", "onResponse: " + response.message)
            appToken = null
            Pair(false, response.message)
        }
    }

    companion object {
        var appToken: String? = "2+q1zStXFKcX6+jWf68dKS0mtu3DKVOTAPRmXovfezXqOqtCwHDwa36VybcTjxf0EDs+gm0edtW98wBUjTW5hPV0Msh+y+6CzMxYON1ib+srZjBfNxLpw1CdQZwLHZR9eq1lRzrrRDTavtIthP636hKFy6S/tRiFZUq8yxPCoMpIiNoTUMtxUKVDtY0BYlBVSLyGPCF50bGPh/7+isbWPZ5hKcVqQR1QplnvfSJDq1rzTNiBCjnCDY7gRvLTyv0FmfbWSFj3hU3woLXDbKPUo9f3tu7YxNF7usfn5QwDDDbucow4UCrsUbRUuogY7hyRGZjCnyWyN0dc2PLt4gmDJgxJ/oTSGue+l9/uiMKZxgN1KybHKxLExNkLvl9lf1G6sOHrmKw9SAZ3ZL9lwEt5zUhquQRwllcOT8Z5pmQAZzhWWtHW5YskUDgBBUPxOjudfxcv8ioLF9SgJvTuAM9OgythuuDwsJCKFYFGj/R3SLMmCvgXXeamyvGcxggRzCDZyQ/AauCeCNDwRxJQcq1DHffJL0jBNRggFZ89UqghWJ0svIwFPetSgNYJjTvzZCbbGynGwuQzlCu6Vq5HxT+zlmO2ezHRSVZZ1dVSIgAY3QNgXDOHhW8BSu9gTQYRrdMSQGnuzWB9ERLXyk+2AjX29Vf+L3cAGWQKKJSqoI/Hkeo6wU/ZycU7X98FeQIPZnmWKUNcsqEcg9cYL71xH27Owq3+x6J0FgI6MqcLOVNU+7rSD0Jxyxi1kMwEx4xYm81hC/L9l3drYSJHMn4JJZpvcjvRA6v5Z4LR6rMKHz8Rwc9lG70jEKy803LY5mCYF4bOKH7x6lYFZG3xf7ujIAlo2TjuQn3N3zjxGrxYKTcYNdv7lm7NTTkDqJERwMdVtkSJUUy8gn7KriKXNRNfP97+IA==:43hZ/7PdSWf2E/qOmX4YOA=="
    }
}