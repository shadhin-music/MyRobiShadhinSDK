package com.myrobi.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.PodcastBottomSheetDialogItemCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.SongTrackModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class PodcastTrackAdapter(
    private val itemClickCB: CommonPlayControlCallback,
    private val bsDialogItemCallback: PodcastBottomSheetDialogItemCallback,
    val cacheRepository: CacheRepository
) : RecyclerView.Adapter<PodcastTrackAdapter.PodcastTrackVH>() {
    var tracks: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastTrackVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_podcast_episodes_item, parent, false)
        return PodcastTrackVH(v)
    }

    override fun onBindViewHolder(holder: PodcastTrackVH, position: Int) {
        val trackItem = tracks[position]
        holder.bindItems(trackItem)
        holder.itemView.setOnClickListener {
            itemClickCB.onClickItem(tracks, position)
            Log.e("TAG", "DATA: " + trackItem?.trackType)
        }

        val ivSongMenuIcon: ImageView = holder.itemView.findViewById(R.id.iv_song_menu_icon)
        val ivSongLiveIcon: ImageView = holder.itemView.findViewById(R.id.iv_song_live_icon)
        if(trackItem?.trackType?.toUpperCase()=="LM"){
            ivSongMenuIcon.visibility = GONE
            ivSongLiveIcon.visibility = VISIBLE
        }else{
            ivSongMenuIcon.visibility = VISIBLE
            ivSongLiveIcon.visibility = GONE
            ivSongMenuIcon.setOnClickListener {
                bsDialogItemCallback.onClickBottomItem(trackItem)
            }
        }

        if (trackItem.isPlaying) {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(holder.context, R.color.my_sdk_color_primary)
            )
        } else {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(
                    holder.context,
                    R.color.my_sdk_black2
                )
            )
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setData(
        songTrack: MutableList<SongTrackModel>,
        rootPatch: HomePatchDetailModel,
        mediaId: String?
    ) {
        this.tracks = mutableListOf()
        for (songItem in songTrack) {
            tracks.add(
                songItem.apply {
                    rootContentId = episodeId
                    rootContentType = rootPatch.content_Type
                    rootImage = rootPatch.imageUrl
                    isSeekAble = true
                }
            )
        }
        if (mediaId != null) {
            setPlayingSong(mediaId)
        }

        notifyDataSetChanged()
    }

    fun setPlayingSong(mediaId: String) {
        val newList: List<IMusicModel> =
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, tracks)
        val callback = AnyTrackDiffCB(tracks, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        tracks.clear()
        tracks.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class PodcastTrackVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()

        var tvSongName: TextView? = null
        fun bindItems(iMusicModel: IMusicModel) {
            val image: ShapeableImageView = itemView.findViewById(R.id.siv_song_icon)
            val textArtistName: TextView = itemView.findViewById(R.id.tv_singer_name)
            val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            textDuration.text = iMusicModel.total_duration
            textArtistName.text = iMusicModel.artistName
            Glide.with(context)
                .load(iMusicModel.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(image)
            tvSongName = itemView.findViewById(R.id.tv_song_name)
            tvSongName?.text = iMusicModel.titleName

            val progressIndicator: CircularProgressIndicator = itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicator.tag = iMusicModel.content_Id
            progressIndicator.visibility = View.GONE
            downloaded.visibility = View.GONE
            val isDownloaded =
                cacheRepository.isTrackDownloaded(iMusicModel.content_Id) ?: false

            if (isDownloaded) {
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 2
    }
}