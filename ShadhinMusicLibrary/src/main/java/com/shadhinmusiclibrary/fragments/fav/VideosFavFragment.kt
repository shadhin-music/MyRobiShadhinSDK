package com.shadhinmusiclibrary.fragments.fav

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.adapter.FavVideoAdapter
import com.shadhinmusiclibrary.adapter.FavoriteAlbumAdapter
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.DownloadingItem
import com.shadhinmusiclibrary.data.model.VideoModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.download.MyBLDownloadService
import com.shadhinmusiclibrary.download.room.DownloadedContent
import com.shadhinmusiclibrary.download.room.WatchLaterContent
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.library.player.Constants
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import java.text.SimpleDateFormat
import java.util.*

internal class VideosFavFragment : BaseFragment(),
    DownloadedSongOnCallBack,
    CommonPSVCallback {
    private lateinit var dataAdapter: FavVideoAdapter
    private var isDownloaded: Boolean = false
    private var iswatched: Boolean = false
    private lateinit var favViewModel: FavViewModel
    private lateinit var parentAdapter: ConcatAdapter
    private lateinit var footerAdapter: HomeFooterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_fragment_download_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    fun loadData() {
        val cacheRepository = CacheRepository(requireContext())
        dataAdapter =  FavVideoAdapter(this, this,   cacheRepository)

        cacheRepository.getVideoFavContent()?.let {
            dataAdapter.setData(
                it.toMutableList()
            )
        }!!
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder().apply { setIsolateViewTypes(false) }.build()
        footerAdapter = HomeFooterAdapter()
        parentAdapter = ConcatAdapter(config, dataAdapter)
        recyclerView.adapter = parentAdapter
        // Log.e("TAG","VIDEOS: "+ cacheRepository.getAllVideosDownloads())
        favViewModel =
            ViewModelProvider(this, injector.factoryFavContentVM)[FavViewModel::class.java]
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

    override fun onClickFavItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
//            if (playerViewModel.currentMusic != null && (mSongDetails[clickItemPosition].rootId == playerViewModel.currentMusic?.rootId)) {
//                if ((mSongDetails[clickItemPosition].contentId != playerViewModel.currentMusic?.mediaId)) {
//                    Log.e("TAG","SONG :"+ mSongDetails[clickItemPosition].contentId )
//                    Log.e("TAG","SONG :"+ playerViewModel.currentMusic?.mediaId )
//                    playerViewModel.skipToQueueItem(clickItemPosition)
//                } else {
//                    playerViewModel.togglePlayPause()
//                }
//            } else {
//                playItem(
//                    UtilHelper.getSongDetailToDownloadedSongDetailList(mSongDetails),
//                    clickItemPosition
//                )
//            }
    }

    override fun onFavAlbumClick(itemPosition: Int, mSongDetails: MutableList<IMusicModel>) {

    }

    override fun onClickBottomItemPodcast(mSongDetails: IMusicModel) {

    }

    override fun onClickBottomItemSongs(mSongDetails: IMusicModel) {

    }

    override fun onClickBottomItemVideo(mSongDetails: IMusicModel) {
        openDialog(
            VideoModel(
                "",
                "",
                mSongDetails.titleName,
                mSongDetails.artistName,
                mSongDetails.artist_Id,
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

    private fun openDialog(mSongDetail: VideoModel) {
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
        artistname?.text = mSongDetail.artist
        val image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
        val url = mSongDetail.image
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
        title?.text = mSongDetail.title
        if (image != null) {
            Glide.with(this).load(url?.replace("<\$size\$>", "300")).into(image)
        }

        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)
        var downloaded = cacheRepository.getDownloadById(mSongDetail.contentID.toString())
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
                cacheRepository.deleteDownloadById(mSongDetail.contentID.toString())
                DownloadService.sendRemoveDownload(
                    requireContext(),
                    MyBLDownloadService::class.java, mSongDetail.contentID.toString(), false
                )
                val localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", mSongDetail.contentID.toString())
                localBroadcastManager.sendBroadcast(localIntent)
                isDownloaded = false

            } else {
                val url = "${Constants.FILE_BASE_URL}${mSongDetail.playUrl}"
                val downloadRequest: DownloadRequest =
                    DownloadRequest.Builder(mSongDetail.contentID.toString(), url.toUri())
                        .build()
                injector.downloadTitleMap[mSongDetail.contentID.toString()] =
                    mSongDetail.title.toString()
                DownloadService.sendAddDownload(
                    requireContext(),
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )

                if (cacheRepository.isDownloadCompleted(mSongDetail.contentID.toString())) {
                    cacheRepository.insertDownload(
                        DownloadedContent().apply {
                            content_Id = mSongDetail.contentID.toString()
                            rootContentId = mSongDetail.rootId.toString()
                            imageUrl = mSongDetail.image.toString()
                            titleName = mSongDetail.title.toString()
                            content_Type = mSongDetail.contentType.toString()
                            playingUrl = mSongDetail.playUrl
                            rootContentType = mSongDetail.contentType.toString()
                            artistName = mSongDetail.artist.toString()
                            artist_Id = mSongDetail.artistId.toString()
                            total_duration = mSongDetail.duration.toString()
                        }
                    )
                    isDownloaded = true
                }
            }
            bottomSheetDialog.dismiss()
        }
        val watchlaterImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgWatchlater)
        val textViewWatchlaterTitle: TextView? = bottomSheetDialog.findViewById(R.id.txtwatchLater)
        val watched = cacheRepository.getWatchedVideoById(mSongDetail.contentID.toString())
        if (watched?.isWatched ==1) {
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
                cacheRepository.deleteWatchlaterById(mSongDetail.contentID.toString())
                iswatched = false
            } else {
                val url = "${Constants.FILE_BASE_URL}${mSongDetail.playUrl}"
                cacheRepository.insertWatchLater(
                    WatchLaterContent(
                        mSongDetail.contentID.toString(),
                        mSongDetail.rootId.toString(),
                        mSongDetail.image.toString(),
                        mSongDetail.title.toString(),
                        mSongDetail.contentType.toString(),
                        url,
                        mSongDetail.contentType.toString(),
                        0,
                        0,1,
                        mSongDetail.artist.toString(),
                        mSongDetail.duration.toString()
                    )
                )
                iswatched = true
            }
            bottomSheetDialog.dismiss()
        }
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
        val constraintFav: ConstraintLayout? = bottomSheetDialog.findViewById(R.id.constraintFav)
        val favImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgLike)
        val textFav: TextView? = bottomSheetDialog.findViewById(R.id.tvFav)
        var isFav = false
        val isAddedToFav = cacheRepository.getFavoriteById(mSongDetail.contentID.toString())
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
                favViewModel.deleteFavContent(mSongDetail.contentID.toString(), "V")
                cacheRepository.deleteFavoriteById(mSongDetail.contentID.toString())
                Toast.makeText(context, "Removed from favorite", Toast.LENGTH_LONG).show()
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
                isFav = false
                dataAdapter.updateData(cacheRepository.getVideoFavContent())
            } else {

                favViewModel.addFavContent(mSongDetail.contentID.toString(), "V")

                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
                cacheRepository.insertFavSingleContent(
                    FavDataModel().apply {
                        content_Id = mSongDetail.contentID.toString()
                        album_Id = mSongDetail.albumId
                        albumImage = mSongDetail.image
                        artistName = mSongDetail.artist
                        artist_Id = mSongDetail.artistId
                        clientValue = 2
                        content_Type = "V"
                        fav = "1"
                        imageUrl = mSongDetail.image
                        playingUrl = mSongDetail.playUrl
                        rootContentId = mSongDetail.rootId
                        titleName = mSongDetail.title
                        createDate = DateTime
                    }
                )
                isFav = true
                Toast.makeText(context, "Added to favorite", Toast.LENGTH_LONG).show()
            }
            bottomSheetDialog.dismiss()
        }
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
            val downloaded: ImageView? = view?.findViewWithTag(200)
            progressIndicator?.progress = it.progress.toInt()
////           if(it.progress.toInt() <= 99) {
            progressIndicator?.visibility = View.VISIBLE
//                downloaded?.visibility= View.GONE
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
                    dataAdapter.notifyDataSetChanged()
                }
                "PROGRESS" -> {
                    //viewModel.videos(videoList)
                    Log.e("PROGRESS", "broadcast fired")
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }
}