package com.myrobi.shadhinmusiclibrary.fragments.create_playlist

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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.myrobi.shadhinmusiclibrary.adapter.UserCreatedPlaylistHeaderAdapter
import com.myrobi.shadhinmusiclibrary.adapter.UserCreatedPlaylistTrackAdapter
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.DownloadingItem
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.download.MyBLDownloadService
import com.myrobi.shadhinmusiclibrary.download.room.DownloadedContent
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class UserCreatedPlaylistDetailsFragment : PlaylistBaseFragment(),
    CommonPlayControlCallback,
    CommonBottomCallback {

    private lateinit var viewModel: CreateplaylistViewModel
    private lateinit var navController: NavController
    private lateinit var cacheRepository: CacheRepository
    private lateinit var favViewModel: FavViewModel

    private lateinit var playlistHeaderAdapter: UserCreatedPlaylistHeaderAdapter
    private lateinit var playlistTrackAdapter: UserCreatedPlaylistTrackAdapter
    private lateinit var footerAdapter: HomeFooterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRef =
            inflater.inflate(R.layout.my_bl_sdk_fragment_user_playlist_details, container, false)
        navController = findNavController()

        return viewRef
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.factoryCreatePlaylistVM
            )[CreateplaylistViewModel::class.java]
    }

    private fun setupFavViewModel() {
        favViewModel =
            ViewModelProvider(
                this,
                injector.factoryFavContentVM
            )[FavViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFavViewModel()
        cacheRepository = CacheRepository(requireContext())
        playlistHeaderAdapter = UserCreatedPlaylistHeaderAdapter(
            argHomePatchDetail,
            playlistName,
            this,
            cacheRepository,
            favViewModel,
            gradientDrawable!!
        )
        playlistTrackAdapter = UserCreatedPlaylistTrackAdapter(this, this, cacheRepository)
        footerAdapter = HomeFooterAdapter()
        setupViewModel()
        ///read data from online
        playlistId?.let { fetchOnlineData(it, playlistName) }

//        adapter.setRootData(argHomePatchDetail!!)
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
//            footerAdapter
        )
        recyclerView.adapter = concatAdapter
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music != null) {
                if (music.mediaId != null) {
                    playlistTrackAdapter.setPlayingSong(music.mediaId!!)
                }
            }
        }
    }

    private fun fetchOnlineData(contentId: String, playlistName: String?) {
        viewModel.getuserSongsInPlaylist(contentId)
        viewModel.getUserSongsPlaylist.observe(viewLifecycleOwner) { res ->
            if (res.data != null) {
                playlistTrackAdapter.setData(
                    res.data?.toMutableList()!!,
                    contentId,
                    playerViewModel.currentMusic?.mediaId
                )
                playlistHeaderAdapter.setSongAndData(
                    res.data?.toMutableList()!!, contentId
                )
//                updateAndSetAdapter(res!!.data!!.data)
            } else {
//                updateAndSetAdapter(mutableListOf())
            }

            val imageView: AppCompatImageView = requireView().findViewById(R.id.imgMore)
            imageView.setOnClickListener {
                showBottomSheetDialogDeletePlaylist(
                    navController,
                    context = requireContext(),
                    SongDetailModel()
                        .apply {
                            content_Id = contentId
                            titleName = playlistName.toString()
                            album_Id = playlistId
                        },
                    argHomePatchItem,
                    argHomePatchDetail
                )
            }
        }
    }

    override fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        val lSongDetails = playlistTrackAdapter.dataSongDetail
        if (lSongDetails.size > clickItemPosition) {
            if ((lSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
                playerViewModel.togglePlayPause()
            } else {
                playItem(lSongDetails, clickItemPosition)
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
        val playlistHeaderVH =
            currentVH as UserCreatedPlaylistHeaderAdapter.UserCreatedPlaylistHeaderVH
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
                            playPauseStateRed(itPla!!.isPlaying, playlistHeaderVH.ivPlayBtn!!)
                        }
                    } else {
                        playlistHeaderVH.ivPlayBtn?.let { playPauseStateRed(false, it) }
                    }
                }
            }
        }
    }

    override fun onClickBottomItem(mSongDetails: IMusicModel) {
        showBottomSheetDialogForPlaylist(
            navController,
            context = requireContext(),
            mSongDetails,
            argHomePatchItem,
            argHomePatchDetail
        )
    }

    private fun showBottomSheetDialogDeletePlaylist(
        bsdNavController: NavController,
        context: Context,
        mSongDetails: SongDetailModel,
        argHomePatchItem: HomePatchItemModel?,
        argHomePatchDetail: HomePatchDetailModel?,
    ) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val contentView =
            View.inflate(context, R.layout.my_bl_sdk_bottomsheet_three_dot_menu_layout, null)
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
        val url = mSongDetails.imageUrl
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = mSongDetails.titleName
        val artistname = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        artistname?.text = mSongDetails.artistName
//        if (image != null) {
//            Glide.with(context)?.load(url?.replace("<\$size\$>", "300"))?.into(image)
//        }
        gradientDrawable?.let { image?.setImageResource(it) }

        image?.setBackgroundResource(R.drawable.my_bl_sdk_playlist_bg)
//        image?.let {
//            Glide.with(requireContext()).load(R.drawable.my_bl_sdk_playlist_bg).centerCrop().into(it)
//        }
//        image?.setImageResource(R.drawable.my_bl_sdk_playlist_bg)
        val constraintAlbum: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAlbum)
        constraintAlbum?.visibility = GONE
//            constraintAlbum?.setOnClickListener {
//                gotoArtistFromPlaylist(
//                    bsdNavController,
//                    context,
//                    mSongDetails,
//                    argHomePatchItem,
//                    argHomePatchDetail
//
//                )
//                bottomSheetDialog.dismiss()
//            }
        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)
        var isDownloaded = false
        var downloaded = cacheRepository.getDownloadById(mSongDetails.content_Id ?: "")
        if (downloaded?.playingUrl != null) {
            isDownloaded = true
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_ic_delete)
        } else {
            isDownloaded = false
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_icon_dowload)
        }

        if (isDownloaded) {
            textViewDownloadTitle?.text = "Remove From Download"
        } else {
            textViewDownloadTitle?.text = "Download Offline"
        }

        val constraintDownload: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintDownload)
        constraintDownload?.visibility = GONE
        constraintDownload?.setOnClickListener {
            if (isDownloaded.equals(true)) {
                cacheRepository.deleteDownloadById(mSongDetails.content_Id ?: "")
                DownloadService.sendRemoveDownload(
                    context,
                    MyBLDownloadService::class.java, mSongDetails.content_Id ?: "", false
                )
                val localBroadcastManager = LocalBroadcastManager.getInstance(context)
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", mSongDetails.content_Id)
                localBroadcastManager.sendBroadcast(localIntent)

            } else {
                val url = "${Constants.FILE_BASE_URL}${mSongDetails.playingUrl}"
                val downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(mSongDetails.content_Id ?: "", url.toUri())
                        .build()
                DownloadService.sendAddDownload(
                    context,
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )

                if (cacheRepository.isDownloadCompleted(mSongDetails.content_Id ?: "")) {
                    cacheRepository.insertDownload(
                        DownloadedContent().apply {
                            content_Id = mSongDetails.content_Id.toString()
                            rootContentId = mSongDetails.rootContentId
                            imageUrl = mSongDetails.imageUrl
                            titleName = mSongDetails.titleName
                            content_Type = mSongDetails.content_Type
                            playingUrl = mSongDetails.playingUrl
                            rootContentType = mSongDetails.content_Type
                            artistName = mSongDetails.artistName
                            artist_Id = mSongDetails.artist_Id
                            total_duration = mSongDetails.total_duration
                        }
                    )
                }
            }
            bottomSheetDialog.dismiss()
        }

        val constraintLayoutFav: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintFav)
        constraintLayoutFav?.visibility = GONE
        val constraintPlaylist: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoPlaylist)
        val playlisttext: TextView? = bottomSheetDialog.findViewById(R.id.tvPlaylist)
        playlisttext?.text = "Delete Playlist"
        constraintPlaylist?.setOnClickListener {
            deletePlayList2(context, playlistId.toString())
            Toast.makeText(context, "${mSongDetails.titleName} Removed Successfully", Toast.LENGTH_LONG).show()
            requireActivity().onBackPressed()
//          viewModel.deleteUserSongFromPlaylist.observe(viewLifecycleOwner){
//              res->
//              Log.e("TAG", "CLICKArtist: " + res.message)
//                   }
            bottomSheetDialog.dismiss()
        }
    }

    private fun showBottomSheetDialogForPlaylist(
        bsdNavController: NavController,
        context: Context,
        mSongDetails: IMusicModel,
        argHomePatchItem: HomePatchItemModel?,
        argHomePatchDetail: HomePatchDetailModel?,
    ) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)

        val contentView =
            View.inflate(context, R.layout.my_bl_sdk_bottomsheet_three_dot_menu_layout, null)
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = mSongDetails.titleName
        val tvArtistName = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        tvArtistName?.text = mSongDetails.artistName
        if (image != null) {
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(mSongDetails.imageUrl!!))
                .into(image)
        }
        val constraintAlbum: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAlbum)
        constraintAlbum?.visibility = GONE
//            constraintAlbum?.setOnClickListener {
//                gotoArtistFromPlaylist(
//                    bsdNavController,
//                    context,
//                    mSongDetails,
//                    argHomePatchItem,
//                    argHomePatchDetail
//                )
//                bottomSheetDialog.dismiss()
//            }
        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)
        var isDownloaded = false
        var downloaded = cacheRepository.getDownloadById(mSongDetails.content_Id ?: "")
        if (downloaded?.playingUrl != null) {
            isDownloaded = true
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_ic_delete)
        } else {
            isDownloaded = false
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_icon_dowload)
        }

        if (isDownloaded) {
            textViewDownloadTitle?.text = "Remove From Download"
        } else {
            textViewDownloadTitle?.text = "Download Offline"
        }

        val constraintDownload: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintDownload)
        constraintDownload?.visibility = GONE
        constraintDownload?.setOnClickListener {
            if (isDownloaded.equals(true)) {
                cacheRepository.deleteDownloadById(mSongDetails.content_Id ?: "")
                DownloadService.sendRemoveDownload(
                    context,
                    MyBLDownloadService::class.java, mSongDetails.content_Id ?: "", false
                )
                val localBroadcastManager = LocalBroadcastManager.getInstance(context)
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", mSongDetails.content_Id)
                localBroadcastManager.sendBroadcast(localIntent)
                localBroadcastManager.sendBroadcast(localIntent)
            } else {
                val url = "${Constants.FILE_BASE_URL}${mSongDetails.playingUrl}"
                val downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(mSongDetails.content_Id ?: "", url.toUri()).build()
                DownloadService.sendAddDownload(
                    context,
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )

                if (cacheRepository.isDownloadCompleted(mSongDetails.content_Id ?: "")) {
                    cacheRepository.insertDownload(
                        DownloadedContent().apply {
                            content_Id = mSongDetails.content_Id.toString()
                            rootContentId = mSongDetails.rootContentId
                            imageUrl = mSongDetails.imageUrl
                            titleName = mSongDetails.titleName
                            content_Type = mSongDetails.content_Type
                            playingUrl = mSongDetails.playingUrl
                            rootContentType = mSongDetails.content_Type
                            artistName = mSongDetails.artistName
                            artist_Id = mSongDetails.artist_Id
                            total_duration = mSongDetails.total_duration
                        }
                    )
                }
            }
            bottomSheetDialog.dismiss()
        }

        val constraintPlaylist: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoPlaylist)
        val playlisttext: TextView? = bottomSheetDialog.findViewById(R.id.tvPlaylist)
        playlisttext?.text = "Delete from Playlist"
        constraintPlaylist?.setOnClickListener {
            deleteSongFromPlayList(context, mSongDetails, playlistId)

            Toast.makeText(context, "Removed Successfully", Toast.LENGTH_LONG).show()
            playlistTrackAdapter.notifyDataSetChanged()
            playlistId?.let { it1 -> viewModel.getuserSongsInPlaylist(it1) }
            bottomSheetDialog.dismiss()
        }

        val constraintFav: ConstraintLayout? = bottomSheetDialog.findViewById(R.id.constraintFav)
        constraintFav?.visibility = GONE
        val favImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgLike)
        val textFav: TextView? = bottomSheetDialog.findViewById(R.id.tvFav)
        var isFav = false
        val isAddedToFav = cacheRepository?.getFavoriteById(mSongDetails.content_Id ?: "")
        if (isAddedToFav?.content_Id != null) {
            favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
            isFav = true
            textFav?.text = "Remove From favorite"
        } else {
            favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
            isFav = false
            textFav?.text = "Favorite"
        }

        constraintFav?.setOnClickListener {
            if (isFav.equals(true)) {
                favViewModel.deleteFavContent(
                    mSongDetails.content_Id ?: "",
                    mSongDetails.content_Type ?: ""
                )
                cacheRepository.deleteFavoriteById(mSongDetails.content_Id ?: "")
                Toast.makeText(requireContext(), "Removed from favorite", Toast.LENGTH_LONG).show()
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
                isFav = false
            } else {
                favViewModel.addFavContent(
                    mSongDetails.content_Id ?: "",
                    mSongDetails.content_Type ?: ""
                )
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
                cacheRepository.insertFavSingleContent(
                    FavDataModel()
                        .apply {
                            content_Id = mSongDetails.content_Id
                            album_Id = mSongDetails.album_Id
                            albumImage = mSongDetails.imageUrl
                            artistName = mSongDetails.artistName
                            artist_Id = mSongDetails.artist_Id
                            clientValue = 2
                            content_Type = mSongDetails.content_Type
                            fav = "1"
                            imageUrl = mSongDetails.imageUrl
                            playingUrl = mSongDetails.playingUrl
                            rootContentId = mSongDetails.rootContentId
                            mSongDetails.rootContentType
                            titleName = mSongDetails.titleName
                        }
                )
                isFav = true
                Toast.makeText(requireContext(), "Added to favorite", Toast.LENGTH_LONG).show()
            }
            bottomSheetDialog.dismiss()
        }
    }

    private fun deleteSongFromPlayList(
        context: Context,
        mSongDetails: IMusicModel,
        playlistId: String?
    ) {
        viewModel.deleteuserSongfromPlaylist(playlistId.toString(), mSongDetails.content_Id ?: "")
    }


    private fun deletePlayList2(context: Context, playlistId: String?) {
        viewModel.deleteuserPlaylist(playlistId.toString())
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("ACTION")
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
            val downloaded: ImageView? = view?.findViewWithTag(220)
            progressIndicator?.visibility = View.VISIBLE
            progressIndicator?.progress = it.progress.toInt()
//            val isDownloaded =
//                cacheRepository?.isTrackDownloaded(it.contentId) ?: false
//            if(!isDownloaded){
//                progressIndicator?.visibility = View.GONE
//                downloaded?.visibility = View.GONE
//            }
        }
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "ACTION" -> {
                    val downloadingItems =
                        intent.getParcelableArrayListExtra<DownloadingItem>("downloading_items")
                    downloadingItems?.let {
                        progressIndicatorUpdate(it)
                    }
                }
                "DELETED" -> {
                    Log.e("DELETED", "broadcast fired")
                }
                "PROGRESS" -> {
                    Log.e("PROGRESS", "broadcast fired")
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }
}