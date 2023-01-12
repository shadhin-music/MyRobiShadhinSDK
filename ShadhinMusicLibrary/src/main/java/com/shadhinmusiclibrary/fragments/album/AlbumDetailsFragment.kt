package com.shadhinmusiclibrary.fragments.album

import android.annotation.SuppressLint
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
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.AlbumHeaderAdapter
import com.shadhinmusiclibrary.adapter.AlbumsTrackAdapter
import com.shadhinmusiclibrary.adapter.ArtistAlbumsAdapter
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.DownloadingItem
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.shadhinmusiclibrary.fragments.artist.ArtistAlbumModelData
import com.shadhinmusiclibrary.fragments.artist.ArtistAlbumsViewModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.library.player.utils.isPlaying
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.Status

internal class AlbumDetailsFragment : BaseFragment(),
    CommonPlayControlCallback,
    CommonBottomCallback,
    HomeCallBack {

    private lateinit var albumViewModel: AlbumViewModel

    private var cacheRepository: CacheRepository? = null
    private lateinit var navController: NavController
    private lateinit var albumHeaderAdapter: AlbumHeaderAdapter
    private lateinit var albumsTrackAdapter: AlbumsTrackAdapter
    private lateinit var artistAlbumsAdapter: ArtistAlbumsAdapter
    private lateinit var footerAdapter: HomeFooterAdapter
    var artistAlbumModelData: ArtistAlbumModelData? = null

    private lateinit var favViewModel: FavViewModel

    private lateinit var viewModelArtistAlbum: ArtistAlbumsViewModel

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
        cacheRepository = CacheRepository(requireContext())
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
        albumHeaderAdapter = AlbumHeaderAdapter(argHomePatchDetail, this, injector.sdkCallback)
        albumsTrackAdapter = AlbumsTrackAdapter(this, this, cacheRepository)
        footerAdapter = HomeFooterAdapter()
        setupViewModel()
       Log.e("TAG","DATA: "+  argHomePatchDetail!!.content_Id)
        Log.e("TAG","DATA: "+  argHomePatchDetail!!.album_Id)
        observeData(
            argHomePatchDetail!!.content_Id ?: "",
            argHomePatchDetail!!.artist_Id ?: "",
            argHomePatchDetail!!.content_Type ?: ""
        )

        artistAlbumsAdapter = ArtistAlbumsAdapter(argHomePatchItem, this)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        val concatAdapter = ConcatAdapter(
            config,
            albumHeaderAdapter,
            albumsTrackAdapter,
            artistAlbumsAdapter,
            footerAdapter
        )
        recyclerView.adapter = concatAdapter
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music != null) {
                if (music.mediaId != null) {
                    albumsTrackAdapter.setPlayingSong(music.mediaId!!)
                }
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

    private fun setupViewModel() {
        albumViewModel = ViewModelProvider(
            this,
            injector.factoryAlbumVM
        )[AlbumViewModel::class.java]

        viewModelArtistAlbum = ViewModelProvider(
            this,
            injector.artistAlbumViewModelFactory
        )[ArtistAlbumsViewModel::class.java]

        favViewModel =
            ViewModelProvider(
                this,
                injector.factoryFavContentVM
            )[FavViewModel::class.java]
    }

    private fun observeData(contentId: String, artistId: String, contentType: String) {
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        argHomePatchDetail?.let { homeDetails ->


            if(homeDetails.content_Type.equals("S",true)) {
                albumViewModel.fetchSingleContent(homeDetails.content_Id)
            }else{
                albumViewModel.fetchAlbumContent(contentId)
            }


            albumViewModel.albumContent.observe(viewLifecycleOwner) { res ->
                if (res.status == Status.SUCCESS) {
                    progressBar.visibility = GONE
                    if (res.data?.data != null && argHomePatchDetail != null) {

                        homeDetails.imageUrl = res.data.albumImage
                        homeDetails.album_Name = res.data.albumName
                        homeDetails.artistName = res.data.artistName
                        homeDetails.artist_Id = res.data.artistId
                        homeDetails.titleName = kotlin.runCatching { res.data.data.first().titleName }.getOrNull()?:""

                        albumsTrackAdapter.setData(
                            res.data.data,
                            homeDetails,
                            playerViewModel.currentMusic?.mediaId
                        )
                        albumHeaderAdapter.setSongAndData(
                            res.data.data,
                            homeDetails,
                            cacheRepository!!,
                            favViewModel
                        )
                       // albumHeaderAdapter.setData(homeDetails)
                        if(res.data.artistId !=null) {
                            viewModelArtistAlbum.fetchArtistAlbum("r", res.data.artistId)
                        }
                    }

                } else {
                    progressBar.visibility = VISIBLE
                }
            }
        }

            viewModelArtistAlbum.fetchArtistAlbum("r", artistId)
            viewModelArtistAlbum.artistAlbumContent.observe(viewLifecycleOwner) { res ->
                if (res.status == Status.SUCCESS) {
                    artistAlbumsAdapter.setData(res.data)
                } else {
                    viewModelArtistAlbum.fetchArtistAlbum("r", artistId)
                }
            }

    }

    override fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        val lSongDetails = albumsTrackAdapter.dataSongDetail
        if (lSongDetails.size > clickItemPosition) {
            if ((lSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
                playerViewModel.togglePlayPause()
            } else {
                playItem(lSongDetails, clickItemPosition)
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        Log.e(
            "CommAlDF",
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
                        playItem(mSongDetails, clickItemPosition)
                    } else {
                        playerViewModel.skipToQueueItem(clickItemPosition)
                        playerViewModel.play()
                    }
                    //Todo change for album to artist(no have multiple item)
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

    override fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        songDetails: MutableList<IMusicModel>
    ) {
//        val mSongDet = albumsTrackAdapter.dataSongDetail
        val albumHeaderVH = currentVH as AlbumHeaderAdapter.HeaderViewHolder
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
                                albumHeaderVH.ivPlayBtn?.let {
                                    playPauseStateRed(
                                        itPla.isPlaying,
                                        it
                                    )
                                }
                        }
                    } else {
                        albumHeaderVH.ivPlayBtn?.let { playPauseStateRed(false, it) }
                    }
                }
            }
        }
    }

    override fun onClickItemAndAllItem(
        itemPosition: Int,
        selectedHomePatchItem: HomePatchItemModel
    ) {
//       val  argHomePatchDetail = selectedHomePatchItem.Data[itemPosition]
//        albumHeaderAdapter.setData(argHomePatchDetail)
//        observeData(argHomePatchDetail.ContentID.toInt(),argHomePatchDetail.ArtistId.toInt(),"r")
    }

    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel) {

    }

    override fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<EpisodeModel>) {

    }


    override fun onArtistAlbumClick(
        itemPosition: Int,
        artistAlbumModelData: List<ArtistAlbumModelData>
    ) {
        val mArtAlbumMod = artistAlbumModelData[itemPosition]
        val data = HomePatchDetailModel().apply {
            album_Id = mArtAlbumMod.album_Id ?: ""
            artist_Id = mArtAlbumMod.artist_Id ?: ""
            content_Id = mArtAlbumMod.content_Id ?: ""
            content_Type = mArtAlbumMod.content_Type ?: ""
            playingUrl = mArtAlbumMod.playingUrl ?: ""
            titleName = mArtAlbumMod.titleName ?: ""
            fav = mArtAlbumMod.fav ?: ""
            total_duration = mArtAlbumMod.total_duration ?: ""
            imageUrl = mArtAlbumMod.imageUrl
            artistName = mArtAlbumMod.artistName ?: ""
            imageWeb = ""
            isSeekAble = true
            titleName = mArtAlbumMod.titleName ?: ""
        }
        argHomePatchDetail = data
        albumHeaderAdapter.setData(data)
        observeData(mArtAlbumMod.content_Id ?: "", mArtAlbumMod.artist_Id ?: "", "r")
    }

    override fun onClickBottomItem(mSongDetails: IMusicModel) {
        (activity as? SDKMainActivity)?.showBottomSheetDialog(
            navController,
            context = requireContext(),
            mSongDetails,
            argHomePatchItem,
            argHomePatchDetail
        )
    }

    private fun progressIndicatorUpdate(downloadingItems: List<DownloadingItem>) {
        downloadingItems.forEach {
            val progressIndicator: CircularProgressIndicator? =
                view?.findViewWithTag(it.contentId)
            //  val downloaded: ImageView?= view?.findViewWithTag(200)
            progressIndicator?.visibility = VISIBLE
            progressIndicator?.progress = it.progress.toInt()

//            val isDownloaded =
//                cacheRepository?.isTrackDownloaded(it.contentId) ?: false
//            if (!isDownloaded) {
//                progressIndicator?.visibility = GONE
//                    downloaded?.visibility = VISIBLE
//            }
        }
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
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
                    albumsTrackAdapter.notifyDataSetChanged()
                }
                "DELETED" -> {
                    albumsTrackAdapter.notifyDataSetChanged()
                }
                "PROGRESS" -> {
                    albumsTrackAdapter.notifyDataSetChanged()
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }

}