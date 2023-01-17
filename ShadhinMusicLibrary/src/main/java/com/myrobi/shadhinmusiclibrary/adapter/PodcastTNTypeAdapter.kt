package com.myrobi.shadhinmusiclibrary.adapter

import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.di.Module
import com.myrobi.shadhinmusiclibrary.di.ShadhinApp
import com.myrobi.shadhinmusiclibrary.download.MyBLDownloadService
import com.myrobi.shadhinmusiclibrary.download.room.DownloadedContent
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.myrobi.shadhinmusiclibrary.library.player.Constants
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


internal class PodcastTNTypeAdapter(
    var patchItem: FeaturedPodcastDataModel,
    val podcastDetailsCallback: PodcastDetailsCallback,
    val cacheRepository: CacheRepository,
    val favViewModel: FavViewModel,
    val injector: Module
) : RecyclerView.Adapter<PodcastTNTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_pod_child_item_trending_now, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.item = patchItem.Data[position]
       podcastDetailsCallback.getCurrentVH(holder, patchItem.Data)
        holder.itemView.setOnClickListener {
          podcastDetailsCallback.onPodcastTrackClick(patchItem.Data,position)

        }


        val pd_download = holder.itemView.findViewById(R.id.pd_download) as ImageView
        var isDownloadComplete = false
        val downloaded = cacheRepository.getDownloadById(patchItem.Data[position].TracktId)
        if (downloaded?.getIsDownloaded()  == 1) {
            isDownloadComplete = true
            pd_download.setImageResource(R.drawable.my_bl_sdk_ic_download_round_red)
        } else {
            isDownloadComplete = false
            pd_download.setImageResource(R.drawable.my_bl_sdk_ic_download_round)
        }
        pd_download.setOnClickListener {

                if (isDownloadComplete == true) {
//                cacheRepository.deleteDownloadById(track.EpisodeId)
                    cacheRepository.deleteDownloadById(patchItem.Data[position].TracktId)
                    DownloadService.sendRemoveDownload(
                        holder.itemView.context,
                        MyBLDownloadService::class.java,
                        patchItem.Data[position].TracktId,
                        false
                    )
                    val localBroadcastManager =
                        LocalBroadcastManager.getInstance(holder.itemView.context)
                    val localIntent = Intent("DELETED")
                        .putExtra("contentID", patchItem.Data[position].TracktId)
//                    .putExtra("contentID", track.EpisodeId)
                    localBroadcastManager.sendBroadcast(localIntent)
                    isDownloadComplete = false
                    pd_download.setImageResource(R.drawable.my_bl_sdk_ic_download_round)
                } else {
                    val mPlayingUrl = "${Constants.FILE_BASE_URL}${patchItem.Data[position].PlayUrl}"
                    val downloadRequest: DownloadRequest =
                        DownloadRequest.Builder(patchItem.Data[position].TracktId, mPlayingUrl.toUri())
                            .build()
                   // Log.e("TAG", "NAME: " + iSongTrack.titleName + " URL " + mPlayingUrl)
                    injector.downloadTitleMap[patchItem.Data[position].TracktId ?: ""] = patchItem.Data[position].TrackName ?: ""
                    DownloadService.sendAddDownload(
                        holder.itemView.context,
                        MyBLDownloadService::class.java,
                        downloadRequest,
                        /* foreground= */ false
                    )

                    //Todo iSongTrack.EpisodeId
                    if (cacheRepository.isDownloadCompleted(patchItem.Data[position].TracktId)) {
                        pd_download.setImageResource(R.drawable.my_bl_sdk_ic_download_round_red)
                        isDownloadComplete = true
//                    val contentType = iSongTrack.content_Type
//                    val podcastType = contentType?.take(2)
//                    val  Type = contentType?.takeLast(2)
                        cacheRepository.insertDownload(
                            DownloadedContent().apply {
                                content_Id = patchItem.Data[position].TracktId
                                album_Id = patchItem.Data[position].EpisodeId
                                artist_Id = ""
                                rootContentId = patchItem.Data[position].TracktId
                                imageUrl = patchItem.Data[position].ImageUrl
                                titleName = patchItem.Data[position].TrackName
                                content_Type = patchItem.Data[position].ContentType
                                playingUrl = patchItem.Data[position].PlayUrl
                                rootContentType = patchItem.Data[position].ContentType
                                artistName = patchItem.Data[position].Presenter
                                total_duration = patchItem.Data[position].Duration
                            }
                        )

                    }
                }


        }
        val pd_favorite = holder.itemView.findViewById(R.id.pd_favorite) as ImageView
        var isFav = false
        //todo iSongTrack.Id
        val isAddedToFav = cacheRepository.getFavoriteById(patchItem.Data[position].TracktId)
        if (isAddedToFav?.content_Id != null) {
            pd_favorite.setImageResource(R.drawable.my_bl_sdk_ic_pd_favorite_added)
          //  pd_favorite.setColorFilter(holder.itemView.context.resources.getColor(R.color.my_sdk_color_primary))
            isFav = true
            //textFav?.text = "Remove From favorite"
        } else {

            pd_favorite.setImageResource(R.drawable.my_bl_sdk_ic_pd_favorite)
            isFav = false
            //textFav?.text = "Favorite"
        }
        pd_favorite.setOnClickListener {
                if (isFav.equals(true)) {
                    val contentType = patchItem.Data[position].ContentType
                    val podcastType = contentType.take(2)
                    val Type = contentType.takeLast(2)
                    //todo iSongTrack.Id
                    favViewModel.deleteFavContent(
                        patchItem.Data[position].TracktId,
                        patchItem.Data[position].ContentType
                    )
                    cacheRepository.deleteFavoriteById(patchItem.Data[position].TracktId)
                    Toast.makeText(
                       holder.itemView.context,
                        "Removed from favorite",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    pd_favorite.setImageResource(R.drawable.my_bl_sdk_ic_pd_favorite)
                    isFav = false

                    //Log.e("TAG", "NAME: " + iSongTrack.content_Type)
                } else {
                    val contentType = patchItem.Data[position].ContentType
                    val podcastType = contentType?.take(2)
                    val Type = contentType?.takeLast(2)
                    //todo iSongTrack.EpisodeId
                    favViewModel.addFavContent(
                        patchItem.Data[position].TracktId,
                        patchItem.Data[position].ContentType
                    )

                    // todo iSongTrack.Id.toString(),
                    //      iSongTrack.Id.toString(),
                    cacheRepository.insertFavSingleContent(
                        FavDataModel().apply {
                            content_Id = patchItem.Data[position].TracktId
                            album_Id = patchItem.Data[position].EpisodeId
                            albumImage = patchItem.Data[position].ImageUrl
                            clientValue = 2
                            content_Type =  patchItem.Data[position].ContentType
                            fav = "1"
                            artistName = patchItem.Data[position].Presenter
                            imageUrl = patchItem.Data[position].ImageUrl
                            playingUrl = patchItem.Data[position].PlayUrl
                            rootContentId = patchItem.Data[position].TracktId
                            rootContentType = patchItem.Data[position].ContentType
                            titleName = patchItem.Data[position].TrackName
                            total_duration = patchItem.Data[position].Duration
                        }
                    )
                    pd_favorite.setImageResource(R.drawable.my_bl_sdk_ic_pd_favorite_added)
                    isFav = true
                    Toast.makeText(holder.itemView.context, "Added to favorite", Toast.LENGTH_LONG)
                        .show()
                }
        }

    }

    override fun getItemCount(): Int {
        return patchItem.Data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        var ivPlayBtn: ImageView? = null
        var item: FeaturedPodcastDetailsModel? = null
        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.show_title) as TextView
            val textViewSubtitle = itemView.findViewById(R.id.sub_title) as TextView
            val show_des = itemView.findViewById(R.id.show_des) as TextView
            val txt_time =itemView.findViewById(R.id.txt_time) as TextView
            val imageView = itemView.findViewById(R.id.image) as ImageView
            ivPlayBtn = itemView.findViewById(R.id.pd_play)
            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize300(
                        patchItem.Data[absoluteAdapterPosition].ImageUrl ?: ""
                    )
                )
                .into(imageView)
            val result = Html.fromHtml(patchItem.Data[absoluteAdapterPosition].About).toString()
            textViewName.text = patchItem.Data[absoluteAdapterPosition].TrackName
            txt_time.text = patchItem.Data[absoluteAdapterPosition].Duration
            textViewSubtitle.text =patchItem.Data[absoluteAdapterPosition].Presenter
            show_des.text = result
            Log.e("TAG","DATA: "+ patchItem.Data.size)
            val pd_download = itemView.findViewById(R.id.pd_download) as ImageView
            val pd_favorite = itemView.findViewById(R.id.pd_favorite) as ImageView
        }
    }
}