package com.shadhinmusiclibrary.di

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.shadhinmusiclibrary.ShadhinSDKCallback
import com.shadhinmusiclibrary.data.remote.ApiLoginService
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.data.repository.*
import com.shadhinmusiclibrary.di.single.*
import com.shadhinmusiclibrary.fragments.album.AlbumViewModelFactory
import com.shadhinmusiclibrary.fragments.amar_tunes.AmarTunesViewModelFactory
import com.shadhinmusiclibrary.fragments.artist.*
import com.shadhinmusiclibrary.fragments.create_playlist.CreatePlaylistViewModelFactory
import com.shadhinmusiclibrary.fragments.fav.FavViewModelFactory
import com.shadhinmusiclibrary.fragments.history.ClientActivityViewModelFactory
import com.shadhinmusiclibrary.fragments.home.HomeViewModelFactory
import com.shadhinmusiclibrary.fragments.podcast.FeaturedPodcastViewModelFactory
import com.shadhinmusiclibrary.fragments.podcast.PodcastViewModelFactory
import com.shadhinmusiclibrary.fragments.search.SearchViewModelFactory
import com.shadhinmusiclibrary.library.player.connection.MusicServiceController
import com.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.shadhinmusiclibrary.library.player.data.rest.PlayerApiService
import com.shadhinmusiclibrary.library.player.data.rest.ShadhinMusicRepository
import com.shadhinmusiclibrary.library.player.data.rest.user_history.UserHistoryRepository
import com.shadhinmusiclibrary.library.player.data.rest.user_history.UserHistoryRepositoryImpl
import com.shadhinmusiclibrary.library.player.singleton.PlayerCache
import com.shadhinmusiclibrary.library.player.ui.PlayerViewModelFactory
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.UtilsOkHttp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class Module(private val applicationContext: Context) {

//    private fun getOkHttpClientWithFMInterceptor(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(
//                LastFmApiKeyInterceptor()
//            ).build()
//    }
//
//    fun getBaseOkHttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(
//                HeaderInterceptor()
//            ).build()
//    }
//
//    private fun getBaseOkHttpClientWithToken(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(
//                BearerTokenHeaderInterceptor()
//            ).build()
//    }
//
//    private fun getBaseOkHttpClientWithTokenAndClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(
//                BearerTokenWithClientHeaderInterceptor()
//            ).build()
//    }


    private fun getRetrofitFMAPIInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstantUtils.LAST_FM_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(UtilsOkHttp.getOkHttpClientWithFMInterceptor())
            .build()
    }

    private fun getRetrofitAPIShadhinMusicApiLoginInstanceV5(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstantUtils.BASE_URL_API_shadhinmusic)
            .addConverterFactory(GsonConverterFactory.create())
            .client(UtilsOkHttp.getBaseOkHttpClient())
            .build()
    }

    private fun getRetrofitAPIShadhinMusicInstanceV5WithBearerTokenAndClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstantUtils.BASE_URL_API_shadhinmusic)
            .addConverterFactory(GsonConverterFactory.create())
            .client(UtilsOkHttp.getBaseOkHttpClientWithTokenAndClient())
            .build()
    }

    private fun getFMService(): ApiService {
        return getRetrofitFMAPIInstance().create(ApiService::class.java)
    }

    private fun getShadhinMusicLoginAuthServiceV5(): ApiLoginService {
        return getRetrofitAPIShadhinMusicApiLoginInstanceV5().create(ApiLoginService::class.java)
    }

//    private fun getApiShadhinMusicServiceV5withToken(): ApiService {
//        return getRetrofitAPIShadhinMusicInstanceV5WithBearerToken().create(ApiService::class.java)
//    }

    private fun getApiShadhinMusicServiceV5withTokenAndClient(): ApiService {
        return getRetrofitAPIShadhinMusicInstanceV5WithBearerTokenAndClient().create(ApiService::class.java)
    }

    private fun getRetrofitInstance(): Retrofit {
        return RetrofitClient.getInstance(UtilsOkHttp.getBaseOkHttpClientWithTokenAndClient())
    }

    fun authRepository() = AuthRepository(getShadhinMusicLoginAuthServiceV5())

    private val repositoryArtistContent: ArtistContentRepository =
        ArtistContentRepository(getFMService())

    private val artistAlbumApiService: ApiService = getApiShadhinMusicServiceV5withTokenAndClient()
    private val podcastApiService: ApiService = getApiShadhinMusicServiceV5withTokenAndClient()

    private val clientActivityRep = ClientActivityContentRepository(
        getApiShadhinMusicServiceV5withTokenAndClient()
    )

    private val repositoryHomeContent: HomeContentRepository =
        HomeContentRepository(getApiShadhinMusicServiceV5withTokenAndClient())
    private val repositoryArtistBannerContent: ArtistBannerContentRepository =
        ArtistBannerContentRepository(getApiShadhinMusicServiceV5withTokenAndClient())
    private val repositoryArtistSongContent: ArtistSongContentRepository =
        ArtistSongContentRepository(getApiShadhinMusicServiceV5withTokenAndClient())
    private val repositoryHomeContentRBT: AmartunesContentRepository =
        AmartunesContentRepository(getApiShadhinMusicServiceV5withTokenAndClient())
    private val repositoryAlbumContent: AlbumContentRepository =
        AlbumContentRepository(getApiShadhinMusicServiceV5withTokenAndClient())
    private val repositoryCreatePlaylist: CreatePlaylistRepository =
        CreatePlaylistRepository(getApiShadhinMusicServiceV5withTokenAndClient())
    private val repositoryFavContentRepository: FavContentRepository =
        FavContentRepository(getApiShadhinMusicServiceV5withTokenAndClient())

    val clientActivityVM: ClientActivityViewModelFactory
        get() = ClientActivityViewModelFactory(clientActivityRep)

    val factoryHomeVM: HomeViewModelFactory
        get() = HomeViewModelFactory(repositoryHomeContent)

    val factoryAmarTuneVM: AmarTunesViewModelFactory
        get() = AmarTunesViewModelFactory(repositoryHomeContentRBT)

    val factoryArtistVM: ArtistViewModelFactory
        get() = ArtistViewModelFactory(repositoryArtistContent)

    val factoryAlbumVM: AlbumViewModelFactory
        get() = AlbumViewModelFactory(repositoryAlbumContent)

    val factoryArtistBannerVM: ArtistBannerViewModelFactory
        get() = ArtistBannerViewModelFactory(repositoryArtistBannerContent)

    val factoryCreatePlaylistVM: CreatePlaylistViewModelFactory
        get() = CreatePlaylistViewModelFactory(repositoryCreatePlaylist)

    val factoryFavContentVM: FavViewModelFactory
        get() = FavViewModelFactory(repositoryFavContentRepository)

    val factoryArtistSongVM: ArtistContentViewModelFactory
        get() = ArtistContentViewModelFactory(repositoryArtistSongContent)

    private val artistAlbumContentRepository: ArtistAlbumContentRepository
        get() = ArtistAlbumContentRepository(
            artistAlbumApiService
        )

    val artistAlbumViewModelFactory: ArtistAlbumViewModelFactory
        get() = ArtistAlbumViewModelFactory(
            artistAlbumContentRepository
        )

    private val featuredpodcastRepository: FeaturedPodcastRepository
        get() = FeaturedPodcastRepository(
            getApiShadhinMusicServiceV5withTokenAndClient()
        )
    val featuredpodcastViewModelFactory: FeaturedPodcastViewModelFactory
        get() = FeaturedPodcastViewModelFactory(featuredpodcastRepository)

    private val podcastRepository: PodcastRepository
        get() = PodcastRepository(
            podcastApiService
        )
    val podcastViewModelFactory: PodcastViewModelFactory
        get() = PodcastViewModelFactory(podcastRepository)

    val popularArtistRepository: PopularArtistRepository
        get() = PopularArtistRepository(
            artistAlbumApiService
        )
    val popularArtistViewModelFactory: PopularArtistViewModelFactory
        get() = PopularArtistViewModelFactory(
            popularArtistRepository
        )

    val featuredtrackListRepository: FeaturedTracklistRepository
        get() = FeaturedTracklistRepository(
            artistAlbumApiService
        )
    val featuredtrackListViewModelFactory: FeaturedTracklistViewModelFactory
        get() = FeaturedTracklistViewModelFactory(
            featuredtrackListRepository
        )

    val searchRepository: SearchRepository
        get() = SearchRepository(
            artistAlbumApiService
        )
    val searchViewModelFactory: SearchViewModelFactory
        get() = SearchViewModelFactory(
            searchRepository
        )

    val exoplayerCache: SimpleCache
        get() = PlayerCache.getInstance(applicationContext)

    private val playerApiService: PlayerApiService
        get() = SinglePlayerApiService.getInstance(getRetrofitInstance())

    val musicRepository: MusicRepository = ShadhinMusicRepository(playerApiService)

    val musicServiceController: MusicServiceController
        get() = SingleMusicServiceConnection.getInstance(applicationContext)//ShadhinMusicServiceConnection(applicationContext)

    val userHistoryRepository: UserHistoryRepository
        get() = UserHistoryRepositoryImpl(playerApiService)

    val downloadTitleMap: MutableMap<String, String>
        get() = SingleDownloadMap.getInstance()
    private val _userSessionRepository: UserSessionRepository =
        UserSessionRepositoryImpl(getApiShadhinMusicServiceV5withTokenAndClient())

    val userSessionRepository: UserSessionRepository
        get() = _userSessionRepository


    val playerViewModelFactory: PlayerViewModelFactory
        get() = PlayerViewModelFactory(musicServiceController, userSessionRepository)

    val sdkCallback:ShadhinSDKCallback?
        get() = SingleCallback.INSTANCE
}