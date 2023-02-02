package com.myrobi.shadhinmusiclibrary.fragments.podcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.activities.SDKMainActivity
import com.myrobi.shadhinmusiclibrary.adapter.*
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.PodcastBottomSheetDialogItemCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.DownloadingItem
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.DataModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.Status
import java.lang.reflect.Type

internal class PodcastDetailsFragment : BaseFragment(),
    HomeCallBack,
    CommonPlayControlCallback,
    PodcastBottomSheetDialogItemCallback {

    private lateinit var viewModel: PodcastViewModel
    private lateinit var commentsViewModel: CommentsViewModel
    private lateinit var navController: NavController

    private lateinit var podcastHeaderAdapter: PodcastHeaderAdapter
    private lateinit var podcastTrackAdapter: PodcastTrackAdapter
    private lateinit var podcastMoreEpisodesAdapter: PodcastMoreEpisodesAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var podcastCommentsHeaderAdapter: PodcastCommentsHeaderAdapter
    var data: DataModel? = null
    var episode: List<EpisodeModel>? = null

    var podcastType: String = ""
    var contentType: String = ""
    private var selectedEpisodeID: String = ""
    var contentId: String = ""
    private lateinit var footerAdapter: HomeFooterAdapter
    private lateinit var parentRecycler: RecyclerView
    private var cacheRepository: CacheRepository? = null
    private lateinit var favViewModel: FavViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
        navController = findNavController()
        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        argHomePatchDetail?.let {
            val Type: String = it.content_Type ?: ""
            podcastType = Type.take(2)
            contentType = Type.takeLast(2)
            contentId = it.content_Id
            selectedEpisodeID = it.album_Id ?: it.content_Id
            Log.e("PDF", "getPodcastDetailsInitialize: "+ Type)
        }
        cacheRepository = CacheRepository(requireContext())

        setupViewModel()
        Log.e("PDF", "getPodcastDetailsInitialize: "+ contentId)
        Log.e("PDF", "getPodcastDetailsInitialize: "+selectedEpisodeID)
         commentsViewModel.getAllComments(contentId,contentType,1)
        //Log.e("PDF", "getPodcastDetailsInitialize: "+ contentId)
        if (selectedEpisodeID.isEmpty()) {
//        if (selectedEpisodeID.isEmpty()) {
            getPodcastShowDetailsInitialize()
            //Log.e("PDF", "getPodcastShowDetailsInitialize")
        }else{
            getPodcastDetailsInitialize()

          //  Log.e("PDF", "getPodcastDetailsInitialize")
        }


        setupAdapters()

        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music?.mediaId != null) {
                podcastTrackAdapter.setPlayingSong(music.mediaId!!)
            }
        }

        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        viewModel.podcastDetailsContent.observe(viewLifecycleOwner) { response ->

            Log.i("PDF", "podcastDetailsContent: observe")
            if (response.status == Status.SUCCESS) {
                response.data?.data?.let {
                    it.EpisodeList.let { it1 ->
                        podcastMoreEpisodesAdapter.setData(it1)
                    }
                }
                response?.data?.data?.EpisodeList?.let { itEpisod ->
//                    podcastHeaderAdapter.setHeader(
//                        itEpisod,
//                        itEpisod[0].TrackList
//                    )
                    podcastHeaderAdapter.setTrackData(
                        itEpisod,
                        itEpisod[0].TrackList,
                        argHomePatchDetail!!
                    )
                }
                response.data?.data?.EpisodeList?.get(0)?.let {
                    podcastTrackAdapter.setData(
                        it.TrackList,
                        argHomePatchDetail!!,
                        playerViewModel.currentMusic?.mediaId
                    )
                }

//                parentRecycler.adapter = concatAdapter
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
            }
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
       podcastCommentsObserve()
    }
    private fun podcastCommentsObserve(){
        commentsViewModel.getComments.observe(viewLifecycleOwner){
            Log.e("TAG","getComments: "+ it.message)

            if(it.status==Status.SUCCESS){
                Log.e("TAG","CommentData: "+ it.data?.data)
            }else{
                Log.e("TAG","CommentData: "+ it.status)
            }
        }
    }
    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
        /*startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })*/
    }
    private fun getPodcastShowDetailsInitialize() {
        Log.e("PDF", "getPodcastShowDetailsInitialize: ")
        observePodcastShowData()
        //observePodcastDetailsData()
    }

    private fun getPodcastDetailsInitialize() {
        Log.e("PDF", "getPodcastDetailsInitialize: ")
        observePodcastDetailsData()

    }

    private fun setupAdapters() {
        parentRecycler = requireView().findViewById(R.id.recyclerView)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder().apply { setIsolateViewTypes(false) }.build()
        podcastHeaderAdapter =
            PodcastHeaderAdapter(this, cacheRepository, favViewModel, argHomePatchDetail)
        podcastTrackAdapter = PodcastTrackAdapter(this, this, cacheRepository!!)
        podcastMoreEpisodesAdapter = PodcastMoreEpisodesAdapter(data, this)
        podcastCommentsHeaderAdapter = PodcastCommentsHeaderAdapter()
        footerAdapter = HomeFooterAdapter()
//        artistsYouMightLikeAdapter =
//            ArtistsYouMightLikeAdapter(argHomePatchItem, this, argHomePatchDetail?.ArtistId)
        concatAdapter = ConcatAdapter(
            config,
            podcastHeaderAdapter,
            PodcastTrackHeaderAdapter(),
            podcastTrackAdapter,
            podcastMoreEpisodesAdapter,
            footerAdapter
        )
        // concatAdapter.notifyDataSetChanged()
        parentRecycler.layoutManager = layoutManager
        parentRecycler.adapter = concatAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            injector.podcastViewModelFactory
        )[PodcastViewModel::class.java]

        favViewModel = ViewModelProvider(
            this,
            injector.factoryFavContentVM
        )[FavViewModel::class.java]
        commentsViewModel = ViewModelProvider(this,injector.factoryCommentsVM)[CommentsViewModel::class.java]
       // commentsViewModel.getAllComments("2374","TC",1)
        Log.e("PDF", "getAllComments: " )
    }

    private fun observePodcastShowData() {

        viewModel.fetchPodcastShowContent(podcastType, contentType, false)
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        /*viewModel.podcastDetailsContent.observe(viewLifecycleOwner) { response ->
            if (response.status == Status.SUCCESS) {
                response?.data?.data?.EpisodeList?.let { itEpisod ->
//                    podcastHeaderAdapter.setHeader(
//                        itEpisod,
//                        itEpisod[0].TrackList
//                    )
                    podcastHeaderAdapter.setTrackData(
                        itEpisod,
                        itEpisod[0].TrackList,
                        argHomePatchDetail!!
                    )
                }
                response.data?.data?.EpisodeList?.get(0)?.let {
                    podcastTrackAdapter.setData(
                        it.TrackList,
                        argHomePatchDetail!!,
                        playerViewModel.currentMusic?.mediaId
                    )
                }
                response.data?.data?.let {
                    it.EpisodeList.let { it1 ->
                        podcastMoreEpisodesAdapter.setData(it1)
                    }
                }
//                parentRecycler.adapter = concatAdapter
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
            }
        }*/
    }

    private fun observePodcastDetailsData() {
        Log.i("PDF", "observePodcastDetailsData: " + selectedEpisodeID)
        viewModel.fetchPodcastContent(podcastType, selectedEpisodeID, contentType, false)

      //  commentsViewModel.getAllComments(selectedEpisodeID,podcastType,1)
    }

    override fun onClickItemAndAllItem(
        itemPosition: Int,
        selectedHomePatchItem: HomePatchItemModel
    ) {
        //  setAdapter(patch)
//        argHomePatchDetail = selectedHomePatchItem.Data[itemPosition]
//        artistHeaderAdapter.setData(argHomePatchDetail!!)
//        observeData()
//        artistsYouMightLikeAdapter.artistIDToSkip = argHomePatchDetail!!.ArtistId
//        parentAdapter.notifyDataSetChanged()
//        parentRecycler.scrollToPosition(0)
    }

    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel) {
    }

    override fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<EpisodeModel>) {
        val episode = selectedEpisode[itemPosition]
        selectedEpisodeID = episode.Id.toString()
        argHomePatchDetail?.content_Id = episode.Id.toString()

        observePodcastDetailsData()

        //podcastHeaderAdapter.setHeader(data)
        // podcastEpisodesAdapter.setData
//        artistHeaderAdapter.setData(argHomePatchDetail!!)
//        observeData()
//        artistsYouMightLikeAdapter.artistIDToSkip = argHomePatchDetail!!.ArtistId
//    parentAdapter.notifyDataSetChanged()
//        parentRecycler.scrollToPosition(0)
        // concatAdapter.notifyDataSetChanged()
        //  parentRecycler.scrollToPosition(0)
    }

    override fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        val lSongDetails = podcastTrackAdapter.tracks
        if (lSongDetails.size > clickItemPosition) {
            if (lSongDetails.size > clickItemPosition) {
                if ((lSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
                    playerViewModel.togglePlayPause()
                } else {
                    playItem(lSongDetails, clickItemPosition)
                }
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        Log.e(
            "PDF",
            "onClickItem: " + mSongDetails[clickItemPosition].rootContentId + " plRootId: " + playerViewModel.currentMusic?.rootId
        )
        if (playerViewModel.currentMusic != null) {
            if (mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId) {
                if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                    playerViewModel.skipToQueueItem(clickItemPosition)
                    playerViewModel.play()
                } else {
                    playerViewModel.togglePlayPause()
                }
            } else {
                playItem(mSongDetails, clickItemPosition)
            }
        } else {
            playItem(mSongDetails, clickItemPosition)
        }
    }

    override fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        songDetails: MutableList<IMusicModel>
    ) {
//        val mSongDet = podcastTrackAdapter.tracks
        val podcastHeaderVH = currentVH as PodcastHeaderAdapter.PodcastHeaderVH
        if (songDetails.size > 0 && isAdded) {
            //DO NOT USE requireActivity()
            playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMusic ->
                if (itMusic != null) {
                    if ((songDetails.indexOfFirst {
                            it.rootContentType == itMusic.rootType &&
                                    it.rootContentId == itMusic.rootId &&
                                    it.content_Id == itMusic.mediaId
                            /*     it.Id.toString() == itMusic.mediaId*/
                        } != -1)
                    ) {
                        playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
                            if (itPla != null)
                                playPauseStateRed(itPla.isPlaying, podcastHeaderVH.ivPlayBtn!!)
                        }
                    } else {
                        podcastHeaderVH.ivPlayBtn?.let { playPauseStateRed(false, it) }
                    }
                }
            }
        }
    }

    override fun onClickBottomItem(track: IMusicModel) {
        (activity as? SDKMainActivity)?.showBottomSheetDialogForPodcast(
            navController,
            context = requireContext(),
            track,
            argHomePatchItem,
            argHomePatchDetail
        )
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("ACTION")
        intentFilter.addAction("DELETED123")
        intentFilter.addAction("REMOVE")
        intentFilter.addAction("PROGRESS")
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(MyBroadcastReceiver(), intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(MyBroadcastReceiver())
    }

    private fun progressIndicatorUpdate(downloadingItems: List<DownloadingItem>) {
        downloadingItems.forEach {
            val progressIndicator: CircularProgressIndicator? =
                view?.findViewWithTag(it.contentId)
//                val downloaded: ImageView?= view?.findViewWithTag(200)
            progressIndicator?.visibility = View.VISIBLE
            progressIndicator?.progress = it.progress.toInt()
//            val isDownloaded =
//                cacheRepository?.isTrackDownloaded(it.contentId) ?: false
//            if(isDownloaded){
//                progressIndicator?.visibility = View.GONE
//                // downloaded?.visibility = VISIBLE
//            }
        }
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "ACTION" -> {
                    //val data = intent.getIntExtra("currentProgress",0)
                    val downloadingItems =
                        intent.getParcelableArrayListExtra<DownloadingItem>("downloading_items")
                    downloadingItems?.let {
                        progressIndicatorUpdate(it)
//                        Log.e("getDownloadManagerx",
//                            "habijabi: ${it.toString()} ")
                    }
                }
                "DELETED123" -> {
                    podcastTrackAdapter.notifyDataSetChanged()
                    Log.e("DELETED", "broadcast fired")
                }
                "REMOVE" -> {
                    podcastTrackAdapter.notifyDataSetChanged()
                    Log.e("DELETED", "broadcast fired")
                }
                "PROGRESS" -> {
                    podcastTrackAdapter.notifyDataSetChanged()
                    Log.e("PROGRESS", "broadcast fired")
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }
}
