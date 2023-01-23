package com.myrobi.shadhinmusiclibrary.fragments

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
import com.myrobi.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.myrobi.shadhinmusiclibrary.adapter.PlaylistHeaderAdapter
import com.myrobi.shadhinmusiclibrary.adapter.PlaylistTrackAdapter
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.DownloadingItem
import com.myrobi.shadhinmusiclibrary.fragments.album.AlbumViewModel
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.Status

internal class PlaylistDetailsFragment : BaseFragment(),
    CommonPlayControlCallback,
    CommonBottomCallback {

    private lateinit var navController: NavController
    private lateinit var albumViewModel: AlbumViewModel

    private lateinit var playlistHeaderAdapter: PlaylistHeaderAdapter
    private lateinit var playlistTrackAdapter: PlaylistTrackAdapter

    private lateinit var favViewModel: FavViewModel

    //    private lateinit var adapter: PlaylistAdapter
    private lateinit var footerAdapter: HomeFooterAdapter
    private var cacheRepository: CacheRepository? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRef =
            inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
        navController = findNavController()
        return viewRef
    }

    private fun setupViewModel() {
        albumViewModel = ViewModelProvider(
            this,
            injector.factoryAlbumVM
        )[AlbumViewModel::class.java]

        favViewModel =
            ViewModelProvider(
                this,
                injector.factoryFavContentVM
            )[FavViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()

        cacheRepository = CacheRepository(requireContext())
        // playlistTrackAdapter = PlaylistTrackAdapter(this, cacheRepository!!)
        playlistHeaderAdapter =
            PlaylistHeaderAdapter(argHomePatchDetail, this, cacheRepository, favViewModel)
        playlistTrackAdapter = PlaylistTrackAdapter(this, this, cacheRepository!!)
//        adapter = PlaylistAdapter(this, this)
        footerAdapter = HomeFooterAdapter()
        //read data from online
        fetchOnlineData(argHomePatchDetail!!.content_Id)
//      adapter.setRootData(argHomePatchDetail!!)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        val concatAdapter = ConcatAdapter(
            config,
            playlistHeaderAdapter,
            playlistTrackAdapter,
            /*adapter,*/
            footerAdapter
        )
        recyclerView.adapter = concatAdapter
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
                playlistTrackAdapter.setPlayingSong(music.mediaId!!)
            }
        }
    }

    private fun fetchOnlineData(contentId: String) {
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        argHomePatchDetail?.let { homeDetails ->
            albumViewModel.fetchPlaylistContent(contentId)
            albumViewModel.albumContent.observe(viewLifecycleOwner) { res ->
                if (res.data?.data != null && res.status == Status.SUCCESS) {

                    homeDetails.imageUrl = res.data.image
                    homeDetails.titleName= res.data.name
                    homeDetails.artistName =""

                    playlistTrackAdapter.setData(
                        res.data.data,
                        homeDetails,
                        playerViewModel.currentMusic?.mediaId
                    )
                    playlistHeaderAdapter.setSongAndData(
                        res.data.data,
                        homeDetails
                    )
//                updateAndSetAdapter(res!!.data!!.data)
                    progressBar.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
//                updateAndSetAdapter(mutableListOf())
                }
            }
        }
    }
//    private fun updateAndSetAdapter(songList: MutableList<SongDetailModel>) {
//        updatedSongList = mutableListOf()
//        for (songItem in songList) {
//            updatedSongList.add(
//                UtilHelper.getSongDetailAndRootData(
//                    songItem.apply { isSeekAble = true },
//                    argHomePatchDetail!!
//                )
//            )
//        }
////        adapter.setSongData(updatedSongList)
//    }

    override fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        val lSongDetails = playlistTrackAdapter.dataSongDetail
        if (lSongDetails.size > clickItemPosition) {
            if ((mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
                playerViewModel.togglePlayPause()
            } else {
                playItem(mSongDetails, clickItemPosition)
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        if (playerViewModel.currentMusic != null) {
            if ((mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
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
        val playlistHeaderVH = currentVH as PlaylistHeaderAdapter.PlaylistHeaderVH
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
                        //DO NOT USE requireActivity()
                        playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
                            playlistHeaderVH.ivPlayBtn?.let {
                                playPauseStateRed(itPla.isPlaying,
                                    it)
                            }
                        }
                    } else {
                        playlistHeaderVH.ivPlayBtn?.let { playPauseStateRed(false, it) }
                    }
                }
            }
        }
    }

    override fun onClickBottomItem(mSongDetails: IMusicModel) {
        (activity as? SDKMainActivity)?.showBottomSheetDialogForPlaylist(
            navController,
            context = requireContext(),
            mSongDetails,
            argHomePatchItem,
            argHomePatchDetail
        )
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("ACTION")
        intentFilter.addAction("DELETED123")
        intentFilter.addAction("DELETE")
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
//            if (!isDownloaded) {
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
                    playlistTrackAdapter.notifyDataSetChanged()
                    Log.e("DELETED", "broadcast fired")
                }
                "DELETE" -> {
                    playlistTrackAdapter.notifyDataSetChanged()
                    Log.e("DELETED", "broadcast fired")
                }
                "PROGRESS" -> {

                    playlistTrackAdapter.notifyDataSetChanged()
                    Log.e("PROGRESS", "broadcast fired")
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
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
}


internal interface DownloadOrDeleteActionSubscriber {
    fun notifyOnChange()
}