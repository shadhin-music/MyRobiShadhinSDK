package com.shadhinmusiclibrary.data.repository

import com.shadhinmusiclibrary.data.model.APIResponse
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.utils.ApiResponse
import com.shadhinmusiclibrary.utils.safeApiCall
import retrofit2.http.Header

internal class AlbumContentRepository(private val apiService: ApiService) {
    suspend fun fetchAlbumContent(contentId: String) = safeApiCall {
        apiService.fetchAlbumContent(contentId)
    }
    suspend fun fetchSingleContent(contentId: String): ApiResponse<APIResponse<MutableList<SongDetailModel>>> {

        val res = safeApiCall { apiService.fetchSingleContent(contentId) }
        val mapData = APIResponse(
            data = mutableListOf(res.data?.data?:SongDetailModel()),
            fav = res.data?.fav?:"",
            follow =res.data?.follow?:"",
            image = res.data?.image?:"",
            message = res.data?.message?:"",
            status = res.data?.status?:"",
            total = res.data?.total?:0,
            type = res.data?.type?:"",
            name = res.data?.data?.titleName?:"",
            albumName = res.data?.data?.album_Name?:"",
            artistName = res.data?.data?.artistName?:"",
            artistId = res.data?.data?.artist_Id?:"",
            albumImage = res.data?.data?.imageUrl?:""
        )
        return ApiResponse(res.status,mapData,res.message,res.errorCode)
    }

    suspend fun fetchPlaylistContent(contentId: String) = safeApiCall {
        apiService.fetchGetPlaylistContentById(contentId)
    }

    suspend fun fetchGetAllRadio() = safeApiCall {
        apiService.fetchGetAllRadio()
    }
}