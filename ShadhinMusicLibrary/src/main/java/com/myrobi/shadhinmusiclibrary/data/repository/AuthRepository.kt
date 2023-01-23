package com.myrobi.shadhinmusiclibrary.data.repository

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.myrobi.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.myrobi.shadhinmusiclibrary.data.model.LoginModel
import com.myrobi.shadhinmusiclibrary.data.model.LoginResponse
import com.myrobi.shadhinmusiclibrary.data.remote.ApiLoginService
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.get
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


internal class AuthRepository(private val apiLoginSer: ApiLoginService) {

    suspend fun login(msisdn: String): Pair<Boolean, String?> {
        val response = safeApiCall { apiLoginSer.fetchMyRobiLogin(LoginModel(msisdn)) }
        return if (response.status == Status.SUCCESS) {
                   appToken = response.data?.data?.Token
            Pair(true, response.message)
        } else {
            appToken = null
            Pair(false, response.message)
        }
    }

//            apiLoginSer.fetchMyRobiLogin(LoginModel()
//                .apply {
//                    msisdn
//
//                }).enqueue(object : Callback<LoginResponse> {
//                override fun onResponse(
//                    call: Call<LoginResponse>,
//                    response: Response<LoginResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        loginResponse = response.body()
//                        appToken = loginResponse?.data?.accessToken
//                        Log.e("AuthRepository: ALS", "login: $data")
//                        Pair(true, response.message())
//                    } else {
//                        appToken = null
//                        Pair(false, response.message())
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//       }

    companion object {
        var appToken: String? = null
      //  = "2+q1zStXFKcX6+jWf68dKS0mtu3DKVOTAPRmXovfezXqOqtCwHDwa36VybcTjxf0EDs+gm0edtW98wBUjTW5hPV0Msh+y+6CzMxYON1ib+srZjBfNxLpw1CdQZwLHZR9eq1lRzrrRDTavtIthP636hKFy6S/tRiFZUq8yxPCoMpIiNoTUMtxUKVDtY0BYlBVSLyGPCF50bGPh/7+isbWPZ5hKcVqQR1QplnvfSJDq1rzTNiBCjnCDY7gRvLTyv0FmfbWSFj3hU3woLXDbKPUo9f3tu7YxNF7usfn5QwDDDbucow4UCrsUbRUuogY7hyRGZjCnyWyN0dc2PLt4gmDJgxJ/oTSGue+l9/uiMKZxgN1KybHKxLExNkLvl9lf1G6sOHrmKw9SAZ3ZL9lwEt5zUhquQRwllcOT8Z5pmQAZzhWWtHW5YskUDgBBUPxOjudfxcv8ioLF9SgJvTuAM9OgythuuDwsJCKFYFGj/R3SLMmCvgXXeamyvGcxggRzCDZyQ/AauCeCNDwRxJQcq1DHffJL0jBNRggFZ89UqghWJ0svIwFPetSgNYJjTvzZCbbGynGwuQzlCu6Vq5HxT+zlmO2ezHRSVZZ1dVSIgAY3QNgXDOHhW8BSu9gTQYRrdMSQGnuzWB9ERLXyk+2AjX29Vf+L3cAGWQKKJSqoI/Hkeo6wU/ZycU7X98FeQIPZnmWKUNcsqEcg9cYL71xH27Owq3+x6J0FgI6MqcLOVNU+7rSD0Jxyxi1kMwEx4xYm81hC/L9l3drYSJHMn4JJZpvcjvRA6v5Z4LR6rMKHz8Rwc9lG70jEKy803LY5mCYF4bOKH7x6lYFZG3xf7ujIAlo2TjuQn3N3zjxGrxYKTcYNdv7lm7NTTkDqJERwMdVtkSJUUy8gn7KriKXNRNfP97+IA==:43hZ/7PdSWf2E/qOmX4YOA=="
    }
}