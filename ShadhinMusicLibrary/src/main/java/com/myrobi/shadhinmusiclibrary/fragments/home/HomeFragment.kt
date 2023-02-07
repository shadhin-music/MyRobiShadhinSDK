package com.myrobi.shadhinmusiclibrary.fragments.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.myrobi.shadhinmusiclibrary.activities.SDKMainActivity
import com.myrobi.shadhinmusiclibrary.adapter.HomeFooterAdapter
//import com.myrobi.shadhinmusiclibrary.adapter.NewReleaseSliderpagerAdapter
import com.myrobi.shadhinmusiclibrary.adapter.ParentAdapter
import com.myrobi.shadhinmusiclibrary.callBackService.DownloadClickCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.myrobi.shadhinmusiclibrary.callBackService.SearchClickCallBack
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.*
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.myrobi.shadhinmusiclibrary.fragments.amar_tunes.AmarTunesViewModel
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.DataContentType
import com.myrobi.shadhinmusiclibrary.utils.TimeParser
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper
import kotlinx.coroutines.launch
import java.io.Serializable

internal class HomeFragment : BaseFragment(),
    HomeCallBack,
    SearchClickCallBack,
    DownloadClickCallBack,
    PodcastTrackCallback,NewReleaseTrackCallback {

    private  var concatAdapter: ConcatAdapter?=null
    private lateinit var favViewModel: FavViewModel
    private var globalRootContentId = ""

    private var dataAdapter: ParentAdapter? = null
    private var pageNum = 1
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewModelAmaraTunes: AmarTunesViewModel


    private  var footerAdapter: HomeFooterAdapter?=null
    private lateinit var cacheRepository: CacheRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.my_bl_sdk_fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        cacheRepository = CacheRepository(requireContext())
        setupViewModel()
        setupAdapter(view)
        homeViewModel.fetchHomeData()

        favViewModel.getFavContentArtist("A")
        favViewModel.getFavContentPodcast("PD")
        favViewModel.getFavContentAlbum("R")
        favViewModel.getFavContentVideo("V")
        favViewModel.getFavContentSong("S")
        favViewModel.getFavContentPlaylist("P")

        observeData()
    }
    private fun setupUi(view: View) {
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
    }

    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
    }

    private fun setupViewModel() {
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
    }
    private fun setupAdapter(view: View) {

        footerAdapter = HomeFooterAdapter()
        dataAdapter = ParentAdapter(this, this, this, this,this)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.layoutManager = layoutManager

        val config = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        concatAdapter = ConcatAdapter(config, dataAdapter)
        recyclerView.adapter = concatAdapter

    }
    private fun observeData() {
      //  playerViewModel.startObservePlayerProgress(viewLifecycleOwner)
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)

        homeViewModel.patchList.observe(viewLifecycleOwner) { patchList ->
            dataAdapter?.submitList(patchList)

            progressBar.visibility = View.GONE
            if(concatAdapter !=null && footerAdapter !=null && concatAdapter?.adapters?.contains(footerAdapter) == false){
                concatAdapter?.addAdapter(footerAdapter!!)

            }

        }




        try {
            favViewModel.getFavContentAlbum.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    //Log.e("TAG","FAV"+ res.data.)
                    cacheRepository.deleteAllFavByType("R")
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentPodcast.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.deleteAllFavPodcast()
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentArtist.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.deleteAllFavByType("A")
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentVideo.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.deleteAllFavByType("V")
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }

            favViewModel.getFavContentSong.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.deleteAllFavByType("S")
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
            favViewModel.getFavContentPlaylist.observe(viewLifecycleOwner) { res ->
                if (res?.status == "success") {
                    cacheRepository.deleteAllFavByType("P")
                    cacheRepository.insertFavoriteContent(res.data?.toMutableList())
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun onClickItemAndAllItem(
        itemPosition: Int,
        selectedHomePatchItem: HomePatchItemModel
    ) {
        patchMonitoring(selectedHomePatchItem)
        route(selectedHomePatchItem, itemPosition)
    }

    private fun route(homePatchItem: HomePatchItemModel, selectedIndex: Int?) {

        if (selectedIndex != null && homePatchItem.Data.size > selectedIndex) {

            val homePatchDetail = homePatchItem.Data[selectedIndex]

            val podcast: String = homePatchDetail.content_Type ?: ""

            if (homePatchDetail.content_Type?.contains("PD") == true) {
                val bundle = Bundle().apply {
                    putSerializable(
                        AppConstantUtils.PatchItem,
                        HomePatchItemModel(
                            homePatchItem.Code,
                            homePatchItem.ContentType,
                            homePatchItem.Data,
                            homePatchItem.Design,
                            homePatchItem.Name,
                            homePatchItem.Sort,
                            homePatchItem.Total
                        ) as Serializable
                    )
                    putSerializable(
                        AppConstantUtils.PatchDetail,
                        homePatchDetail as Serializable
                    )
                }
                Log.e("TAG","Podcast: " + homePatchItem )
                Log.e("TAG","Podcast: " + homePatchDetail.album_Id)
                Log.e("TAG","Podcast: " + homePatchDetail.content_Id)
                Log.e("TAG","data: " +homePatchDetail.album_Id )
                findNavController().navigate(R.id.to_podcast_details, bundle)
            }
            val bundle = Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    homePatchItem
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    homePatchDetail as Serializable
                )
            }
            Log.e("TAG","data: " +homePatchDetail.content_Type )
            val action = when (homePatchDetail.content_Type?.uppercase()) {

                DataContentType.CONTENT_TYPE_A -> R.id.to_artist_details
                DataContentType.CONTENT_TYPE_R -> R.id.to_album_details
                DataContentType.CONTENT_TYPE_P -> R.id.to_playlist_details
                DataContentType.CONTENT_TYPE_S -> R.id.to_s_type_details

                else -> null
            }

            action?.let { a-> findNavController().navigate(a, bundle) }


        } else {
            if (homePatchItem.ContentType.contains("PD",true)) {

                findNavController().navigate(R.id.to_podcast_see_all_fragment,   Bundle().apply {
                    putSerializable(
                        AppConstantUtils.PatchItem,
                        homePatchItem as Serializable
                    )
                    Log.e("TAG","Podcast: " + homePatchItem )
                })
            }
            if (homePatchItem.ContentType.contains("VD",true)) {

                findNavController().navigate(R.id.to_large_video_list_fragment,   Bundle().apply {
                    putSerializable(
                        AppConstantUtils.PatchItem,
                        homePatchItem as Serializable
                    )
                })
            }

            val bundle = Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    homePatchItem as Serializable
                )
            }
            when (homePatchItem.ContentType.uppercase()) {
                DataContentType.CONTENT_TYPE_PS -> {
                    Log.e("TAG","Podcast: " +bundle )
                    findNavController().navigate(R.id.to_podcast_see_all_fragment,  bundle )

                }
                DataContentType.CONTENT_TYPE_A -> {
                    findNavController().navigate(R.id.to_popular_artist_fragment,bundle)
                }

                DataContentType.CONTENT_TYPE_R -> {
                    findNavController().navigate(R.id.to_release_list_fragment,bundle)
                }

                DataContentType.CONTENT_TYPE_P -> {
                    findNavController().navigate(R.id.to_playlist_list_fragment,bundle)
                }

                DataContentType.CONTENT_TYPE_S -> {
                    findNavController().navigate(R.id.to_s_type_list_fragment,bundle)
                }

                DataContentType.CONTENT_TYPE_V -> {
                    findNavController().navigate(R.id.to_video_list_fragment,bundle)
                }
//                DataContentType.CONTENT_TYPE_PODCAST_VIDEO -> {
//                    findNavController().navigate(R.id.to_video_list_fragment,bundle)
//                }
            }
        }
    }

    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel) {

        patchMonitoring(selectedHomePatchItem)
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        route(selectedHomePatchItem,null)
    }

    override fun onClickItemPodcastEpisode(
        itemPosition: Int,
        selectedEpisode: List<EpisodeModel>
    ) {

    }


    override fun clickOnSearchBar(selectedHomePatchItem: HomePatchItemModel) {

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
    override fun clickOnDownload(selectedHomePatchItem: HomePatchItemModel) {
        lifecycleScope.launch {

            val data = Bundle()
            data.putSerializable(
                AppConstantUtils.PatchItem,
                selectedHomePatchItem as Serializable
            )

            if(homeViewModel.haveActiveSubscriptionPlan()) {
                findNavController().navigate(R.id.to_download, data)
            }else{
                findNavController().navigate(R.id.to_subscription_not_found)
            }
        }

      /*  startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Download
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })*/
    }

    override fun clickOnWatchLater(selectedHomePatchItem: HomePatchItemModel) {
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        findNavController().navigate(R.id.to_watch_later, data)

       /* startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Watchlater
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })*/
    }

    override fun clickOnMyPlaylist(selectedHomePatchItem: HomePatchItemModel) {
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        lifecycleScope.launch {
            if (homeViewModel.haveActiveSubscriptionPlan()) {
                findNavController().navigate(R.id.to_my_playlist,data)
            } else {
                findNavController().navigate(R.id.to_subscription_not_found)
            }
        }

    }

    override fun clickOnMyFavorite(selectedHomePatchItem: HomePatchItemModel) {
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        findNavController().navigate(R.id.to_favorite,data)
        /*startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_MyFavorite
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })*/
    }

    override fun clickOnArtist(selectedHomePatchItem: HomePatchItemModel) {

//        val bundle = Bundle().apply {
//            putSerializable(
//                AppConstantUtils.PatchItem,
//                selectedHomePatchItem as Serializable
//            )
//        }
        findNavController().navigate(R.id.to_userProfileUpdateFragment)
    }
    override fun clickOnPodcast(selectedHomePatchItem: HomePatchItemModel) {
      /*  val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )

        Bundle().apply {
            val details = HomePatchDetailModel().apply {
                this.content_Type = type
            }
            putSerializable(
                AppConstantUtils.PatchDetail,
                details as Serializable
            )
        }*/
        findNavController().navigate(R.id.podcast_see_all_fragment)
       /* startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Podcast
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })*/
    }


    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        setMusicPlayerInitData(mSongDetails, clickItemPosition)
    }

    override fun onClickRadioItem(currentSong: IMusicModel) {
        globalRootContentId = currentSong.content_Id
        if (playerViewModel.currentMusic != null) {
            if ((globalRootContentId == playerViewModel.currentMusic?.rootId)) {
                playerViewModel.togglePlayPause()
            } else {
                ShadhinMusicSdkCore.openRadio(requireContext(), globalRootContentId)
            }
        } else {
            ShadhinMusicSdkCore.openRadio(requireContext(), globalRootContentId)
        }
    }



//        override fun getCurrentVH(
//            currentVH: NewReleaseSliderpagerAdapter.ViewHolder?,
//            data: List<IMusicModel>
//        ) {
//            val trackViewHolder = currentVH as NewReleaseSliderpagerAdapter.ViewHolder
//
//            trackViewHolder.let {
//                if (isAdded) {
//                    playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMusic ->
//                        if (itMusic != null) {
//                            trackViewHolder.item.let {
//
//                                    playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
//                                        if (it?.content_Id == itMusic.mediaId &&
//                                            it?.content_Type == itMusic.contentType
//                                        ) {
//                                            if (itPla != null){
//                                                playPauseStateRed(itPla.isPlaying,
//                                                    trackViewHolder.ivPlayBtn!!)
//                                                Log.e("TAG","DATA: " + itMusic.mediaId)
//                                            }
//                                        } else {
//                                            Log.e("TAG","DATA: " + isAdded)
//                                            Log.e("TAG","DATA123: " + itMusic.rootType)
//                                            trackViewHolder.ivPlayBtn?.let { playPauseStateRed(false, it) }
//                                        }
//                                    }
//
//
//                            }
//                        }
//                    }
//                }
//            }
//        }


        override fun onTrackClick(data:  MutableList<IMusicModel>, position: Int) {

            if (playerViewModel.currentMusic != null && (data[position].content_Id == playerViewModel.currentMusic?.mediaId)) {
                if ((data[position].content_Id!= playerViewModel.currentMusic?.mediaId)) {
                    playerViewModel.skipToQueueItem(position)
                } else {
                    playerViewModel.togglePlayPause()
                }
            } else {
                playerViewModel.unSubscribe()
                playerViewModel.subscribe(
                    MusicPlayList(
                        UtilHelper.getMusicListToSongDetailList(data),

                        0
                    ),
                    false,
                    position
                )
            }
        }



}
internal interface NewReleaseTrackCallback {
//    fun getCurrentVH(
//        currentVH: NewReleaseSliderpagerAdapter.ViewHolder?,
//        data: List<IMusicModel>,
//    )
    fun onTrackClick(data: MutableList<IMusicModel>, position: Int)
}