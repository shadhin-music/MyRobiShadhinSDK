package com.myrobi.shadhinmusiclibrary.data.repository

import com.myrobi.shadhinmusiclibrary.data.model.AddtoFavBody
import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.utils.safeApiCall


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