package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.model.profile.UserProfileResponseModel
import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


internal class UserProfileRepository(private val apiService: ApiService) {
//    suspend fun updateUserProfile(name:String,birthday:String,gender:String,profileImageFile:File)= safeApiCall {
//            apiService.updateUserProfile(ProfileupdateModel(name,birthday,gender,profileImageFile))
//        }
    suspend fun getUserInfo()= safeApiCall {
        apiService.getUserInfo()
    }
    suspend fun updateUserProfile(fullName:String?,birthDate:String?,gender:String?,profileImage: File?): ApiResponse<UserProfileResponseModel>? {
     //   suspend fun updateUserProfile(fullName:String?,birthDate:String?,gender:String?): ApiResponse<UserProfileResponseModel>? {
        val requestBody: RequestBody? = profileImage?.asRequestBody("image/png".toMediaTypeOrNull())
        return safeApiCall {
            apiService.updateUserProfile(
//                fullName = fullName.toString(),
//                birthDate = birthDate?.toString(),
//                gender = gender?.toString(),
                fullName = fullName?.toRequestBody(),
                birthDate = birthDate?.toRequestBody(),
                gender = gender?.toRequestBody(),
                profileImage = requestBody?.let {
                    MultipartBody.Part.createFormData(
                        "ImageFile",
                        "profile.png",
                        it
                    )
                }
            )
        }
    }
}
