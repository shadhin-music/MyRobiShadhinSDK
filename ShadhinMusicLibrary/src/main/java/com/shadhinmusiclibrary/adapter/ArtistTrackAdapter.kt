package com.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.ArtistContentDataModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class ArtistTrackAdapter(
    private val itemClickCB: CommonPlayControlCallback,
    val bottomSheetDialogItemCallback: CommonBottomCallback,
    val cacheRepository: CacheRepository?
) : RecyclerView.Adapter<ArtistTrackAdapter.ArtistTrackVH>() {
    var artistSongList: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistTrackVH {
//        val v = LayoutInflater.from(parent.context)
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_video_podcast_epi_single_item, parent, false)
        return ArtistTrackVH(parentView!!)
    }

    override fun onBindViewHolder(holder: ArtistTrackVH, position: Int) {
        val mArtistConData = artistSongList[position]
        holder.bindItems(mArtistConData)

        holder.itemView.setOnClickListener {
            itemClickCB.onClickItem(artistSongList, position)
        }
        val ivSongMenuIcon: ImageView = holder.itemView.findViewById(R.id.iv_song_menu_icon)
        ivSongMenuIcon.setOnClickListener {
            val artistContent = artistSongList[position]
            bottomSheetDialogItemCallback.onClickBottomItem(
                artistContent
            )
        }
        if (mArtistConData.isPlaying) {
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
        return artistSongList.size
    }

    fun setArtistTrack(
        data: MutableList<ArtistContentDataModel>,
        rootPatch: HomePatchDetailModel,
        mediaId: String?
    ) {
        this.artistSongList = mutableListOf()
        this.artistSongList.clear()
        for (songItem in data) {
            Log.e("ATA", "setArtistTrack: " + rootPatch.content_Id + " " + songItem.rootContentId)
            artistSongList.add(
                UtilHelper.getMixdUpIMusicWithRootData(songItem, rootPatch)
            )
        }

        if (mediaId != null) {
            setPlayingSong(mediaId)
        }

        notifyDataSetChanged()
    }

    fun setPlayingSong(mediaId: String) {
        val newList: List<IMusicModel> =
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, artistSongList)
        val callback = AnyTrackDiffCB(artistSongList, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        artistSongList.clear()
        artistSongList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ArtistTrackVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        var tvSongName: TextView? = null
        fun bindItems(artistContent: IMusicModel) {
            val imageView: ShapeableImageView? = itemView.findViewById(R.id.siv_song_icon)
            Glide.with(context)
                .load(artistContent.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView!!)
            tvSongName = itemView.findViewById(R.id.tv_song_name)
            val textArtist: TextView = itemView.findViewById(R.id.tv_singer_name)
            val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            tvSongName?.text = artistContent.titleName
            textArtist.text = artistContent.artistName
            textDuration.text = TimeParser.secToMin(artistContent.total_duration)

            val progressIndicatorArtist: CircularProgressIndicator =
                itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicatorArtist.tag = artistContent.content_Id
            downloaded.tag = 200
            progressIndicatorArtist.visibility = View.GONE
            downloaded.visibility = View.GONE
            val isDownloaded =
                cacheRepository?.isTrackDownloaded(artistContent.content_Id) ?: false
            if (isDownloaded) {
                downloaded.visibility = View.VISIBLE
                progressIndicatorArtist.visibility = View.GONE
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 2
    }
}