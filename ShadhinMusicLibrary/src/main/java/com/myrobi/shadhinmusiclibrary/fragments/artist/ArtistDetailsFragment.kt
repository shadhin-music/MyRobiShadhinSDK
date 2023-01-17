package com.myrobi.shadhinmusiclibrary.fragments.artist

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.myrobi.shadhinmusiclibrary.adapter.ArtistAlbumsAdapter
import com.myrobi.shadhinmusiclibrary.adapter.ArtistHeaderAdapter
import com.myrobi.shadhinmusiclibrary.adapter.ArtistTrackAdapter
import com.myrobi.shadhinmusiclibrary.adapter.ArtistsYouMightLikeAdapter
import com.myrobi.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.myrobi.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.ArtistContentModel
import com.myrobi.shadhinmusiclibrary.data.model.DownloadingItem
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


import java.io.Serializable


internal class ArtistDetailsFragment : BaseFragment(),
    HomeCallBack,
    CommonPlayControlCallback,
    CommonBottomCallback {

    private lateinit var navController: NavController
    var artistContent: ArtistContentModel? = null
    private lateinit var viewModel: ArtistViewModel
    private lateinit var viewModelArtistBanner: ArtistBannerViewModel
    private lateinit var viewModelArtistSong: ArtistContentViewModel
    private lateinit var viewModelArtistAlbum: ArtistAlbumsViewModel
    private var cacheRepository: CacheRepository? = null
    private lateinit var parentAdapter: ConcatAdapter
    private lateinit var artistHeaderAdapter: ArtistHeaderAdapter
    private lateinit var artistTrackAdapter: ArtistTrackAdapter
    private lateinit var artistAlbumsAdapter: ArtistAlbumsAdapter
    private lateinit var artistsYouMightLikeAdapter: ArtistsYouMightLikeAdapter
    private lateinit var footerAdapter: HomeFooterAdapter
    private lateinit var parentRecycler: RecyclerView
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
        setupViewModel()

        cacheRepository = CacheRepository(requireContext())
        initialize()
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music?.mediaId != null) {
                music.mediaId.let {
                    if (it != null) {
                        artistTrackAdapter.setPlayingSong(it)
                    }
                }

            }
        }
    }

    private fun initialize() {
        setupAdapters()
        observeData()
    }

    private fun setupAdapters() {
        parentRecycler = requireView().findViewById(R.id.recyclerView)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder().apply { setIsolateViewTypes(false) }.build()
        artistHeaderAdapter = ArtistHeaderAdapter(argHomePatchDetail,
            this,
            cacheRepository,
            injector.sdkCallback
        )
        artistTrackAdapter = ArtistTrackAdapter(this, this, cacheRepository)
        artistAlbumsAdapter = ArtistAlbumsAdapter(argHomePatchItem, this)
        artistsYouMightLikeAdapter =
            ArtistsYouMightLikeAdapter(argHomePatchItem, this, argHomePatchDetail?.artist_Id)
        footerAdapter = HomeFooterAdapter()
        if (argHomePatchItem?.ContentType == "P") {
            parentAdapter = ConcatAdapter(
                config,
                artistHeaderAdapter,
                artistTrackAdapter,
                artistAlbumsAdapter,
                footerAdapter
            )
            parentAdapter.notifyDataSetChanged()
            parentRecycler.layoutManager = layoutManager
            parentRecycler.adapter = parentAdapter
        } else {
            parentAdapter = ConcatAdapter(
                config,
                artistHeaderAdapter,
                artistTrackAdapter,
                artistAlbumsAdapter,
                artistsYouMightLikeAdapter,
                footerAdapter
            )
            parentAdapter.notifyDataSetChanged()
            parentRecycler.layoutManager = layoutManager
        }
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.factoryArtistVM
            )[ArtistViewModel::class.java]

        viewModelArtistBanner = ViewModelProvider(
            this,
            injector.factoryArtistBannerVM
        )[ArtistBannerViewModel::class.java]

        viewModelArtistSong = ViewModelProvider(
            this,
            injector.factoryArtistSongVM
        )[ArtistContentViewModel::class.java]

        viewModelArtistAlbum = ViewModelProvider(
            this,
            injector.artistAlbumViewModelFactory
        )[ArtistAlbumsViewModel::class.java]

        favViewModel = ViewModelProvider(
            this,
            injector.factoryFavContentVM
        )[FavViewModel::class.java]
    }

    private fun observeData() {
        argHomePatchDetail?.let {
            viewModel.fetchArtistBioData(it.artistName ?: "")
        }
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        viewModel.artistBioContent.observe(viewLifecycleOwner) { response ->
            if (response.status == Status.SUCCESS) {
                artistHeaderAdapter.artistBio(response.data, favViewModel)
                progressBar.visibility = GONE
            } else {
                progressBar.visibility = GONE
            }
        }
        argHomePatchDetail.let {
            it?.artist_Id?.let { it1 ->
                it1.let { it2 -> viewModelArtistBanner.fetchArtistBannerData(it2) }
            }
            viewModelArtistBanner.artistBannerContent.observe(viewLifecycleOwner) { response ->
                if (response.status == Status.SUCCESS) {
                    progressBar.visibility = GONE
                    artistHeaderAdapter.artistBanner(response.data)
                } else {
                    progressBar.visibility = VISIBLE
                }
            }
        }
        argHomePatchDetail?.let { homeDetails ->
            viewModelArtistSong.fetchArtistSongData(homeDetails.artist_Id ?: "")
            viewModelArtistSong.artistSongContent.observe(viewLifecycleOwner) { res ->
                if (res.status == Status.SUCCESS) {
                    if (res.data?.data != null) {
                        homeDetails.artistName = res.data.name
                        homeDetails.imageUrl = res.data.image
                        artistTrackAdapter.setArtistTrack(
                            res.data.data,
                            homeDetails,
                            playerViewModel.currentMusic?.mediaId
                        )
                        artistHeaderAdapter.setSongAndData(res.data.data, homeDetails)
                        artistHeaderAdapter.setData(homeDetails)
                        viewModel.fetchArtistBioData(homeDetails.artistName?:"")

                    }
                } else {
                    showDialog()
                }
            }
        }
        argHomePatchDetail?.let {
            viewModelArtistAlbum.fetchArtistAlbum("r", it.artist_Id ?: "")
            viewModelArtistAlbum.artistAlbumContent.observe(viewLifecycleOwner) { res ->
                if (res.status == Status.SUCCESS) {
                    artistAlbumsAdapter.setData(res.data)
                } else {
                    showDialog()
                }
            }
        }
        parentRecycler.adapter = parentAdapter
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext()) //set icon
            .setIcon(android.R.drawable.ic_dialog_alert) //set title
            .setTitle("An error occurred") //set message
            .setMessage("Go back to previous page") //set positive button
            .setPositiveButton(
                "Okay"
            ) { _, _ ->
                requireActivity().finish()
            }.show()
    }

    override fun onClickItemAndAllItem(
        itemPosition: Int,
        selectedHomePatchItem: HomePatchItemModel,
    ) {
        //  setAdapter(patch)
        argHomePatchDetail = selectedHomePatchItem.Data[itemPosition]
        if (argHomePatchDetail?.content_Id.isNullOrEmpty()) {
            argHomePatchDetail?.content_Id = argHomePatchDetail?.artist_Id ?: ""
        }
         argHomePatchDetail?.let {
             artistHeaderAdapter.setData(it)
         }

        observeData()
        artistsYouMightLikeAdapter.artistIDToSkip = argHomePatchDetail?.artist_Id
        parentAdapter.notifyDataSetChanged()
        parentRecycler.scrollToPosition(0)
    }

    override fun onArtistAlbumClick(
        itemPosition: Int,
        artistAlbumModelData: List<ArtistAlbumModelData>,
    ) {
        val mArtAlbumMod = artistAlbumModelData[itemPosition]
        navController.navigate(R.id.to_album_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "", listOf(), "", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    UtilHelper.getHomePatchDetailToAlbumModel(mArtAlbumMod) as Serializable
                )
            })
    }

    override fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<EpisodeModel>) {

    }


    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel) {
//        observeData()
//        artistsYouMightLikeAdapter.artistIDToSkip = argHomePatchDetail!!.ArtistId
//        parentAdapter.notifyDataSetChanged()
//        parentRecycler.scrollToPosition(0)
    }

    override fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
//        val lSongDetails = artistTrackAdapter.artistSongList
        if (mSongDetails.size > clickItemPosition) {
            if (mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId) {
                playerViewModel.togglePlayPause()
            } else {
                playItem(
                    mSongDetails,
                    clickItemPosition
                )
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        Log.e(
            "CommArDF",
            "onClickItem: sRCId " + mSongDetails[clickItemPosition].rootContentId
                    + " pCMRId " + playerViewModel.currentMusic?.rootId
        )
        Log.e(
            "CommAlDF",
            "onClickItem: sCId " + mSongDetails[clickItemPosition].content_Id
                    + " pmId " + playerViewModel.currentMusic?.mediaId
        )
        if (playerViewModel.currentMusic != null) {
            if ((mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
                if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                    val songListSize = playerViewModel.musicList?.size
                    if (songListSize != mSongDetails.size) {
                        Log.e("CommAlDF", "onClickItem: $songListSize")
                        playItem(mSongDetails, clickItemPosition)
                    } else {
                        Log.e("CommAlDF", "skipToQueueItem: $clickItemPosition")
                        playerViewModel.skipToQueueItem(clickItemPosition)
                        playerViewModel.play()
                    }
                    //Todo change for album to artist(no have multiple item)
                } else {
                    playerViewModel.togglePlayPause()
                }
            } else {
                playItem(
                    mSongDetails,
                    clickItemPosition
                )
            }
        } else {
            playItem(
                mSongDetails,
                clickItemPosition
            )
        }
    }

    override fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        songDetails: MutableList<IMusicModel>,
    ) {
//        val mSongDet = artistTrackAdapter.artistSongList
        val artistHeaderVH = currentVH as ArtistHeaderAdapter.ArtistHeaderVH
        if (songDetails.size > 0 && isAdded) {
            //DO NOT USE requireActivity()
            playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMusic ->
                if (itMusic != null) {
                    if ((songDetails.indexOfFirst {
                            it.rootContentType == itMusic.rootType &&
                                    it.rootContentId == itMusic.rootId &&
                                    it.content_Id == itMusic.mediaId
                        } != -1)
                    ) {
                        playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
                            if (itPla != null)
                                artistHeaderVH.ivPlayBtn?.let {
                                    playPauseStateRed(
                                        itPla.isPlaying,
                                        it
                                    )
                                }
                        }
                    } else {
                        artistHeaderVH.ivPlayBtn?.let { playPauseStateRed(false, it) }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("ACTION")
        intentFilter.addAction("DELETED123")
        intentFilter.addAction("DELETED")
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
//            if(!isDownloaded){
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
                    }
                }
                "DELETED123" -> {
                    artistTrackAdapter.notifyDataSetChanged()
                }
                "DELETED" -> {
                    artistTrackAdapter.notifyDataSetChanged()
                }
                "PROGRESS" -> {
                    artistTrackAdapter.notifyDataSetChanged()
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
       /* startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })*/
    }

    override fun onClickBottomItem(mSongDetails: IMusicModel) {
        (activity as? SDKMainActivity)?.showBottomSheetDialogGoTOALBUM(
            navController,
            context = requireContext(),
            mSongDetails,
            argHomePatchItem,
            argHomePatchDetail
        )
    }





}