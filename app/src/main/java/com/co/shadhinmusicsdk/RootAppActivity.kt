package com.co.shadhinmusicsdk

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast



import com.co.shadhinmusicsdk.data.AppApiService
import com.co.shadhinmusicsdk.data.model.ClientService
import com.co.shadhinmusicsdk.data.model.LoginData
import com.co.shadhinmusicsdk.data.model.LoginModel
import com.co.shadhinmusicsdk.data.model.LoginResponse


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class RootAppActivity : AppCompatActivity() {
    @SuppressLint("ShowToast")

    private var loginResponse: LoginResponse? = null
    val callApi =
        ClientService.getRetrofitBaseService().create(AppApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_app)

        val etMobileNumber: EditText = findViewById(R.id.et_mobile_number)
        val btnHomeFrag: Button = findViewById(R.id.btn_home_fragment)

        btnHomeFrag.setOnClickListener {
            val mobileNm = etMobileNumber.text.toString()

            if (mobileNm.isNotEmpty()) {
                callApi.fetchMyRobiLogin(LoginModel()
                    .apply {
                        msisdn = mobileNm
//                        userFullName = "test"
//                        deviceToken = ""
//                        gender = "test"
//                        imageURL = ""
                    }).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            loginResponse = response.body()
                            goToSdk()
                            Log.e("RAppA", "onResponse: " + response.code())
                        } else {
                            Log.e(
                                "RAppA",
                                "onResponse: " + response.body()?.message + " \n" + response.message()
                            )
                            Toast.makeText(
                                this@RootAppActivity,
                                response.body()?.message.toString() + "",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@RootAppActivity, t.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToSdk() {
        if (loginResponse?.data != null) {
            if (loginResponse?.message == "SUCCESS") {
                val loginData: LoginData? = loginResponse?.data
                startActivity(Intent(this, AppActivity::class.java)
                    .apply {
                        putExtra("loginData", Bundle().apply {
                            putSerializable("loginData", loginData as Serializable)
                        })
                    })
            }
        }
    }
}