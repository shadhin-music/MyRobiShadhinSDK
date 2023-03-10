package com.myrobi.shadhinmusiclibrary.data.remote

import com.myrobi.shadhinmusiclibrary.data.model.*
import com.myrobi.shadhinmusiclibrary.data.model.APIResponse
import com.myrobi.shadhinmusiclibrary.data.model.ArtistBannerModel
import com.myrobi.shadhinmusiclibrary.data.model.HomeDataModel
import com.myrobi.shadhinmusiclibrary.data.model.PatchDataModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.comments.CommentReplyResponse
import com.myrobi.shadhinmusiclibrary.data.model.comments.CommentResponse
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataResponseModel
import com.myrobi.shadhinmusiclibrary.data.model.lastfm.LastFmResult
import com.myrobi.shadhinmusiclibrary.data.model.podcast.PodcastModel
import com.myrobi.shadhinmusiclibrary.data.model.profile.ProfileupdateModel
import com.myrobi.shadhinmusiclibrary.data.model.profile.UserProfileResponseModel
import com.myrobi.shadhinmusiclibrary.data.model.search.SearchModel
import com.myrobi.shadhinmusiclibrary.data.model.search.TopTrendingModel
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.CreatePlaylistResponseModel
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.UserPlayListModel
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.UserSongsPlaylistModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

internal interface ApiService {
    // @GET("ClientHomeContent/GetHomeContent")
      @GET("ClientHomeContent/GetHomeContentV2")
   // @GET("HomeContent/GetHomeContent")
    suspend fun fetchHomeData(
        @Query("pageNumber") pageNumber: Int?,
        @Query("isPaid") isPaid: Boolean?,
    ): HomeDataModel

    @GET("patch/getpatchdata")
    suspend fun fetchPatchData(
        @Query("patchCode") patchCode: String,
    ): PatchDataModel

    @GET("album/GetAlbumDetailsFC")
    suspend fun fetchAlbumContent(
        @Query("id") contentId: String,
    ): APIResponse<MutableList<SongDetailModel>>

    @GET("Track/singletrack")
    suspend fun fetchSingleContent(
        @Query("id") contentId: String,
    ): APIResponse<SongDetailModel>

    @GET("?method=artist.getinfo")
    suspend fun fetchArtistBiography(
        @Query("artist") artist: String?,
    ): LastFmResult

    @GET("Artist/ArtistPlayList")
    suspend fun fetchArtistBannerData(
        @Query("id") id: String?,
    ): ArtistBannerModel

    @GET("Artist/GetArtistContent")
    suspend fun fetchArtistSongs(
        @Query("id") id: String?,
    ): ArtistContentModel

    @GET("Artist/ArtistAlbumsbyidtype")
    suspend fun fetchArtistAlbum(
        @Query("type") type: String,
        @Query("id") id: String?,
    ): ArtistAlbumModel

    @GET("playlist/GetPlaylistContentByIdV2")
    suspend fun fetchGetPlaylistContentById(
        @Query("id") id: String,
    ): APIResponse<MutableList<SongDetailModel>>

    @GET("mybl/getradiosdataall")
    suspend fun fetchGetAllRadio(): APIResponse<MutableList<HomePatchDetailModel>>

    @GET("Playlist/GetPlaylistContentById")
    fun fetchGetRadioListByContentById(
        @Query("id") id: String,
    ): Call<APIResponse<MutableList<SongDetailModel>>>

    @GET("Podcast/PodcastbyepisodeIdV3")
    suspend fun fetchPodcastByID(
        @Query("podType") podType: String,
        @Query("episodeId") episodeId: String,
        @Query("contentTYpe") contentTYpe: String,
        @Query("isPaid") isPaid: Boolean,
    ): PodcastModel

    @GET("ClientPodcast/PodcastHomeDataV1")
    suspend fun fetchFeturedPodcast(
        @Query("isPaid") isPaid: Boolean,
    ): FeaturedPodcastModel

    @GET("Podcast/PodcastV3")
    suspend fun fetchPodcastShow(
        @Query("podType") podType: String,
        @Query("contentType") contentType: String,
        @Query("isPaid") isPaid: Boolean,
    ): PodcastModel

    @GET("ClientPodcast/PodcastMainLandingdata")
    suspend fun fetchPodcastSeeAll(
        @Query("isPaid") isPaid: Boolean,
    ): FeaturedPodcastModel

    @GET("Podcast/PodcastHomeDataV1")
    suspend fun fetchShadhinPodcastSeeAll(
        @Query("isPaid") isPaid: Boolean,
    ): FeaturedPodcastModel

    @GET("artist/getpopularartist")
    suspend fun fetchPopularArtist(): PopularArtistModel

    @GET("video/getlatestvideo")
    suspend fun fetchLatestVideo(): LatestVideoModel

    @GET("track/GetLatestTrack")
    suspend fun fetchFeaturedTrackList(): APIResponse<MutableList<FeaturedSongDetailModel>>

    @GET("RBTPWA/GETPWATOKEN")
    suspend fun rbtURL(): RBTModel

    @GET("Search/SearchByKeyword")
    suspend fun getSearch(
        @Query("keyword") keyword: String,
    ): SearchModel

    @GET("Track/TopTrending")
    suspend fun getTopTrendingItems(@Query("type") type: String): TopTrendingModel

//    @GET("mybl/Login")
//    suspend fun login(@Header("Authorization") token: String): LoginResponse


    @POST("Playlist/PostUserplayList")
    suspend fun createPlaylist(@Body body: PlaylistBody): CreatePlaylistResponseModel

    @GET("Playlist/Userplaylist")
    suspend fun getUserPlaylist(): UserPlayListModel

    @POST("Playlist/PostUserplayListContent")
    suspend fun songsAddedtoPlaylist(@Body body: SongsAddedtoPlaylistBody): CreatePlaylistResponseModel

    @GET("Playlist/GetUserPlaylist")
    suspend fun getUserSongsInPlaylist(@Query("id") id: String): UserSongsPlaylistModel

    /* @DELETE("Playlist/DeleteUserPlaylistContent")*/

    @HTTP(method = "DELETE", path = "Playlist/DeleteUserPlaylistContent", hasBody = true)
    suspend fun songDeletedfromPlaylist(@Body body: SongsAddedtoPlaylistBody): CreatePlaylistResponseModel

    @HTTP(method = "DELETE", path = "Playlist/DeleteUserplayList", hasBody = false)
    suspend fun deletePlaylist(@Query("id") id: String): CreatePlaylistResponseModel

    @GET("Favourite/GetFavourite")
    suspend fun fetchAllFavoriteByType(@Query("type") type: String): FavDataResponseModel

    @POST("Favourite")
    suspend fun addToFavorite(@Body body: AddtoFavBody): FavDataResponseModel

    @HTTP(method = "DELETE", path = "Favourite", hasBody = true)
    suspend fun deleteFavorite(@Body body: AddtoFavBody): FavDataResponseModel

    @POST("RobiUserActivity/UserSession")
    suspend fun userSession(@Body body: UserSessionBody): UserSessionResponse

    @POST("RobiUserActivity/PatchClickhistory")
    suspend fun fetchPatchClickHistory(@Body body: HistoryModel): ClickHistoryModel


    @GET("User/GetUserInfo")
    suspend fun getUserInfo(): UserProfileResponseModel

//    @POST("User/RobiUserProfileUpdate ")
//    fun updateUserProfile(fullName: RequestBody?, birthDate: RequestBody?, gender: RequestBody?, profileImage: MultipartBody.Part?) :UserProfileResponseModel

    @Multipart
    @POST("User/RobiUserProfileUpdate")
    suspend fun updateUserProfile(
        @Part("UserFullName") fullName: RequestBody?,
        @Part("BirthDate") birthDate: RequestBody?,
        @Part("Gender") gender: RequestBody?,
        @Part profileImage: MultipartBody.Part? = null,
    ): UserProfileResponseModel

    //@FormUrlEncoded
//@POST("User/RobiUserProfileUpdate")
//suspend fun updateUserProfile(
//    @Field("UserFullName") fullName: String,
//    @Field("BirthDate") birthDate: String?,
//    @Field("Gender") gender: String?
//):UserProfileResponseModel
    @GET("Comment/GetListV2/{id}/{type}/{page}")
   suspend fun getAllComment(
        @Path("id") id: String?,
        @Path("type") type: String?,
        @Path("page") page: Int,
    ): CommentResponse

    @GET("Reply/GetList/{id}")
    suspend fun getAllReply(@Path("id") id: Int): CommentReplyResponse
}