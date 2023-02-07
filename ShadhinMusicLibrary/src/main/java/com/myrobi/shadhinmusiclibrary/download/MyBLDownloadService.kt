package com.myrobi.shadhinmusiclibrary.download

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.Download.STATE_COMPLETED
import com.google.android.exoplayer2.offline.Download.STATE_REMOVING
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Requirements
import com.google.android.exoplayer2.scheduler.Scheduler
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.activities.SDKMainActivity
import com.myrobi.shadhinmusiclibrary.data.model.DownloadingItem
import com.myrobi.shadhinmusiclibrary.di.ServiceEntryPoint
import com.myrobi.shadhinmusiclibrary.download.room.DownloadedContent
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.Constants.DOWNLOAD_CHANNEL_ID
import com.myrobi.shadhinmusiclibrary.library.player.Constants.SHOULD_OPEN_DOWNLOADS_ACTIVITY
import com.myrobi.shadhinmusiclibrary.library.player.data.source.MyBLDownloadManager
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import kotlinx.coroutines.*


private val NOTIFICATION_ID = 2

class MyBLDownloadService : DownloadService(
    1,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    "my app",
    R.string.dummy_content,
    R.string.dummy_content
), ServiceEntryPoint {

    private lateinit var notificationHelper: DownloadNotificationHelper
    private var currentItem: DownloadedContent? = null
    private lateinit var cacheRepository: CacheRepository
    private var downloadServiceScope: CoroutineScope? = null
    private val downloadCancelBroadcastListeners = DownloadCancelBroadcastListeners()

    companion object {
        var isRunning: Boolean = true
        var currentId: String? = null
        var currentProgress: Int? = null
    }



    override fun onCreate() {
        super.onCreate()
        isRunning = true
        cacheRepository = CacheRepository(applicationContext)
        notificationHelper = DownloadNotificationHelper(
            this.applicationContext,
            "my app",
            injector.downloadTitleMap,
            cacheRepository
        )

        registerReceiver(
            downloadCancelBroadcastListeners,
            IntentFilter(DownloadNotificationHelper.CANCEL_ACTION)
        )

    }

    override fun getDownloadManager(): DownloadManager {

        val myBlDownloadmanager = MyBLDownloadManager(applicationContext, injector.musicRepository)


        val manager = myBlDownloadmanager.downloadManager
        //Set the maximum number of parallel downloads
        manager.maxParallelDownloads = 5


        // manager.removeAllDownloads()

        manager.addListener(object : DownloadManager.Listener {
            override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
                Toast.makeText(applicationContext, "Deleted", Toast.LENGTH_SHORT).show()
                val localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
                   val localIntent = Intent("DELETED123")
                        .putExtra("contentID", download.request.id)
                  localBroadcastManager.sendBroadcast(localIntent)
            }

            override fun onDownloadsPausedChanged(
                downloadManager: DownloadManager,
                downloadsPaused: Boolean
            ) {
                if (downloadsPaused) {
                    Toast.makeText(applicationContext, "paused", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Download Started", Toast.LENGTH_SHORT)
                        .show()

                }


            }

            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: java.lang.Exception?,
            ) {
                super.onDownloadChanged(downloadManager, download, finalException)
                if (download.state == STATE_COMPLETED) {
                    currentProgress = 100
                    val localBroadcastManager =
                        LocalBroadcastManager.getInstance(applicationContext)
                    val localIntent = Intent("PROGRESS")
                        .putExtra("progress", 100)
                    localBroadcastManager.sendBroadcast(localIntent)
                    cacheRepository.downloadState(download.request.id, isDownloaded = true)
                    Log.e("DELETED", "STATE_REMOVING FIRED <--> "+download.request.id +" ${download.percentDownloaded}")
                }
//                else if(download.state == STATE_REMOVING){
//
//                  if (download.request.id.isEmpty()){
//                        return
//                    }
//
//                    cacheRepository.downloadState(download.request.id, isDownloaded = false)
                //     Log.e("DELETED", "STATE_REMOVING FIRED <--> "+download.request.id +" ${download.percentDownloaded}")
//                    val localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
//                    val localIntent = Intent("DELETEDXXX")
//                        .putExtra("contentID", download.request.id)
//                    localBroadcastManager.sendBroadcast(localIntent)
                // }

            }

            override fun onInitialized(downloadManager: DownloadManager) {
                super.onInitialized(downloadManager)
                Log.i("getDownloadManager", "onInitialized")
            }

            override fun onIdle(downloadManager: DownloadManager) {
                super.onIdle(downloadManager)
                Log.i("getDownloadManager", "onIdle")
            }

            override fun onRequirementsStateChanged(
                downloadManager: DownloadManager,
                requirements: Requirements,
                notMetRequirements: Int,
            ) {
                super.onRequirementsStateChanged(downloadManager, requirements, notMetRequirements)
                Log.i("getDownloadManager", "onRequirementsStateChanged")
            }

            override fun onWaitingForRequirementsChanged(
                downloadManager: DownloadManager,
                waitingForRequirements: Boolean,
            ) {
                super.onWaitingForRequirementsChanged(downloadManager, waitingForRequirements)
                Log.i("getDownloadManager", "onWaitingForRequirementsChanged")
            }


        })

        return manager
    }

    override fun getScheduler(): Scheduler? {
        return null
    }


    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int,
    ): Notification {
        Log.e(
            "getDownloadManagerx",
            "getForegroundNotification: ${downloads.map { it.percentDownloaded }}"
        )
        val downloadingItems = downloads.map {
            if (it.percentDownloaded in 0f..100f) {
                DownloadingItem(contentId = it.request.id, progress = it.percentDownloaded)
            } else {
                DownloadingItem(contentId = it.request.id, progress = 0f)
            }
        }
        val localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
        val localIntent = Intent("ACTION")
            .putParcelableArrayListExtra(
                "downloading_items",
                downloadingItems as ArrayList<out Parcelable>
            )
        localBroadcastManager.sendBroadcast(localIntent)

        return notificationHelper.buildProgressNotification(
            applicationContext,
            R.drawable.my_bl_sdk_ic_shadhin_icon_gray_vector,
            null,
            "",
            downloads,
            notMetRequirements,
        )

    }

    fun getAllDownloadCompleteNotification(): Notification {
        return NotificationCompat.Builder(applicationContext, DOWNLOAD_CHANNEL_ID)
            .setContentTitle("All Download Complete")
            .setShowWhen(false)
            .setOngoing(false)
            .setSmallIcon(R.drawable.my_bl_sdk_shadhin_logo_with_text_for_light)
            .setContentIntent(getPendingIntent())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    override fun onDestroy() {
        isRunning = false
        currentId = null
        currentProgress = 0
        downloadServiceScope?.coroutineContext?.cancelChildren()
        downloadServiceScope?.cancel()
        downloadServiceScope = null
        unregisterReceiver(downloadCancelBroadcastListeners)
        super.onDestroy()
    }

    internal fun saveDownloadedContent(downloadedContent: DownloadedContent) {
        downloadServiceScope?.launch {
            if (isRunning) {
                cacheRepository.setDownloadedContentPath(
                    downloadedContent.content_Id ?: "",
                    downloadedContent.playingUrl ?: ""
                )
                delay(Constants.DELAY_DURATION)
                // downloadUpdateListener?.loadData()
                withContext(Dispatchers.Main) {
                    currentItem = null
                    //DownloadProgressObserver.updateProgressForAllHolder()
                    Log.i(
                        "getDownloadManager",
                        "onDownloadChanged:" + cacheRepository.setDownloadedContentPath(
                            downloadedContent.content_Id ?: "",
                            downloadedContent.playingUrl ?: ""
                        )
                    )
                    // DownloadOrDeleteMp3Observer.notifySubscriber()
                }
            }
        }
    }

    fun getDownloadingNotification(title: String, progress: Int): Notification {
        return NotificationCompat.Builder(applicationContext, "my app")
            .setContentTitle("Downloading $title")
            .setContentText("$progress%")
            .setShowWhen(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.my_bl_sdk_shadhin_logo_with_text_for_light)
            .setContentIntent(getPendingIntent())
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.my_bl_sdk_shadhin_logo_with_text_for_light
                )
            )
            .setProgress(100, progress ?: 0, false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    fun getPendingIntent(): PendingIntent {

        var notificationIntent = Intent(this, SDKMainActivity::class.java)
        val arguments = Bundle()
        arguments.putBoolean(SHOULD_OPEN_DOWNLOADS_ACTIVITY, true)
        notificationIntent.putExtras(arguments)
        var uniqueReqCode: Int = (System.currentTimeMillis()).toInt()
        var pendingIntent = PendingIntent.getActivity(
            this, uniqueReqCode, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

    class DownloadCancelBroadcastListeners : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i("DATAARTISTd", "onReceive: ${intent?.action}")
            sendRemoveAllDownloads(
                context?.applicationContext!!,
                MyBLDownloadService::class.java,
                false
            )
        }
    }
}