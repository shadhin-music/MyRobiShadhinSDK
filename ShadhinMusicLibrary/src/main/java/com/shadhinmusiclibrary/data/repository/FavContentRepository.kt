package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.model.AddtoFavBody
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.safeApiCall

internal class FavContentRepository (private val apiService: ApiService) {

    suspend fun fetchAllFavoriteByType(type:String) = safeApiCall {
        apiService.fetchAllFavoriteByType(type)
    }
    suspend fun addFavByType(contentId:String,contentType:String) = safeApiCall {
        apiService.addToFavorite(AddtoFavBody(contentId,contentType))
    }
    suspend fun deleteFavByType(contentId:String,contentType:String) = safeApiCall {
        apiService.deleteFavorite(AddtoFavBody(contentId,contentType))
    }
}