package com.shadhinmusiclibrary.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.ItemClickListener
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.AllDownloadedAdapter
import com.shadhinmusiclibrary.adapter.CreatePlaylistListAdapter
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.*
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.data.model.podcast.SongTrackModel
import com.shadhinmusiclibrary.download.MyBLDownloadService
import com.shadhinmusiclibrary.download.room.DownloadedContent
import com.shadhinmusiclibrary.download.room.WatchLaterContent
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.fragments.create_playlist.CreateplaylistViewModel
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.Constants
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable

internal class AllDownloadDetailsFragment : BaseFragment(),
    DownloadedSongOnCallBack,
    CommonPSVCallback,
    ItemClickListener {

    private lateinit var viewModel: CreateplaylistViewModel
    private lateinit var favViewModel: FavViewModel
    private var isDownloaded: Boolean = false
    private var iswatched: Boolean = false
    private lateinit var navController: NavController
    private lateinit var allDownloadAdapter: AllDownloadedAdapter
    private lateinit var parentAdapter: ConcatAdapter
    private lateinit var footerAdapter: HomeFooterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        navController = findNavController()
        return inflater.inflate(R.layout.my_bl_sdk_fragment_download_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allDownloadAdapter = AllDownloadedAdapter(this, this)

        favViewModel = ViewModelProvider(
            this,
            injector.factoryFavContentVM
        )[FavViewModel::class.java]

        viewModel = ViewModelProvider(
            this,
            injector.factoryCreatePlaylistVM
        )[CreateplaylistViewModel::class.java]

        loadData()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun loadData() {
        val cacheRepository = CacheRepository(requireContext())
        allDownloadAdapter.setData(
            cacheRepository.getAllDownloads()?.toMutableList() ?: mutableListOf(),
            argHomePatchDetail ?: HomePatchDetailModel(),
            playerViewModel.currentMusic?.mediaId
        )

        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder().apply { setIsolateViewTypes(false) }.build()
        footerAdapter = HomeFooterAdapter()
        parentAdapter = ConcatAdapter(config, allDownloadAdapter)
        recyclerView.adapter = parentAdapter

        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music != null) {
                if (music.mediaId != null) {
                    allDownloadAdapter.setPlayingSong(music.mediaId!!)
                }
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        if (playerViewModel.currentMusic != null && (mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
            if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                playerViewModel.skipToQueueItem(clickItemPosition)
            } else {
                playerViewModel.togglePlayPause()
            }
        } else {
            playItem(
                mSongDetails,
                clickItemPosition
            )
        }
    }

    //TODO have to re test those codes
    override fun onClickFavItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {

    }

    override fun onFavAlbumClick(itemPosition: Int, mSongDetails: MutableList<IMusicModel>) {

    }

    override fun onClickBottomItemPodcast(mSongDetails: IMusicModel) {
        (activity as? SDKMainActivity)?.showBottomSheetDialogForPodcast(
            navController,
            context = requireContext(),
            SongTrackModel().apply {
                rootContentType = mSongDetails.rootContentType
                content_Type = "PD"
                artistName = mSongDetails.artistName
                total_duration = mSongDetails.total_duration
                content_Id = mSongDetails.content_Id
                imageUrl = mSongDetails.imageUrl
                titleName = mSongDetails.titleName
                playingUrl = mSongDetails.playingUrl
                rootContentId = mSongDetails.rootContentId
                rootImage = mSongDetails.imageUrl
            },
            argHomePatchItem,
            argHomePatchDetail
        )
    }

    override fun onClickBottomItemSongs(mSongDetails: IMusicModel) {
        showBottomSheetDialog(
            navController,
            context = requireContext(),
            mSongDetails,
            argHomePatchItem,
            HomePatchDetailModel().apply {
                artistName = mSongDetails.artistName ?: ""
                artist_Id = mSongDetails.artist_Id ?: ""
                content_Id = mSongDetails.content_Id ?: ""
                rootContentType = mSongDetails.rootContentType ?: ""
                playingUrl = mSongDetails.playingUrl ?: ""
                imageUrl = mSongDetails.imageUrl ?: ""
                titleName = mSongDetails.titleName ?: ""
                total_duration = mSongDetails.total_duration
                album_Id = mSongDetails.album_Id
                content_Type = mSongDetails.content_Type
            }
        )
    }

    override fun onClickBottomItemVideo(mSongDetails: IMusicModel) {
        openDialog(
            VideoModel(
                "",
                "",
                mSongDetails.titleName,
                mSongDetails.artistName,
                "",
                "",
                "",
                2,
                mSongDetails.content_Id,
                mSongDetails.rootContentType,
                "",
                mSongDetails.total_duration,
                "",
                "",
                mSongDetails.imageUrl,
                "",
                false,
                "",
                0,
                "",
                "",
                "",
                mSongDetails.playingUrl,
                "",
                "",
                false,
                "",
                mSongDetails.titleName,
                "",
                ""
            )
        )
    }

    private fun openDialog(item: VideoModel) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val cacheRepository = CacheRepository(requireContext())
        val contentView =
            View.inflate(
                requireContext(),
                R.layout.my_bl_sdk_video_bottomsheet_three_dot_menu,
                null
            )
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val artistname = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        artistname?.text = item.artist
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
//        val url = item.image
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = item.title
        if (image != null) {
            Glide.with(this)
                .load(UtilHelper.getImageUrlSize300(item.image ?: ""))
                .into(image)
        }

        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)

        var downloaded = cacheRepository.getDownloadById(item.contentID.toString())
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
        constraintDownload?.setOnClickListener {
            if (isDownloaded.equals(true)) {
                cacheRepository.deleteDownloadById(item.contentID.toString())
                DownloadService.sendRemoveDownload(
                    requireContext(),
                    MyBLDownloadService::class.java, item.contentID.toString(), false
                )

                val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", item.contentID.toString())
                localBroadcastManager.sendBroadcast(localIntent)
                isDownloaded = false
            } else {
                val url = "${Constants.FILE_BASE_URL}${item.playUrl}"
                val downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(item.contentID.toString(), url.toUri())
                        .build()
                injector.downloadTitleMap[item.contentID.toString()] = item.title.toString()
                DownloadService.sendAddDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )

                if (cacheRepository.isDownloadCompleted(item.contentID.toString()).equals(true)) {
                    cacheRepository.insertDownload(
                        DownloadedContent().apply {
                            content_Id = item.contentID.toString()
                            rootContentId = item.rootId.toString()
                            imageUrl = item.image.toString()
                            titleName = item.title.toString()
                            content_Type = item.contentType.toString()
                            playingUrl = item.playUrl
                            rootContentType = item.contentType.toString()
                            artistName = item.artist.toString()
                            artist_Id = item.artistId.toString()
                            total_duration = item.duration.toString()

                        }
                    )
                    isDownloaded = true
                }
            }
            bottomSheetDialog.dismiss()
        }
        val watchlaterImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgWatchlater)
        val textViewWatchlaterTitle: TextView? = bottomSheetDialog.findViewById(R.id.txtwatchLater)

        var watched = cacheRepository.getWatchedVideoById(item.contentID.toString())

        if (watched?.isWatched == 1) {
            iswatched = true
            watchlaterImage?.setImageResource(R.drawable.my_bl_sdk_watch_later_remove)
//            watchIcon.setColorFilter(applicationContext.getResources().getColor(R.color.my_sdk_color_primary))

        } else {
            iswatched = false
            watchlaterImage?.setImageResource(R.drawable.my_bl_sdk_ic_watch_later)
//            watchIcon.setColorFilter(applicationContext.getResources().getColor(R.color.my_sdk_down_item_desc))
        }

        if (iswatched) {
            textViewWatchlaterTitle?.text = "Remove From Watchlater"
        } else {
            textViewWatchlaterTitle?.text = "Watch Later"
        }
        val constraintWatchlater: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoWatch)
        constraintWatchlater?.setOnClickListener {
            if (iswatched) {
                cacheRepository.deleteWatchlaterById(item.contentID.toString())
                iswatched = false
            } else {
                val url = "${Constants.FILE_BASE_URL}${item.playUrl}"
                cacheRepository.insertWatchLater(
                    WatchLaterContent(
                        item.contentID.toString(),
                        item.rootId.toString(),
                        item.image.toString(),
                        item.title.toString(),
                        item.contentType.toString(),
                        url,
                        item.contentType.toString(),
                        0,
                        0,
                        1,
                        item.artist.toString(),
                        item.duration.toString()
                    )
                )
                iswatched = true
            }
            bottomSheetDialog.dismiss()
        }
    }

    fun showBottomSheetDialog(
        bsdNavController: NavController,
        context: Context,
        mSongDetails: IMusicModel,
        argHomePatchItem: HomePatchItemModel?,
        argHomePatchDetail: HomePatchDetailModel?,
    ) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val cacheRepository = CacheRepository(requireContext())
        val contentView =
            View.inflate(context, R.layout.my_bl_sdk_bottomsheet_three_dot_menu_layout, null)
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val imageArtist: ImageView? = bottomSheetDialog.findViewById(R.id.imgAlbum)
        val textAlbum: TextView? = bottomSheetDialog.findViewById(R.id.tvAlbums)
        textAlbum?.text = "Go to Artist"
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
        val url = argHomePatchDetail?.imageUrl.toString()
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = argHomePatchDetail?.titleName
        val artistname = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        artistname?.text = mSongDetails.artistName
        if (image != null) {
            Glide.with(context).load(UtilHelper.getImageUrlSize300(url)).into(image)
        }
        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)
        var isDownloadedComplete = false
        var downloaded = cacheRepository.getDownloadById(mSongDetails.content_Id ?: "")
        if (downloaded?.playingUrl != null) {
            isDownloadedComplete = true
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_ic_delete)
        } else {
            isDownloadedComplete = false
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_icon_dowload)
        }

        if (isDownloadedComplete) {
            textViewDownloadTitle?.text = "Remove From Download"
        } else {
            textViewDownloadTitle?.text = "Download Offline"
        }
        val constraintDownload: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintDownload)
        constraintDownload?.setOnClickListener {
            if (isDownloadedComplete.equals(true)) {
                cacheRepository.deleteDownloadById(mSongDetails.content_Id ?: "")
                DownloadService.sendRemoveDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    mSongDetails.content_Id ?: "",
                    false
                )
                val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
                val localIntent = Intent("DELETE")
                    .putExtra("contentID", mSongDetails.content_Id)
                localBroadcastManager.sendBroadcast(localIntent)
                isDownloadedComplete = false
                allDownloadAdapter.upDateData(cacheRepository.getAllDownloads()?.toMutableList())
            } else {
                val url = "${Constants.FILE_BASE_URL}${mSongDetails.playingUrl}"
                var downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(mSongDetails.content_Id ?: "", url.toUri())
                        .build()
                DownloadService.sendAddDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )
                if (cacheRepository.isDownloadCompleted(mSongDetails.content_Id ?: "")) {
//                if (cacheRepository.isDownloadCompleted(mSongDetails.ContentID).equals(true)) {
                    cacheRepository.insertDownload(
                        DownloadedContent()
                            .apply {
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
                                album_Id = mSongDetails.album_Id
                            }
                    )
                    isDownloadedComplete = true
                }
            }
            bottomSheetDialog.dismiss()
        }
        val constraintAlbum: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAlbum)
        constraintAlbum?.setOnClickListener {
            gotoArtist(
                bsdNavController,
                context,
                mSongDetails,
                argHomePatchItem,
                argHomePatchDetail
            )
            bottomSheetDialog.dismiss()
        }
        val constraintPlaylist: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoPlaylist)
        constraintPlaylist?.setOnClickListener {
            if (isNetworkAvailable(requireContext()) == true) {
                gotoPlayList(context, mSongDetails)
            } else {
                Toast.makeText(requireContext(), "Please check network", Toast.LENGTH_LONG).show()
            }

            bottomSheetDialog.dismiss()
        }

        val constraintFav: ConstraintLayout? = bottomSheetDialog.findViewById(R.id.constraintFav)
        val favImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgLike)
        val textFav: TextView? = bottomSheetDialog.findViewById(R.id.tvFav)
        var isFav = false
//        favViewModel.getFavContentSong("S")
//        favViewModel.getFavContentSong.observe(this){res->
//            res.data?.forEach{
//                if(it.contentID.equals(mSongDetails.ContentID)) {
//
//                    isFav = true
//                    favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
//                }
//                else {
//                    isFav = false
//                 favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
//                }
//            }
//        }
        val isAddedToFav = cacheRepository.getFavoriteById(mSongDetails.content_Id ?: "")
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
                            clientValue = 2
                            content_Type = mSongDetails.content_Type
                            fav = "1"
                            imageUrl = mSongDetails.imageUrl
                            playingUrl = mSongDetails.playingUrl
                            rootContentId = mSongDetails.rootContentId
                            rootContentType = mSongDetails.rootContentType
                            titleName = mSongDetails.titleName
                        }
                )
                isFav = true
                Toast.makeText(requireContext(), "Added to favorite", Toast.LENGTH_LONG).show()
            }
            bottomSheetDialog.dismiss()
        }
    }


    private fun gotoArtist(
        bsdNavController: NavController,
        context: Context,
        mSongDetails: IMusicModel,
        argHomePatchItem: HomePatchItemModel?,
        argHomePatchDetail: HomePatchDetailModel?
    ) {
        //  Log.e("Check", ""+bsdNavController.graph.displayName)
        bsdNavController.navigate(R.id.to_artist_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "A", mutableListOf(), "Artist", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    HomePatchDetailModel().apply {
                        content_Id = mSongDetails.content_Id ?: ""
                        album_Id = mSongDetails.album_Id.toString()
                        artist_Id = mSongDetails.artist_Id.toString()
                        artistName = mSongDetails.artistName ?: ""
                        content_Type = mSongDetails.content_Type ?: ""
                        playingUrl = mSongDetails.playingUrl ?: ""
                        imageUrl = mSongDetails.imageUrl.toString()
                        titleName = mSongDetails.titleName.toString()
                    } as Serializable
                )
            })
    }

    private fun gotoPlayList(context: Context, mSongDetails: IMusicModel) {
        val bottomSheetDialogPlaylist = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val contentView =
            View.inflate(context, R.layout.my_bl_sdk_bottomsheet_create_playlist_with_list, null)
        bottomSheetDialogPlaylist.setContentView(contentView)
        bottomSheetDialogPlaylist.show()
        val closeButton: ImageView? = bottomSheetDialogPlaylist.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialogPlaylist.dismiss()
        }
        val recyclerView: RecyclerView? = bottomSheetDialogPlaylist.findViewById(R.id.recyclerView)
        viewModel.getuserPlaylist()
        viewModel.getUserPlaylist.observe(this) { res ->
            recyclerView?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            if (res != null) {
                if (res.data != null) {
                    recyclerView?.adapter = res.data?.let {
                        CreatePlaylistListAdapter(it, this, mSongDetails)
                    }
                }
            }
        }
        val btnCreateplaylist: AppCompatButton? =
            bottomSheetDialogPlaylist.findViewById(R.id.btnCreatePlaylist)
        btnCreateplaylist?.setOnClickListener {
//            if(isNetworkAvailable(requireContext()).equals(true)){
            openCreatePlaylist(context)
            bottomSheetDialogPlaylist.dismiss()
        }
        viewModel.createPlaylist.observe(this) { res ->

            Toast.makeText(context, res.status.toString(), Toast.LENGTH_LONG).show()
        }
    }


    fun openCreatePlaylist(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val contentView =
            View.inflate(context, R.layout.my_bl_sdk_bottomsheet_create_new_playlist, null)
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val etCreatePlaylist: EditText? = bottomSheetDialog.findViewById(R.id.etCreatePlaylist)
        val savePlaylist: AppCompatButton? = bottomSheetDialog.findViewById(R.id.btnSavePlaylist)
        etCreatePlaylist?.setOnFocusChangeListener { view, focused ->
            val keyboard: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (focused) keyboard.showSoftInput(
                etCreatePlaylist,
                0
            ) else keyboard.hideSoftInputFromWindow(
                etCreatePlaylist.getWindowToken(),
                0
            )
        }
        etCreatePlaylist?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name: String = etCreatePlaylist.text.toString()
                savePlaylist?.setBackgroundResource(R.drawable.my_bl_sdk_rounded_button_red)
                savePlaylist?.isEnabled = true
                savePlaylist?.setOnClickListener {
                    viewModel.createPlaylist(name)
                    // requireActivity().onBackPressed()
                    bottomSheetDialog.dismiss()
                }
                if (etCreatePlaylist.text.isNullOrEmpty()) {
                    savePlaylist?.setBackgroundResource(R.drawable.my_bl_sdk_rounded_button_gray)
                    savePlaylist?.isEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        etCreatePlaylist?.requestFocus()
    }

    override fun onClick(position: Int, mSongDetails: IMusicModel, id: String?) {
        addSongsToPlaylist(mSongDetails, id)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    private fun addSongsToPlaylist(mSongDetails: IMusicModel, id: String?) {
        id?.let { viewModel.songsAddedToPlaylist(it, mSongDetails.content_Id ?: "") }
        viewModel.songsAddedToPlaylist.observe(this) { res ->
            Toast.makeText(requireContext(), res.status.toString(), Toast.LENGTH_LONG).show()
        }
    }
//    override fun onStart() {
//        super.onStart()
//        val intentFilter = IntentFilter()
//        intentFilter.addAction("ACTION")
//        intentFilter.addAction("DELETED123")
//        intentFilter.addAction("DELETE")
//        intentFilter.addAction("PROGRESS")
//        LocalBroadcastManager.getInstance(requireContext())
//            .registerReceiver(MyBroadcastReceiver(), intentFilter)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        LocalBroadcastManager.getInstance(requireContext())
//            .unregisterReceiver(MyBroadcastReceiver())
//    }
//    inner class MyBroadcastReceiver : BroadcastReceiver() {
//        @SuppressLint("NotifyDataSetChanged")
//        override fun onReceive(context: Context, intent: Intent) {
//            when (intent.action) {
//                "ACTION" -> {
//                    //parentAdapter.notifyDataSetChanged()
//                    //val data = intent.getIntExtra("currentProgress",0)
////                    val downloadingItems =
////                        intent.getParcelableArrayListExtra<DownloadingItem>("downloading_items")
////                    downloadingItems?.let {
////                        progressIndicatorUpdate(it)
////                        Log.e("getDownloadManagerx",
////                            "habijabi: ${it.toString()} ")
//                    }
//
//                "DELETED123" -> {
//                   // val cacheRepository = CacheRepository(requireContext())
//
//                   // allDownloadAdapter.notifyDataSetChanged()
//                   // Log.e("DELETED", "STATE_REMOVING FIRED <--> CLICK")
//                }
//                "DELETE" -> {
//                  // loadData()
//                  // parentAdapter.notifyDataSetChanged()
//                    Log.e("DELETED12333", "STATE_REMOVING FIRED <--> CLICK")
//                }
////                "PROGRESS" -> {
////                    parentAdapter.notifyDataSetChanged()
////                }
//                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}