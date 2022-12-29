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
import com.shadhinmusiclibrary.callBackService.CommonSingleCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class QueueTrackAdapter(private val mItemClick: CommonSingleCallback) :
    RecyclerView.Adapter<QueueTrackAdapter.QueueTrackVH>() {

    var queueSongList: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QueueTrackVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_video_podcast_epi_single_item, parent, false)
        return QueueTrackVH(parentView!!)
    }

    override fun onBindViewHolder(holder: QueueTrackVH, position: Int) {
        val mQueueSongItem = queueSongList[position]
        holder.bindItems(mQueueSongItem)

        holder.itemView.setOnClickListener {
            mItemClick.onClickItem(mQueueSongItem, position)
        }

        if (mQueueSongItem.isPlaying) {
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

    override fun getItemCount(): Int {
        return queueSongList.size
    }

    fun setQueueTrack(
        data: MutableList<IMusicModel>,
        mediaId: String?
    ) {
        for (songItem in data) {
//            artistSongList.add(
            Log.e("QTA", "setQueueTrack: " + songItem.artistName + " " + songItem.total_duration)
//            )
        }
        queueSongList = data

        if (mediaId != null) {
            setPlayingSong(mediaId)
        }

        notifyDataSetChanged()
    }

    fun setPlayingSong(mediaId: String) {
        val newList: List<IMusicModel> =
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, queueSongList)
        val callback = AnyTrackDiffCB(queueSongList, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        queueSongList.clear()
        queueSongList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class QueueTrackVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            progressIndicatorArtist.visibility = View.GONE
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            downloaded.visibility = View.GONE
            val ivSongMenuIcon: ImageView = itemView.findViewById(R.id.iv_song_menu_icon)
            ivSongMenuIcon.visibility = View.GONE
        }
    }
}