package com.shadhinmusiclibrary.fragments.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.adapter.ParentAdapter
import com.shadhinmusiclibrary.callBackService.DownloadClickCallBack
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.shadhinmusiclibrary.callBackService.SearchClickCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomeDataModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.shadhinmusiclibrary.fragments.amar_tunes.AmarTunesViewModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.fragments.history.ClientActivityViewModel
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.library.player.utils.isPlaying
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.Status
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable

internal class HomeFragment : BaseFragment(),
    HomeCallBack,
    SearchClickCallBack,
    DownloadClickCallBack,
    PodcastTrackCallback {

    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var favViewModel: FavViewModel

    //mini music player
    private lateinit var llMiniMusicPlayer: CardView
    private lateinit var ivSongThumbMini: ImageView
    private lateinit var tvSongNameMini: TextView
    private lateinit var tvSingerNameMini: TextView
    private lateinit var tvTotalDurationMini: TextView
    private lateinit var ibtnSkipPreviousMini: ImageButton
    private lateinit var ibtnPlayPauseMini: ImageButton
    private lateinit var ibtnSkipNextMini: ImageButton

    private var dataAdapter: ParentAdapter? = null
    private var pageNum = 1
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewModelAmaraTunes: AmarTunesViewModel
//    private lateinit var albumVM: AlbumViewModel

    //var page = -1
    var isLoading = false
    var isLastPage = false

    private lateinit var footerAdapter: HomeFooterAdapter
    private lateinit var cacheRepository: CacheRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_fragment_home, container, false)

        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiInitMiniMusicPlayer(view)
        cacheRepository = CacheRepository(requireContext())
        homeViewModel = ViewModelProvider(
            this,
            injector.factoryHomeVM
        )[HomeViewModel::class.java]

        viewModelAmaraTunes = ViewModelProvider(
            this,
            injector.factoryAmarTuneVM
        )[AmarTunesViewModel::class.java]

        favViewModel = ViewModelProvider(
            this,
            injector.factoryFavContentVM
        )[FavViewModel::class.java]

        homeViewModel.fetchHomeData(pageNum, false)

        favViewModel.getFavContentArtist("A")
        favViewModel.getFavContentPodcast("PD")
        favViewModel.getFavContentAlbum("R")
        favViewModel.getFavContentVideo("V")
        favViewModel.getFavContentSong("S")
        favViewModel.getFavContentPlaylist("P")

        observeData()
    }

    private fun observeData() {
        playerViewModel.startObservePlayerProgress(viewLifecycleOwner)
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        homeViewModel.homeContent.observe(viewLifecycleOwner) { res ->
            if (res.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (res.data?.data?.isNotEmpty() == true) {
                    viewDataInRecyclerView(res.data)
                }
            } else {
                progressBar.visibility = GONE
                pageNum = 1
                homeViewModel.fetchHomeData(pageNum, false)
            }
            isLoading = false
        }
        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMus ->
            if (itMus != null) {
                setupMiniMusicPlayerAndFunctionality(UtilHelper.getSongDetailToMusic(itMus))
            }
        }
        playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) {
            if (it != null)
                miniPlayerPlayPauseState(it.isPlaying)
        }
        playerViewModel.playerProgress.observe(viewLifecycleOwner) {
            tvTotalDurationMini.text = it.currentPositionTimeLabel()
        }

        if (playerViewModel.isMediaDataAvailable()) {
            llMiniMusicPlayer.visibility = View.VISIBLE
        } else {
            llMiniMusicPlayer.visibility = GONE
        }

        try {
            favViewModel.getFavContentAlbum.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    //Log.e("TAG","FAV"+ res.data.)
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentPodcast.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentArtist.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentVideo.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }

            favViewModel.getFavContentSong.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentPlaylist.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun viewDataInRecyclerView(homeData: HomeDataModel?) {
        if (dataAdapter == null) {
            footerAdapter = HomeFooterAdapter()
            dataAdapter = ParentAdapter(this, this, this, this)

            val recyclerView: RecyclerView = view?.findViewById(R.id.recyclerView)!!
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            isLoading = true
                            homeViewModel.fetchHomeData(++pageNum, false)
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
            val config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
            concatAdapter = ConcatAdapter(config, dataAdapter)
            recyclerView.adapter = concatAdapter
            //recyclerView.adapter =  dataAdapter

//            concatAdapter.removeAdapter(dataAdapter)
        }
        /* viewModelAmaraTunes.urlContent.observe(viewLifecycleOwner) { res ->
             if (res.status == Status.SUCCESS) {
                 this.rbtData = res.data?.data
             }
         }*/

        homeData.let {
            for (item in it?.data?.indices!!) {
                if (isValidDesign(it.data[item].Design) == -1) {
                    it.data[item].Design = ""
                }
                if (it.data[item].Design.isNotEmpty()) {
                    it.data[item].let { it1 ->
                        dataAdapter?.setData(listOf(it1))
                        //dataAdapter?.notifyItemChanged(pageNum)
                        dataAdapter?.notifyDataSetChanged()
                    }
//                  it.data.let {
                    //   it1 ->
                    // }
                }
            }
        }
        if (homeData?.total == pageNum) {
            isLastPage = true
            //Log.e("TAG","PAGE NUMBER: "+ pageNum)
            val config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
            val recyclerView: RecyclerView = view?.findViewById(R.id.recyclerView)!!
            concatAdapter.addAdapter(footerAdapter)
            //recyclerView.adapter = ConcatAdapter(config, dataAdapter, footerAdapter)
        }
    }

    private fun isValidDesign(design: String): Int {
        return when (design) {
//            "search" -> VIEW_SEARCH
            "Artist" -> ParentAdapter.VIEW_ARTIST
            "Playlist" -> ParentAdapter.VIEW_PLAYLIST
            "Release" -> ParentAdapter.VIEW_RELEASE
            "Track" -> ParentAdapter.VIEW_RELEASE
            "Podcast" -> ParentAdapter.VIEW_POPULAR_PODCAST
            "SmallVideo" -> ParentAdapter.VIEW_TRENDING_MUSIC_VIDEO
            "amarTune" -> ParentAdapter.VIEW_POPULAR_AMAR_TUNES
            "download" -> ParentAdapter.VIEW_DOWNLOAD
            "PodcastLive" -> ParentAdapter.VIEW_PODCAST_LIVE
            "Show" ->ParentAdapter.VIEW_PODCAST_LIVE
            "Discover" ->ParentAdapter.VIEW_BANNER
            else -> {
                -1
            }
        }
    }

    override fun onClickItemAndAllItem(
        itemPosition: Int,
        selectedHomePatchItem: HomePatchItemModel
    ) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        patchMonitoring(selectedHomePatchItem)
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(
            Intent(requireActivity(), SDKMainActivity::class.java)
                .apply {
                    putExtra(
                        AppConstantUtils.UI_Request_Type,
                        AppConstantUtils.Requester_Name_Home
                    )
                    putExtra(AppConstantUtils.PatchItem, data)
                    putExtra(AppConstantUtils.SelectedPatchIndex, itemPosition)
                })
//        val valueCon = selectedHomePatchItem.Data[itemPosition].ContentID
//        fetchOnlineData(valueCon)
    }

    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        patchMonitoring(selectedHomePatchItem)
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(
            Intent(requireActivity(), SDKMainActivity::class.java)
                .apply {
                    putExtra(
                        AppConstantUtils.UI_Request_Type,
                        AppConstantUtils.Requester_Name_Home
                    )
                    putExtra(AppConstantUtils.PatchItem, data)
                })
    }

    override fun onClickItemPodcastEpisode(
        itemPosition: Int,
        selectedEpisode: List<EpisodeModel>
    ) {

    }


    private fun fetchOnlineData(playlistId: String) {
//        albumVM.fetchPlaylistContent(playlistId)
//        albumVM.albumContent.observe(viewLifecycleOwner) { res ->
//            if (res.data?.data != null && res.status == Status.SUCCESS) {
//                setMusicPlayerInitData(res.data.data.toMutableList(), 0)
//                res.data.data
//            }
//        }
    }

    override fun clickOnSearchBar(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(
            Intent(requireActivity(), SDKMainActivity::class.java)
                .apply {
                    putExtra(
                        AppConstantUtils.UI_Request_Type,
                        AppConstantUtils.Requester_Name_Search
                    )
                    putExtra(AppConstantUtils.PatchItem, data)
                })
    }

    fun setMusicPlayerInitData(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        /* if(BuildConfig.DEBUG){
       mSongDetails.forEach {
           it.PlayUrl = "https://cdn.pixabay.com/download/audio/2022/01/14/audio_88400099c4.mp3?filename=madirfan-demo-20-11-2021-14154.mp3"
       }
   }*/
        playerViewModel.unSubscribe()
        playerViewModel.subscribe(
            MusicPlayList(
                UtilHelper.getMusicListToSongDetailList(mSongDetails),
                0
            ),
            false,
            clickItemPosition
        )
    }

    //Copy paste from SDKMainActivity
    private fun uiInitMiniMusicPlayer(view: View) {
        llMiniMusicPlayer = view.findViewById(R.id.include_mini_music_player)
        ivSongThumbMini = view.findViewById(R.id.iv_song_thumb_mini)
        tvSongNameMini = view.findViewById(R.id.tv_song_name_mini)
        tvSingerNameMini = view.findViewById(R.id.tv_singer_name_mini)
        tvTotalDurationMini = view.findViewById(R.id.tv_total_duration_mini)
        ibtnSkipPreviousMini = view.findViewById(R.id.ibtn_skip_previous_mini)
        ibtnPlayPauseMini = view.findViewById(R.id.ibtn_play_pause_mini)
        ibtnSkipNextMini = view.findViewById(R.id.ibtn_skip_next_mini)
    }

    //Copy paste from SDKMainActivity
    private fun setupMiniMusicPlayerAndFunctionality(mSongDetails: SongDetailModel) {
        if (mSongDetails.isSeekAble!!) {
            ibtnSkipPreviousMini.visibility = View.VISIBLE
            ibtnSkipPreviousMini.setOnClickListener {
                playerViewModel.skipToPrevious()
            }
            ibtnSkipNextMini.visibility = View.VISIBLE
            ibtnSkipNextMini.setOnClickListener {
                playerViewModel.skipToNext()
            }
        } else {
            ibtnSkipPreviousMini.visibility = View.INVISIBLE
            ibtnSkipNextMini.visibility = View.INVISIBLE
        }

        Glide.with(this)
            .load(UtilHelper.getImageUrlSize300(mSongDetails.imageUrl ?: ""))
            .transition(DrawableTransitionOptions().crossFade(500))
            .fitCenter()
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
            .placeholder(R.drawable.my_bl_sdk_default_song)
            .error(R.drawable.my_bl_sdk_default_song)
            .into(ivSongThumbMini)

        tvSongNameMini.text = mSongDetails.titleName
        tvSingerNameMini.text = mSongDetails.artistName
        tvTotalDurationMini.text = TimeParser.secToMin(mSongDetails.total_duration)
        llMiniMusicPlayer.visibility = View.VISIBLE

        ibtnPlayPauseMini.setOnClickListener {
            playerViewModel.togglePlayPause()
        }
    }

    //Copy paste from SDKMainActivity
    private fun miniPlayerPlayPauseState(playing: Boolean) {
        if (playing) {
            ibtnPlayPauseMini.setImageResource(R.drawable.my_bl_sdk_ic_baseline_pause_24)
        } else {
            ibtnPlayPauseMini.setImageResource(R.drawable.my_bl_sdk_ic_baseline_play_arrow_black_24)
        }
    }

    override fun clickOnDownload(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Download
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })
    }

    override fun clickOnWatchLater(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Watchlater
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })
    }

    override fun clickOnMyPlaylist(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_MyPlaylist
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })
    }

    override fun clickOnMyFavorite(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_MyFavorite
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.startObservePlayerProgress(viewLifecycleOwner)
        playerViewModel.playerProgress.observe(viewLifecycleOwner) {
            tvTotalDurationMini.text = it.currentPositionTimeLabel()
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        Log.e("podcast", "clickItemPosition " + mSongDetails[clickItemPosition].titleName)
/*        if (playerViewModel.currentMusic != null &&
            (mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)
        ) {
            if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                playerViewModel.skipToQueueItem(clickItemPosition)
            } else {
                playerViewModel.togglePlayPause()
            }
        } else {
            playItem(
                mSongDetails as MutableList<IMusicModel>,
                clickItemPosition
            )
        }*/

/*        playItem(
            mSongDetails as MutableList<IMusicModel>,
            clickItemPosition
        )*/
        setMusicPlayerInitData(mSongDetails, clickItemPosition)
    }
}