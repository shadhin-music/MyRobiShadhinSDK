package com.shadhinmusiclibrary.activities.video

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.VideoAdapter
import com.shadhinmusiclibrary.data.model.DownloadingItem
import com.shadhinmusiclibrary.data.model.VideoModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.di.ActivityEntryPoint
import com.shadhinmusiclibrary.download.MyBLDownloadService
import com.shadhinmusiclibrary.download.room.DatabaseClient
import com.shadhinmusiclibrary.download.room.DownloadedContent
import com.shadhinmusiclibrary.download.room.WatchLaterContent
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.Constants
import com.shadhinmusiclibrary.library.player.ShadhinMusicQueueNavigator
import com.shadhinmusiclibrary.library.player.audio_focus.AudioFocusManager
import com.shadhinmusiclibrary.library.player.audio_focus.AudioFocusManagerFactory
import com.shadhinmusiclibrary.library.player.data.source.MediaSources
import com.shadhinmusiclibrary.library.player.data.source.ShadhinVideoMediaSource
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.UtilHelper
import com.shadhinmusiclibrary.utils.calculateVideoHeight
import com.shadhinmusiclibrary.utils.px
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


internal class VideoActivity : AppCompatActivity(),
    ActivityEntryPoint,
    AudioManager.OnAudioFocusChangeListener,
    BottomsheetDialog {

    /**1144x480 OR 856x480*/
    private val videoWidth: Int = 856
    private val videoHeight: Int = 480

    private var isDownloaded: Boolean = false
    private var iswatched: Boolean = false
    private lateinit var mainLayout: FrameLayout
    private lateinit var adapter: VideoAdapter
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var videoTitleTextView: TextView
    private lateinit var videoDescTextView: TextView
    private lateinit var layoutToggle: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var favLayout: LinearLayout
    private lateinit var downloadLayout: LinearLayout
    private lateinit var watchlaterLayout: LinearLayout
    private lateinit var fullscreenToggleButton: ImageButton
    private lateinit var favImageView: ImageView
    private lateinit var watchIcon: ImageView
    private lateinit var downloadImage: ImageView
    private lateinit var favTextView: TextView
    private lateinit var watchlatertext: TextView
    private lateinit var downloadTextview: TextView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var bufferProgress: ProgressBar
    private lateinit var playerLayout: FrameLayout
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var playerOnScaleGestureListener: PlayerOnScaleGestureListener

    private var exoPlayer: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var mediaSession: MediaSessionCompat
    private val contactMediaSource = ConcatenatingMediaSource()
    private lateinit var shadhinQueueNavigator: TimelineQueueNavigator
    private var videoMediaSource: MediaSources? = null
    private lateinit var audioFocusManager: AudioFocusManager

    private var currentPosition = 0
    private var videoList: ArrayList<VideoModel>? = ArrayList()
    private lateinit var viewModel: VideoViewModel
    private val receiver = MyBroadcastReceiver()
    private lateinit var videoViewModelFactory: VideoViewModelFactory
    private lateinit var favViewModel: FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_bl_sdk_activity_video)
        initAudioFocus()
        setupViewModel()
        setupUI()
        setupAdapter()
        initData()
        initializePlayer()
        gestureSetup()
        observe()
    }

    val cacheRepository by lazy {
        CacheRepository(this)
    }

    private fun initAudioFocus() {
        audioFocusManager = AudioFocusManagerFactory.createAudioFocusManager()
        audioFocusManager.initialize(applicationContext, this)
        audioFocusManager.requestAudioFocus()
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            exoPlayer?.pause()
        }
    }

    private fun setupViewModel() {
        videoViewModelFactory = VideoViewModelFactory(DatabaseClient(applicationContext))
        viewModel = ViewModelProvider(
            this,
            videoViewModelFactory
        )[VideoViewModel::class.java]

        favViewModel = ViewModelProvider(
            this,
            injector.factoryFavContentVM
        )[FavViewModel::class.java]
    }

    private fun setupUI() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = ""
        findViewById<ImageView>(R.id.imageBack).setOnClickListener {
            onBackPressed()
        }
        val searchBar: AppCompatImageView = findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
        mainLayout = findViewById(R.id.main)
        videoRecyclerView = findViewById(R.id.videoRecyclerView)
        videoTitleTextView = findViewById(R.id.videoTitle)
        videoDescTextView = findViewById(R.id.videoDesc)
        favImageView = findViewById(R.id.favoriteIcon)
        downloadImage = findViewById(R.id.imageDownload)
        watchIcon = findViewById(R.id.watchIcon)
        favTextView = findViewById(R.id.txtFav)
        watchlatertext = findViewById(R.id.txtWatch)
        downloadTextview = findViewById(R.id.txtDownload)
        layoutToggle = findViewById(R.id.layoutToggle)
        favLayout = findViewById(R.id.favourite)
        watchlaterLayout = findViewById(R.id.watchLater)
        downloadLayout = findViewById(R.id.download)
        mainProgressBar = findViewById(R.id.main_progress)
        bufferProgress = findViewById(R.id.bufferProgress)
        playerLayout = findViewById(R.id.playerLayout)
        playerView = findViewById(R.id.playerView)
        backButton = playerView.findViewById(R.id.backButton)
        fullscreenToggleButton = playerView.findViewById(R.id.toggleOrientationButton)
        layoutToggle.setOnClickListener { viewModel.layoutToggle() }
        backButton.setOnClickListener { onBackPressed() }
        fullscreenToggleButton.setOnClickListener { toggleOrientation() }
        configOrientation(resources.configuration.orientation)
    }

    private fun openSearch() {
        startActivity(Intent(this, SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })
    }

    private fun setupAdapter() {
        adapter = VideoAdapter(this, this, cacheRepository)
        videoRecyclerView.adapter = adapter
        videoRecyclerView.layoutManager = adapter.layoutManager

        adapter.onItemClickListeners { item, isMenu ->
            if (!isMenu) {
                togglePlayPause(item)
                var iswatched = false
                val watched = cacheRepository.getWatchedVideoById(item.contentID.toString())
                val downloaded = cacheRepository.getDownloadById(item.contentID.toString())
                if (watched?.isWatched ==1) {
                    iswatched = true
                    watchIcon.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))

                } else {
                    iswatched = false
                    watchIcon.setColorFilter(
                        applicationContext.getResources().getColor(R.color.my_sdk_down_item_desc)
                    )
                }
                if (downloaded?.playingUrl != null) {
                    isDownloaded = true
                    downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))
                    // downloadTextview.text = "Remove Download"
                } else {
                    isDownloaded = false
                    downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_down_item_desc))
                    // downloadTextview.text = "Download"
                }
                val isAddedToFav = cacheRepository.getFavoriteById(item.contentID.toString())
                var isFav = false
                if (isAddedToFav?.fav == "1") {

                    favImageView.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    isFav = true
                } else {

                    favImageView.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                }
            }
        }
    }

    private fun initData() {
        if (intent.hasExtra(INTENT_KEY_DATA_LIST) &&
            intent.hasExtra(INTENT_KEY_POSITION)
        ) {
            currentPosition = intent.getIntExtra(INTENT_KEY_POSITION, 0)
            videoList = intent.getParcelableArrayListExtra(INTENT_KEY_DATA_LIST)
            viewModel.videos(videoList)

            val downloaded =
                cacheRepository.getDownloadById(videoList?.get(currentPosition)?.contentID.toString())
            val watched =
                cacheRepository.getWatchedVideoById(videoList?.get(currentPosition)?.contentID.toString())
             Log.e("TAG","Message: " + watched?.isWatched)
            if (downloaded?.playingUrl != null) {
                downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))
                isDownloaded = true
            } else {
                downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_down_item_desc))
                isDownloaded = false
            }

            if (watched?.isWatched ==1) {
                // watchIcon.setImageResource(R.drawable.my_bl_sdk_watch_later_remove)
                watchIcon.setColorFilter(
                    applicationContext.resources.getColor(R.color.my_sdk_color_primary)
                )
                iswatched = true
            } else {
                watchIcon.setColorFilter(
                    applicationContext.resources.getColor(R.color.my_sdk_down_item_desc)
                )
                iswatched = false
            }
            var isFav = false
            val isAddedToFav =
                cacheRepository.getFavoriteById(videoList?.get(currentPosition)?.contentID.toString())
            if (isAddedToFav?.fav == "1") {

                favImageView.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                isFav = true
//            textFav?.text = "Remove From favorite"
            } else {

                favImageView.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                isFav = false
                // textFav?.text = "Favorite"
            }
        }
    }

    private fun observe() {
        viewModel.isListLiveData.observe(this, Observer { isListLayout ->
            if (isListLayout) {
                layoutToggle.setImageResource(R.drawable.my_bl_sdk_ic_grid_view)
                adapter.changeToList()
                videoRecyclerView.setPadding(0, 8.px, 0, 16.px)
            } else {
                layoutToggle.setImageResource(R.drawable.my_bl_sdk_ic_list_view_do)
                adapter.changeToGrid()
                videoRecyclerView.setPadding(
                    8.px,
                    8.px,
                    8.px,
                    16.px
                )
            }
        })
        viewModel.progressbarVisibility.observe(this) {
            mainProgressBar.visibility = it
        }
        viewModel.videoListLiveData.observe(this) { videoList ->
            adapter.submitList(videoList)
            addOnPlayerQueue(videoList)
        }
        viewModel.currentVideo.observe(this) { video ->
            videoTitleTextView.text = video.title
            videoDescTextView.text = video.artist
            val currentVideoID: String? = video.contentID
            val watched = cacheRepository.getWatchedVideoById(currentVideoID.toString())
            val downloaded = cacheRepository.getDownloadById(currentVideoID.toString())

            if (downloaded?.playingUrl != null) {
                downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))
                isDownloaded = true
            } else {
                downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_down_item_desc))
                isDownloaded = false
            }

            if (watched?.isWatched==1) {
                watchIcon.setColorFilter(
                    applicationContext.resources.getColor(R.color.my_sdk_color_primary)
                )
                iswatched = true
            } else {
                watchIcon.setColorFilter(
                    applicationContext.resources.getColor(R.color.my_sdk_down_item_desc)
                )
                iswatched = false
            }

            watchlaterLayout.setOnClickListener {
                if (iswatched.equals(true)) {

                    //  val currentVideoID =   viewModel.currentVideo.value?.contentID
                    val currentURL = viewModel.currentVideo.value?.playUrl
                    currentVideoID?.let { it1 -> cacheRepository.deleteWatchlaterById(it1) }
                    watchIcon.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_down_item_desc))
                    //watchlatertext?.text = "Remove From Watchlater"

                    iswatched = false
                    Log.e("TAG", "CLICKED123: " + iswatched)
                } else {
                    //val currentVideoID:String? =   viewModel.currentVideo.value?.contentID
                    val currentURL: String? = viewModel.currentVideo.value?.playUrl
                    val currentRootID = viewModel.currentVideo.value?.rootId
                    val currentImage = viewModel.currentVideo.value?.image
                    val currentTitle = viewModel.currentVideo.value?.title
                    val currentRootType = viewModel.currentVideo.value?.contentType
                    val currentArtist = viewModel.currentVideo.value?.artist
                    val duration = viewModel.currentVideo.value?.duration
                    watchIcon.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))
                    iswatched = true

                    //watchlatertext.text = "Watch Later"
                    val url = "${Constants.FILE_BASE_URL}${currentURL}"
                    Log.e("TAG", "CLICKED: " + url)
                    cacheRepository.insertWatchLater(
                        WatchLaterContent(
                            currentVideoID.toString(),
                            currentRootID.toString(),
                            currentImage.toString(),
                            currentTitle.toString(),
                            currentRootType.toString(),
                            currentURL,
                            currentRootType.toString(),
                            0,
                            0,
                            1,
                            currentArtist.toString(),
                            duration.toString()
                        )
                    )
                }
            }
            // val currentVideoID =   viewModel.currentVideo.value?.contentID
            // isDownloaded= false
            downloadLayout.setOnClickListener {
                if (isDownloaded == true) {
                    cacheRepository.deleteDownloadById(currentVideoID.toString())
                    DownloadService.sendRemoveDownload(
                        applicationContext,
                        MyBLDownloadService::class.java, currentVideoID.toString(), false
                    )
                    val localBroadcastManager =
                        LocalBroadcastManager.getInstance(applicationContext)
                    val localIntent = Intent("DELETED")
                        .putExtra("contentID", currentVideoID.toString())
                    localBroadcastManager.sendBroadcast(localIntent)
                    downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_down_item_desc))
                    isDownloaded = false
                } else {
                    //val currentVideoID:String? =   viewModel.currentVideo.value?.contentID
                    val currentURL: String? = viewModel.currentVideo.value?.playUrl
                    val currentRootID = viewModel.currentVideo.value?.rootId
                    val currentImage = viewModel.currentVideo.value?.image
                    val currentTitle = viewModel.currentVideo.value?.title
                    val currentRootType = viewModel.currentVideo.value?.contentType
                    val currentArtist = viewModel.currentVideo.value?.artist
                    val duration = viewModel.currentVideo.value?.duration
                    val url = "${Constants.FILE_BASE_URL}${currentURL}"
                    val downloadRequest: DownloadRequest? =
                        url.let { it1 ->
                            DownloadRequest.Builder(currentVideoID.toString(), it1.toUri())
                                .build()
                        }
                    injector.downloadTitleMap[currentVideoID.toString()] =
                        currentTitle.toString()
                    downloadRequest?.let { it1 ->
                        DownloadService.sendAddDownload(
                            applicationContext,
                            MyBLDownloadService::class.java,
                            it1,
                            /* foreground= */ false
                        )
                    }
                    // downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))
                    if (cacheRepository.isDownloadCompleted(currentVideoID.toString())) {
                        cacheRepository.insertDownload(
                            DownloadedContent().apply {
                                content_Id = currentVideoID.toString()
                                rootContentId = currentRootID.toString()
                                imageUrl = currentImage.toString()
                                titleName = currentTitle.toString()
                                rootContentType = currentRootType.toString()
                                playingUrl = currentURL
                                content_Type = currentRootType.toString()
                                artistName = currentArtist.toString()
                                total_duration = duration.toString()
                            }
                        )
                        isDownloaded = true
                        downloadImage.setColorFilter(applicationContext.resources.getColor(R.color.my_sdk_color_primary))
                    }
                }
            }

            var isFav = false
            val isAddedToFav = cacheRepository.getFavoriteById(currentVideoID.toString())
            if (isAddedToFav?.fav == "1") {
                favImageView.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                isFav = true
//            textFav?.text = "Remove From favorite"
            } else {
                favImageView.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                isFav = false
                // textFav?.text = "Favorite"
            }
            val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val formatedTime = SimpleDateFormat("HH:mm").format(Date())
            val DateTime = "$formatedDate  $formatedTime"
            favLayout.setOnClickListener {
                if (isFav.equals(true)) {
                    //  val currentVideoID = viewModel.currentVideo.value?.contentID
                    favViewModel.deleteFavContent(currentVideoID.toString(), "V")
                    cacheRepository.deleteFavoriteById(currentVideoID.toString())
                    Toast.makeText(
                        applicationContext,
                        "Removed from favorite",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    favImageView.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                } else {
                    val currentURL = viewModel.currentVideo.value?.playUrl
                    val url = "${Constants.FILE_BASE_URL}${currentURL}"

                    //val currentVideoID: String? = viewModel.currentVideo.value?.contentID
                    // val currentURL:String? = viewModel.currentVideo.value?.playUrl
                    val currentRootID = viewModel.currentVideo.value?.rootId
                    val currentImage = viewModel.currentVideo.value?.image
                    val currentTitle = viewModel.currentVideo.value?.title
                    val currentRootType = viewModel.currentVideo.value?.contentType
                    val currentArtist = viewModel.currentVideo.value?.artist
                    val duration = viewModel.currentVideo.value?.duration
                    favViewModel.addFavContent(currentVideoID.toString(), "V")

                    favImageView.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    cacheRepository.insertFavSingleContent(
                        FavDataModel().apply {
                            content_Id = currentVideoID.toString()
                            album_Id = currentRootID
                            rootImage = currentImage
                            artistName = currentArtist
                            clientValue = 2
                            content_Type = "V"
                            fav = "1"
                            imageUrl = currentImage
                            playingUrl = currentURL
                            rootContentId = currentRootID
                            titleName = currentTitle
                            rootContentType = currentRootType
                            total_duration = duration
                            createDate = DateTime
                        }
                    )
                    isFav = true
                    Toast.makeText(applicationContext, "Added to favorite", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(this)
                .setSeekBackIncrementMs(1000 * 10)
                .setSeekForwardIncrementMs(1000 * 10)
                .build()
            exoPlayer?.addListener(playbackStatus())
            playerView.player = exoPlayer
            mediaSession = MediaSessionCompat(this, packageName)
            val mediaSessionConnector = MediaSessionConnector(mediaSession)
            shadhinQueueNavigator = ShadhinMusicQueueNavigator(mediaSession)
            mediaSessionConnector.setPlayer(exoPlayer)
            mediaSessionConnector.setQueueNavigator(shadhinQueueNavigator)
            mediaSession.isActive = true
        }
    }

    private fun playbackStatus() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                ExoPlayer.STATE_BUFFERING -> showBuffering()
                ExoPlayer.STATE_READY -> hideBuffering()
                else -> hideBuffering()
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            adapter.currentItem(isPlaying, exoPlayer?.currentMediaItem?.mediaId)
            playerView.keepScreenOn = isPlaying
            if (isPlaying) {
                audioFocusManager.requestAudioFocus()
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            adapter.currentItem(false, mediaItem?.mediaId)
            viewModel.currentVideo(mediaItem?.mediaId)
        }
    }

    private fun gestureSetup() {
        playerOnScaleGestureListener = PlayerOnScaleGestureListener(playerView, this)
        scaleGestureDetector = ScaleGestureDetector(this, playerOnScaleGestureListener)
    }

    private fun togglePlayPause(item: VideoModel) {
        if (item.contentID == exoPlayer?.currentMediaItem?.mediaId) {
            if (exoPlayer?.isPlaying == true) {
                exoPlayer?.pause()
            } else {
                exoPlayer?.play()
            }
        } else {
            val windowIndex = videoList?.indexOfFirst { it.contentID == item.contentID }
            if (windowIndex != -1) {
                exoPlayer?.seekTo(windowIndex ?: 0, 0L)
                exoPlayer?.playWhenReady = true
            }
        }
    }

    private fun addOnPlayerQueue(videoList: List<VideoModel>) {
        contactMediaSource.clear()
        exoPlayer?.clearMediaItems()
        videoMediaSource = ShadhinVideoMediaSource(
            this.applicationContext,
            videoList,
            injector.exoplayerCache,
            injector.musicRepository
        )
        val mediaSources = videoMediaSource?.createSources()
        if (!mediaSources.isNullOrEmpty()) {
            contactMediaSource.addMediaSources(mediaSources)
            exoPlayer?.addMediaSource(contactMediaSource)
            exoPlayer?.seekTo(currentPosition, 0)
            exoPlayer?.prepare()
            exoPlayer?.playWhenReady = true
        }
    }

    private fun hideBuffering() {
        bufferProgress.visibility = View.GONE
        playerView.useController = true
    }

    private fun showBuffering() {
        bufferProgress.visibility = View.VISIBLE
        playerView.useController = false
    }

    private fun toggleOrientation() {
        requestedOrientation = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                fullscreenToggleButton.setImageResource(R.drawable.my_bl_sdk_ic_video_fullscreen_minimize)
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                fullscreenToggleButton.setImageResource(R.drawable.my_bl_sdk_ic_video_fullscreen)
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            else -> {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }

    private fun configOrientation(orientation: Int) {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> preparePortraitUI()
            Configuration.ORIENTATION_LANDSCAPE -> prepareLandscapeUI()
            else -> preparePortraitUI()
        }
    }

    private fun preparePortraitUI() {
        showSystemUI()
        supportActionBar?.show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            setPortraitPlayerSize()
        }, 100)
        fullscreenToggleButton.setImageResource(R.drawable.my_bl_sdk_ic_video_fullscreen)
    }

    private fun prepareLandscapeUI() {
        hideSystemUI()
        supportActionBar?.hide()
        setLandscapePlayerSize()
        fullscreenToggleButton.setImageResource(R.drawable.my_bl_sdk_ic_video_fullscreen_minimize)
    }

    private fun setPortraitPlayerSize() {
        val displayWidth = UtilHelper.getScreenSize(this)?.x
        val height = calculateVideoHeight(displayWidth ?: 0, videoWidth, videoHeight)
        playerLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
    }

    private fun setLandscapePlayerSize() {
        playerLayout.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            mainLayout
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mainLayout).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            event?.let { scaleGestureDetector.onTouchEvent(it) }
        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configOrientation(newConfig.orientation)
    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            toggleOrientation()
        } else {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }

    override fun onDestroy() {
        exoPlayer?.release()
        exoPlayer = null
        LocalBroadcastManager.getInstance(applicationContext)
            .unregisterReceiver(MyBroadcastReceiver())
        super.onDestroy()
    }

    companion object {
        const val INTENT_KEY_THEME = "theme"
        const val INTENT_KEY_DATA_LIST = "data_list"
        const val INTENT_KEY_DATA = "data"
        const val INTENT_KEY_CONTENT_ID = "content_id"
        const val INTENT_KEY_POSITION = "currentPosition"
        const val INTENT_KEY_BACK_TO_MAIN = "back_to_main"
        const val LAST_PLAYED_TRACK = "last_track"
        const val LAST_PLAYED_POSITION = "last_position"
    }

    override fun openDialog(item: VideoModel) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val contentView =
            View.inflate(this, R.layout.my_bl_sdk_video_bottomsheet_three_dot_menu, null)
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val tvArtistName = bottomSheetDialog.findViewById<TextView>(R.id.desc)
        tvArtistName?.text = item.artist
        var image: ImageView? = bottomSheetDialog.findViewById(R.id.thumb)
       // var thumbCard: CardView? = bottomSheetDialog.findViewById(R.id.thumb_card)
        val title: TextView? = bottomSheetDialog.findViewById(R.id.name)
//        val layoutParams = FrameLayout.LayoutParams(300, 250)
//        image?.setLayoutParams(layoutParams)
         title?.text = item.title
         //thumbCard?.minimumHeight=LinearLayout.LayoutParams.WRAP_CONTENT
//        val height = LinearLayout.LayoutParams.WRAP_CONTENT
//        val parms = LinearLayout.LayoutParams(200,height)


            if (image != null) {
            Glide.with(this).load(UtilHelper.getImageUrlSize300(item.image!!)).into(image)

        }

        val downloadImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgDownload)
        val textViewDownloadTitle: TextView? = bottomSheetDialog.findViewById(R.id.tv_download)
        var isDownloaded = false
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
            if (isDownloaded) {
                cacheRepository.deleteDownloadById(item.contentID.toString())
                DownloadService.sendRemoveDownload(
                    applicationContext,
                    MyBLDownloadService::class.java, item.contentID.toString(), false
                )
                Log.e("TAG", "DELETED: " + isDownloaded)
                val localBroadcastManager =
                    LocalBroadcastManager.getInstance(applicationContext)
                val localIntent = Intent("DELETED")
                    .putExtra("contentID", item.contentID.toString())
                localBroadcastManager.sendBroadcast(localIntent)
                isDownloaded = false

            } else {
                val url = "${Constants.FILE_BASE_URL}${item.playUrl}"
                val downloadRequest: DownloadRequest =
                    url.toUri().let { it1 ->
                        DownloadRequest.Builder(item.contentID.toString(), it1)
                            .build()
                    }
                injector.downloadTitleMap[item.contentID.toString()] = item.title.toString()
                DownloadService.sendAddDownload(
                    applicationContext,
                    MyBLDownloadService::class.java,
                    downloadRequest,
                    /* foreground= */ false
                )
                if (cacheRepository.isDownloadCompleted(item.contentID.toString())
                        .equals(true)
                ) {
                    // if (cacheRepository.isDownloadCompleted().equals(true)) {
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
        val textViewWatchlaterTitle: TextView? =
            bottomSheetDialog.findViewById(R.id.txtwatchLater)
        var iswatched = false
        var watched = cacheRepository.getWatchedVideoById(item.contentID.toString())
        if (watched?.isWatched== 1) {
            iswatched = true
            watchlaterImage?.setImageResource(R.drawable.my_bl_sdk_watch_later_remove)
            watchIcon.setColorFilter(
                applicationContext.getResources().getColor(R.color.my_sdk_color_primary)
            )
            textViewWatchlaterTitle?.text = "Remove From Watchlater"
        } else {
            iswatched = false
            watchlaterImage?.setImageResource(R.drawable.my_bl_sdk_ic_watch_later)
            watchIcon.setColorFilter(
                applicationContext.getResources().getColor(R.color.my_sdk_down_item_desc)
            )
            textViewWatchlaterTitle?.text = "Watch Later"
        }

        val constraintWatchlater: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintAddtoWatch)
        constraintWatchlater?.setOnClickListener {
            if (iswatched.equals(true)) {
                cacheRepository.deleteWatchlaterById(item.contentID.toString())
                watchlaterImage?.setImageResource(R.drawable.my_bl_sdk_ic_watch_later)
                watchIcon.setColorFilter(
                    applicationContext.getResources().getColor(R.color.my_sdk_down_item_desc)
                )
                Log.e("TAG","DATA: "+ iswatched)
                iswatched= false
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
                watchlaterImage?.setImageResource(R.drawable.my_bl_sdk_watch_later_remove)
                watchIcon.setColorFilter(
                    applicationContext.getResources().getColor(R.color.my_sdk_color_primary)
                )

                iswatched = true
            }
            bottomSheetDialog.dismiss()
        }
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
        val constraintFav: ConstraintLayout? =
            bottomSheetDialog.findViewById(R.id.constraintFav)
        val favImage: ImageView? = bottomSheetDialog.findViewById(R.id.imgLike)
        val textFav: TextView? = bottomSheetDialog.findViewById(R.id.tvFav)
        var isFav = false
        val isAddedToFav = cacheRepository.getFavoriteById(item.contentID.toString())
        if (isAddedToFav?.fav == "1") {

            favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
            favImageView.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
            isFav = true
            textFav?.text = "Remove From favorite"
        } else {

            favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
            favImageView.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
            isFav = false
            textFav?.text = "Favorite"
        }
        constraintFav?.setOnClickListener {
            if (isFav.equals(true)) {
                favViewModel.deleteFavContent(item.contentID.toString(), "V")
                cacheRepository.deleteFavoriteById(item.contentID.toString())
                Toast.makeText(applicationContext, "Removed from favorite", Toast.LENGTH_LONG)
                    .show()
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_like)
                favImageView.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                isFav = false
            } else {
                favViewModel.addFavContent(item.contentID.toString(), "V")
                favImage?.setImageResource(R.drawable.my_bl_sdk_ic_icon_fav)
                favImageView.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                cacheRepository.insertFavSingleContent(
                    FavDataModel()
                        .apply {
                            content_Id = item.contentID.toString()
                            album_Id = item.albumId
                            albumImage = item.image
                            artistName = item.artist
                            artist_Id = item.artistId
                            clientValue = 2
                            content_Type = "V"
                            fav = "1"
                            imageUrl = item.image
                            playingUrl = item.playUrl
                            rootContentId = item.rootId
                            titleName = item.title
                            createDate = DateTime
                        }
                )
                isFav = true
                Toast.makeText(applicationContext, "Added to favorite", Toast.LENGTH_LONG)
                    .show()
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
        LocalBroadcastManager.getInstance(applicationContext)
            .registerReceiver(MyBroadcastReceiver(), intentFilter)
    }

    private fun progressIndicatorUpdate(downloadingItems: List<DownloadingItem>) {
        downloadingItems.forEach {
            val progressIndicator: CircularProgressIndicator? =
                videoRecyclerView.findViewWithTag(it.contentId)
            progressIndicator?.progress = it.progress.toInt()
            progressIndicator?.visibility = View.VISIBLE
            if (it.progress == 100f) {
                progressIndicator?.visibility = View.GONE
            }
        }
    }

    inner class MyBroadcastReceiver : BroadcastReceiver() {
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
                    adapter.notifyDataSetChanged()
                    Log.e("DELETED", "broadcast fired")
                }
                "PROGRESS" -> {
                    adapter.notifyDataSetChanged()
                    Log.e("PROGRESS", "broadcast fired")
                }
                else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
            }
        }
    }
}

internal interface BottomsheetDialog {
    fun openDialog(item: VideoModel)
}