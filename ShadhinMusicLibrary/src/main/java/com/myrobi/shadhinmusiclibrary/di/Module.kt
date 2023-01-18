package com.myrobi.shadhinmusiclibrary.di

import android.content.Context
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.myrobi.shadhinmusiclibrary.ShadhinSDKCallback
import com.myrobi.shadhinmusiclibrary.data.remote.ApiLoginService
import com.myrobi.shadhinmusiclibrary.data.remote.ApiService
import com.myrobi.shadhinmusiclibrary.data.remote.SubscriptionApiService
import com.myrobi.shadhinmusiclibrary.data.repository.*
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistBannerContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.ArtistContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.AuthRepository
import com.myrobi.shadhinmusiclibrary.data.repository.ClientActivityContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.HomeContentRepository
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionCheckRepository
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionCheckRepositoryImpl
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepositoryImpl
import com.myrobi.shadhinmusiclibrary.di.single.*
import com.myrobi.shadhinmusiclibrary.di.single.RetrofitClient
import com.myrobi.shadhinmusiclibrary.di.single.SingleDownloadMap
import com.myrobi.shadhinmusiclibrary.di.single.SingleMusicServiceConnection
import com.myrobi.shadhinmusiclibrary.di.single.SinglePlayerApiService
import com.myrobi.shadhinmusiclibrary.fragments.album.AlbumViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.amar_tunes.AmarTunesViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.artist.*
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistAlbumViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistBannerViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistContentViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.artist.PopularArtistViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.CreatePlaylistViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.history.ClientActivityViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.home.HomeViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.podcast.FeaturedPodcastViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.podcast.PodcastViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.search.SearchViewModelFactory
import com.myrobi.shadhinmusiclibrary.fragments.subscription.SubscriptionViewModelFactory
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.MusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.PlayerApiService
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.ShadhinMusicRepository
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.user_history.UserHistoryRepository
import com.myrobi.shadhinmusiclibrary.library.player.data.rest.user_history.UserHistoryRepositoryImpl
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.UtilsOkHttp
import com.myrobi.shadhinmusiclibrary.library.player.connection.MusicServiceController
import com.myrobi.shadhinmusiclibrary.library.player.singleton.PlayerCache
import com.myrobi.shadhinmusiclibrary.library.player.ui.PlayerViewModelFactory


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

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

    private fun getShadhinMusicAuthServiceV5(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstantUtils.BASE_URL_API_shadhinmusic)
            .addConverterFactory(GsonConverterFactory.create())
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

    private fun getApiShadhinMusicServiceV5(): ApiService {
        return getShadhinMusicAuthServiceV5().create(ApiService::class.java)
    }

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
        HomeContentRepository(getApiShadhinMusicServiceV5())
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

    private fun subscriptionApiService(): SubscriptionApiService {
        return getRetrofitAPIShadhinMusicInstanceV5WithBearerTokenAndClient().create<SubscriptionApiService>()
    }
    val subscriptionCheckRepository:SubscriptionCheckRepository
        get() = SubscriptionCheckRepositoryImpl(subscriptionApiService())
    val subscriptionRepository:SubscriptionRepository
        get() = SubscriptionRepositoryImpl(subscriptionCheckRepository)

    val subscriptionViewModelFactory:SubscriptionViewModelFactory
        get() = SubscriptionViewModelFactory(subscriptionRepository)

    val sdkCallback: ShadhinSDKCallback?
        get() = SingleCallback.INSTANCE
}