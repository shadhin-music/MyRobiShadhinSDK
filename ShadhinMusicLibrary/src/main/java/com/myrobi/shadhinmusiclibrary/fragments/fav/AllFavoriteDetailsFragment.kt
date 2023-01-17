package com.myrobi.shadhinmusiclibrary.fragments.fav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.activities.ItemClickListener
import com.myrobi.shadhinmusiclibrary.adapter.AllFavoriteAdapter
import com.myrobi.shadhinmusiclibrary.adapter.CreatePlaylistListAdapter
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.myrobi.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.SongTrackModel
import com.myrobi.shadhinmusiclibrary.download.MyBLDownloadService
import com.myrobi.shadhinmusiclibrary.download.room.DownloadedContent
import com.myrobi.shadhinmusiclibrary.download.room.WatchLaterContent
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.CreateplaylistViewModel
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

internal class AllFavoriteDetailsFragment : BaseFragment(),
    DownloadedSongOnCallBack,
    CommonPSVCallback,
    ItemClickListener,
    onFavArtistClickAll {

    private var isDownloaded: Boolean = false
    private var iswatched: Boolean = false
    private lateinit var favViewModel: FavViewModel
    private lateinit var viewModel: CreateplaylistViewModel
    private lateinit var navController: NavController
    private lateinit var dataAdapter: AllFavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        navController = findNavController()
        return inflater.inflate(R.layout.my_bl_sdk_fragment_download_details, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        viewModel =
            ViewModelProvider(
                this,
                injector.factoryCreatePlaylistVM
            )[CreateplaylistViewModel::class.java]
    }

    fun loadData() {
        favViewModel =
            ViewModelProvider(this, injector.factoryFavContentVM)[FavViewModel::class.java]
        val cacheRepository = CacheRepository(requireContext())

        dataAdapter = AllFavoriteAdapter(this, this, this)

        cacheRepository.getAllFavoriteContent()?.let {
            dataAdapter.setData(
                it.toMutableList(),
                "",
                playerViewModel.currentMusic?.mediaId
            )
        }!!

        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = dataAdapter
        // Log.e("TAG","VIDEOS: "+ cacheRepository.getAllVideosDownloads())
        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music != null) {
                if (music.mediaId != null) {
                    dataAdapter.setPlayingSong(music.mediaId!!)
                }
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {

    }

    override fun onClickFavItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        if (playerViewModel.currentMusic != null && (mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
            if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                playerViewModel.skipToQueueItem(clickItemPosition)
            } else {
                playerViewModel.togglePlayPause()
            }
        } else {
            //Todo song play and need test
            playItem(
                mSongDetails,
                clickItemPosition
            )
        }
    }

    override fun onClickBottomItemPodcast(mSongDetails: IMusicModel) {
        showBottomSheetDialogForPodcast(
            navController,
            context = requireContext(),
            SongTrackModel().apply {
                content_Type = mSongDetails.content_Type
                artistName = mSongDetails.artistName.toString()
                total_duration = mSongDetails.total_duration.toString()
                content_Id = mSongDetails.content_Id
                imageUrl = mSongDetails.imageUrl.toString()
                titleName = mSongDetails.titleName.toString()
                playingUrl = mSongDetails.playingUrl.toString()
                rootContentId = mSongDetails.rootContentId.toString()
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
                content_Id = mSongDetails.content_Id
                album_Id = mSongDetails.album_Id.toString()
                artist_Id = mSongDetails.artist_Id.toString()
                album_Name = mSongDetails.album_Name.toString()
                imageUrl = mSongDetails.imageUrl.toString()
                artistName = mSongDetails.artistName.toString()
                content_Type = mSongDetails.content_Type.toString()
                playingUrl = mSongDetails.playingUrl.toString()
                titleName = mSongDetails.titleName.toString()
                total_duration = mSongDetails.total_duration
            }
        )
    }

    override fun onClickBottomItemVideo(mSongDetails: IMusicModel) {
        openDialog(
            VideoModel(
                mSongDetails.album_Id,
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

    fun openDialog(item: VideoModel) {
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
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
        val url = item.image
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = item.title
        if (image != null) {
            Glide.with(this).load(url?.replace("<\$size\$>", "300")).into(image)
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
                Log.e("TAG", "DELETED: " + item.playUrl)
                if (cacheRepository.isDownloadCompleted(item.contentID.toString()).equals(true)) {
                    cacheRepository.insertDownload(
                        DownloadedContent().apply {
                            content_Id = item.contentID.toString()
                            rootContentId = item.rootId.toString()
                            imageUrl = item.image.toString()
                            titleName = item.title.toString()
                            content_Type = item.contentType.toString()
                            playingUrl = item.playUrl
                            content_Type = item.contentType.toString()
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
                        0, 1,
                        item.artist.toString(),
                        item.duration.toString()
                    )
                )
                iswatched = true
            }
            bottomSheetDialog.dismiss()
        }
        val constraintFav: ConstraintLayout? = bottomSheetDialog.findViewById(R.id.constraintFav)
        val favImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgLike)
        val textFav: TextView? = bottomSheetDialog.findViewById(R.id.tvFav)
        var isFav = false
        val isAddedToFav = cacheRepository.getFavoriteById(item.contentID.toString())
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
                favViewModel.deleteFavContent(item.contentID.toString(), "V")
                cacheRepository.deleteFavoriteById(item.contentID.toString())
                Toast.makeText(context, "Removed from favorite", Toast.LENGTH_LONG).show()
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
                isFav = false

                dataAdapter.updateData(cacheRepository.getAllFavoriteContent())
                Log.e("TAG", "NAME: " + isFav)
            } else {
                favViewModel.addFavContent(item.contentID.toString(), "V")
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
                Log.e("TAG", "NAME123: " + isFav)
                cacheRepository.insertFavSingleContent(
                    FavDataModel().apply {
                        content_Id = item.contentID.toString()
                        album_Id = item.albumId
                        imageUrl = item.image
                        artistName = item.artist
                        artist_Id = item.artistId
                        clientValue = 2
                        content_Type = "V"
                        fav = "1"
                        playingUrl = item.playUrl
                        rootContentId = item.rootId
                        titleName = item.title
                        total_duration = item.duration
                        createDate = DateTime
                    }
                )
                isFav = true
                Toast.makeText(context, "Added to favorite", Toast.LENGTH_LONG).show()
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
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
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
        val constraintDownload: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintDownload)
        val constraintAlbum: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAlbum)
        val constraintPlaylist: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoPlaylist)
        if (argHomePatchDetail?.content_Type?.equals("P") == true) {
            constraintAlbum?.visibility = GONE
            constraintDownload?.visibility = GONE
            constraintPlaylist?.visibility = GONE
        }
        if (argHomePatchDetail?.content_Type?.equals("A") == true) {
            constraintDownload?.visibility = GONE
            constraintPlaylist?.visibility = GONE

        }
        if (argHomePatchDetail?.content_Type?.equals("R") == true) {
            constraintDownload?.visibility = GONE
            constraintPlaylist?.visibility = GONE

        }
        val imageArtist: ImageView? = bottomSheetDialog.findViewById(R.id.imgAlbum)
        val textAlbum: TextView? = bottomSheetDialog.findViewById(R.id.tvAlbums)
        textAlbum?.text = "Go to Artist"
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
        val url = argHomePatchDetail?.imageUrl
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = argHomePatchDetail?.titleName
        val artistname = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        artistname?.text = mSongDetails.artistName
        if (image != null) {
            Glide.with(context).load(UtilHelper.getImageUrlSize300(url ?: "")).into(image)
        }
        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)
        var isDownloaded = false
        var downloaded = cacheRepository.getDownloadById(mSongDetails.content_Id)
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

        constraintDownload?.setOnClickListener {
            if (isDownloaded.equals(true)) {
                cacheRepository.deleteDownloadById(mSongDetails.content_Id!!)
                DownloadService.sendRemoveDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    mSongDetails.content_Id!!,
                    false
                )
                Log.e("TAG", "DELETED: " + isDownloaded)
                val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", mSongDetails.content_Id)
                localBroadcastManager.sendBroadcast(localIntent)
                isDownloaded = false
            } else {
                val url = "${Constants.FILE_BASE_URL}${mSongDetails.playingUrl}"
                var downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(mSongDetails.content_Id!!, url.toUri())
                        .build()
                injector.downloadTitleMap[mSongDetails.content_Id ?: ""] =
                    mSongDetails.titleName ?: ""
                DownloadService.sendAddDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )
                if (cacheRepository.isDownloadCompleted(mSongDetails.content_Id!!).equals(true)) {
//                if (cacheRepository.isDownloadCompleted(mSongDetails.ContentID).equals(true)) {
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
                            artist_Id = mSongDetails.artist_Id.toString()
                            total_duration = mSongDetails.total_duration
                        }
                    )
                    isDownloaded = true
                }
            }
            bottomSheetDialog.dismiss()
        }

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

        constraintPlaylist?.setOnClickListener {
            gotoPlayList(context, mSongDetails)
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
//                    isFav = true
//                    favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
//                }
//                else {
//                    isFav = false
//                 favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
//                }
//            }
//        }
        val isAddedToFav = cacheRepository.getFavoriteById(mSongDetails.content_Id)
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
                    mSongDetails.content_Id!!,
                    mSongDetails.content_Type!!
                )
                cacheRepository.deleteFavoriteById(mSongDetails.content_Id!!)
                Toast.makeText(requireContext(), "Removed from favorite", Toast.LENGTH_LONG).show()
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
                isFav = false
                dataAdapter.updateData(cacheRepository.getAllFavoriteContent())
                Log.e("TAG", "NAME: " + isFav)
            } else {

                favViewModel.addFavContent(mSongDetails.content_Id!!, mSongDetails.content_Type!!)

                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
                cacheRepository.insertFavSingleContent(
                    FavDataModel().apply {
                        content_Id = mSongDetails.content_Id
                        album_Id = mSongDetails.album_Id
                        imageUrl = mSongDetails.imageUrl
                        artistName = mSongDetails.artistName
                        artist_Id = mSongDetails.artist_Id
                        clientValue = 2
                        content_Type = mSongDetails.content_Type
                        fav = "1"
                        playingUrl = mSongDetails.playingUrl
                        rootContentId = mSongDetails.rootContentId
                        rootContentType = mSongDetails.rootContentType
                        titleName = mSongDetails.titleName
                        total_duration = mSongDetails.total_duration
                        createDate = DateTime
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
//        bsdNavController.navigate(R.id.action_download_to_to_artistDetailsFragment,
//            Bundle().apply {
//                putSerializable(
//                    AppConstantUtils.PatchItem,
//                    HomePatchItem("","A", mutableListOf(),"Artist","",0,0)
//                )
//                putSerializable(
//                    AppConstantUtils.PatchDetail,
//                    HomePatchDetail(mSongDetails.albumId.toString(),"","",mSongDetails.artist,mSongDetails.ArtistId.toString(),"","",
//                        mSongDetails.ContentID,mSongDetails.ContentType,"","","",false,"",
//                        0,"","","",mSongDetails.PlayUrl.toString(),"","",
//                        false,"","","","",mSongDetails.image.toString(),"",mSongDetails.title.toString()) as Serializable
//                )
//            })

        bsdNavController.navigate(
            R.id.to_artist_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "A", mutableListOf(), "Artist", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    HomePatchDetailModel().apply {
                        album_Id = mSongDetails.album_Id.toString()
                        artistName = mSongDetails.artistName!!
                        artist_Id = mSongDetails.artist_Id.toString()
                        content_Id = mSongDetails.content_Id
                        content_Type = mSongDetails.content_Type!!
                        playingUrl = mSongDetails.playingUrl.toString()
                        imageUrl = mSongDetails.imageUrl.toString()
                        titleName = mSongDetails.titleName.toString()
                        total_duration = mSongDetails.total_duration
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
        var savePlaylist: AppCompatButton? = bottomSheetDialog.findViewById(R.id.btnSavePlaylist)
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
                val name: String = etCreatePlaylist.getText().toString()
                Log.e("TAG", "NAME: " + name)
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

    fun addSongsToPlaylist(mSongDetails: IMusicModel, id: String?) {
        id?.let { viewModel.songsAddedToPlaylist(it, mSongDetails.content_Id) }
        viewModel.songsAddedToPlaylist.observe(this) { res ->
            Toast.makeText(requireContext(), res.status.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onFavAlbumClick(itemPosition: Int, mSongDetails: MutableList<IMusicModel>) {
        val favDat = mSongDetails[itemPosition]
        navController.navigate(
            R.id.to_album_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "R", mutableListOf(), "Release", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    HomePatchDetailModel().apply {
                        album_Id = favDat.album_Id.toString()
                        artistName = favDat.artistName.toString()
                        artist_Id = favDat.artist_Id.toString()
                        content_Id = favDat.content_Id
                        content_Type = favDat.content_Type.toString()
                        playingUrl = favDat.playingUrl.toString()
                        imageUrl = favDat.imageUrl.toString()
                        titleName = favDat.titleName.toString()
                        total_duration = favDat.total_duration
                    } as Serializable
                )
            })
    }

    override fun onFavArtistClick(itemPosition: Int, favData: List<IMusicModel>) {
        val mFavData = favData[itemPosition]
        navController.navigate(
            R.id.to_artist_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "A", mutableListOf(), "Artist", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    HomePatchDetailModel().apply {
                        album_Id = mFavData.album_Id.toString()
                        artistName = mFavData.artistName.toString()
                        artist_Id = mFavData.artist_Id.toString()
                        content_Id = mFavData.content_Id
                        content_Type = mFavData.content_Type.toString()
                        playingUrl = mFavData.playingUrl.toString()
                        imageUrl = mFavData.imageUrl.toString()
                        titleName = mFavData.titleName.toString()
                        total_duration = mFavData.total_duration
                    } as Serializable
                )
            })
    }

    override fun onFavPlaylistClick(itemPosition: Int, favData: List<IMusicModel>) {
        val mfavData = favData[itemPosition]
        navController.navigate(
            R.id.to_playlist_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "P", mutableListOf(), "Playlist", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    HomePatchDetailModel().apply {
                        album_Id = mfavData.album_Id.toString()
                        artistName = mfavData.artistName.toString()
                        artist_Id = mfavData.artist_Id.toString()
                        content_Id = mfavData.content_Id
                        content_Type = mfavData.content_Type.toString()
                        playingUrl = mfavData.playingUrl.toString()
                        imageUrl = mfavData.imageUrl.toString()
                        titleName = mfavData.titleName.toString()
                        total_duration = mfavData.total_duration
                    } as Serializable
                )
            })
    }

    fun showBottomSheetDialogForPodcast(
        bsdNavController: NavController,
        context: Context,
        iSongTrack: IMusicModel,
        argHomePatchItem: HomePatchItemModel?,
        argHomePatchDetail: HomePatchDetailModel?,
    ) {
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
        val cacheRepository = CacheRepository(requireContext())
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
        val contentView =
            View.inflate(
                context,
                R.layout.my_bl_sdk_bottomsheet_three_dot_menu_layout,
                null
            )
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
        val url = iSongTrack.imageUrl
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = iSongTrack.titleName
        val artistname = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        artistname?.text = iSongTrack.artistName
        if (image != null) {
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(url!!))
                .into(image)
        }

        val constraintAlbum: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAlbum)
        constraintAlbum?.setOnClickListener {
//            gotoArtistFromPlaylist(
//                bsdNavController,
//                context,
//                mSongDetails,
//                argHomePatchItem,
//                argHomePatchDetail
//            )
            bottomSheetDialog.dismiss()
        }
        constraintAlbum?.visibility = View.GONE
        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? =
            bottomSheetDialog.findViewById(R.id.tv_download)
        var isDownloadComplete = false
        val downloaded = cacheRepository.getDownloadById(iSongTrack.content_Id)
        if (downloaded?.playingUrl != null) {
            isDownloadComplete = true
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_ic_delete)
        } else {
            isDownloadComplete = false
            downloadImage?.setImageResource(R.drawable.my_bl_sdk_icon_dowload)
        }

        if (isDownloadComplete) {
            textViewDownloadTitle?.text = "Remove From Download"
        } else {
            textViewDownloadTitle?.text = "Download Offline"
        }
        val constraintDownload: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintDownload)
        constraintDownload?.setOnClickListener {
            if (isDownloadComplete) {
//                cacheRepository.deleteDownloadById(track.EpisodeId)
                cacheRepository.deleteDownloadById(iSongTrack.content_Id)
                DownloadService.sendRemoveDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    iSongTrack.content_Id,
                    false
                )
                val localBroadcastManager =
                    LocalBroadcastManager.getInstance(requireContext())
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", iSongTrack.content_Id)
//                    .putExtra("contentID", track.EpisodeId)
                localBroadcastManager.sendBroadcast(localIntent)

            } else {
                val mPlayingUrl = "${Constants.FILE_BASE_URL}${iSongTrack.playingUrl}"
                val downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(iSongTrack.content_Id, mPlayingUrl.toUri())
                        .build()
                Log.e("TAG", "NAME: " + iSongTrack.titleName + " URL " + mPlayingUrl)
                injector.downloadTitleMap[iSongTrack.content_Id ?: ""] = iSongTrack.titleName ?: ""
                DownloadService.sendAddDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )

                //Todo iSongTrack.EpisodeId
                if (cacheRepository.isDownloadCompleted(iSongTrack.content_Id)) {
//                    val contentType = iSongTrack.content_Type
//                    val podcastType = contentType?.take(2)
//                    val  Type = contentType?.takeLast(2)
                    cacheRepository.insertDownload(
                        DownloadedContent().apply {

                            content_Id = iSongTrack.content_Id
                            album_Id = iSongTrack.album_Id
                            rootContentId = iSongTrack.rootContentId
                            imageUrl = iSongTrack.imageUrl
                            titleName = iSongTrack.titleName
                            content_Type = iSongTrack.content_Type
                            playingUrl = iSongTrack.playingUrl
                            rootContentType = iSongTrack.rootContentType
                            titleName = iSongTrack.titleName
                            artist_Id = iSongTrack.artist_Id
                            artistName = iSongTrack.artistName.toString()
                            total_duration = iSongTrack.total_duration

                        }
                    )
                }
            }
            bottomSheetDialog.dismiss()
        }

        val constraintFav: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintFav)
        val favImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgLike)
        val textFav: TextView? = bottomSheetDialog.findViewById(R.id.tvFav)
        var isFav = false
        //todo iSongTrack.Id
        val isAddedToFav = cacheRepository.getFavoriteById(iSongTrack.content_Id)
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
                val contentType = iSongTrack.content_Type
                val podcastType = contentType?.take(2)
                val Type = contentType?.takeLast(2)
                //todo iSongTrack.Id
                favViewModel.deleteFavContent(
                    iSongTrack.content_Id,
                    iSongTrack.content_Type.toString()
                )
                cacheRepository.deleteFavoriteById(iSongTrack.content_Id)
                Toast.makeText(
                    requireContext(),
                    "Removed from favorite",
                    Toast.LENGTH_LONG
                )
                    .show()
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
                isFav = false
                dataAdapter.updateData(cacheRepository.getAllFavoriteContent()?.toMutableList())
                Log.e("TAG", "NAME: " + iSongTrack.content_Type)
            } else {
                val contentType = iSongTrack.content_Type
                val podcastType = contentType?.take(2)
                val Type = contentType?.takeLast(2)
                //todo iSongTrack.EpisodeId
                favViewModel.addFavContent(
                    iSongTrack.content_Id,
                    iSongTrack.content_Type.toString()
                )

                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)

                // todo iSongTrack.Id.toString(),
                //      iSongTrack.Id.toString(),
                cacheRepository.insertFavSingleContent(
                    FavDataModel().apply {
                        content_Id = iSongTrack.content_Id
                        album_Id = iSongTrack.content_Id
                        albumImage = iSongTrack.imageUrl
                        clientValue = 2
                        content_Type = iSongTrack.content_Type.toString()
                        fav = "1"
                        imageUrl = iSongTrack.imageUrl
                        playingUrl = iSongTrack.playingUrl
                        rootContentId = iSongTrack.rootContentId
                        rootContentType = iSongTrack.rootContentType
                        titleName = iSongTrack.titleName
                        total_duration = iSongTrack.total_duration
                        createDate = DateTime
                    }
                )
                isFav = true
                Toast.makeText(requireContext(), "Added to favorite", Toast.LENGTH_LONG)
                    .show()
            }
            bottomSheetDialog.dismiss()
        }
        val constraintPlaylist: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoPlaylist)
        constraintPlaylist?.visibility = View.GONE
    }
}

internal interface onFavArtistClickAll {
    fun onFavArtistClick(itemPosition: Int, favData: List<IMusicModel>)
    fun onFavPlaylistClick(itemPosition: Int, favData: List<IMusicModel>)
}