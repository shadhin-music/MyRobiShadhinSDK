package com.shadhinmusiclibrary.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.video.VideoActivity
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.VideoModel
import com.shadhinmusiclibrary.download.room.WatchLaterContent
import com.shadhinmusiclibrary.fragments.WatchlaterBottomSheetDialogItemCallback
import com.shadhinmusiclibrary.utils.TimeParser

internal class WatchlaterAdapter(
    val onItemClickCallback: WatchlaterBottomSheetDialogItemCallback
) : RecyclerView.Adapter<WatchlaterAdapter.ViewHolder>() {
    private var allwatchlater: MutableList<WatchLaterContent> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_download_songs_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, VideoActivity::class.java)
            val videoArray = ArrayList<VideoModel>()
            for (item in allwatchlater) {
                val video = VideoModel()
                video.setDataWatchlater(item)
                videoArray.add(video)
            }
            val videos: ArrayList<VideoModel> = videoArray
            intent.putExtra(VideoActivity.INTENT_KEY_POSITION, position)
            intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videos)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allwatchlater.size
    }

    fun setData(allWatchlater: MutableList<WatchLaterContent>?) {
        if (allWatchlater != null) {
            allwatchlater = allWatchlater
            notifyDataSetChanged()
        }
    }

    fun updateData(allWatchlaterContent: List<WatchLaterContent>?) {
        allwatchlater.clear()
        allWatchlaterContent?.let { allwatchlater.addAll(it) }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.getContext()
        fun bindItems() {

            val sivSongIcon: ImageView = itemView.findViewById(R.id.siv_song_icon)
            Glide.with(context)
                .load(allwatchlater[absoluteAdapterPosition].getImageUrl300Size())
                .into(sivSongIcon)
            val tvSongName: TextView = itemView.findViewById(R.id.tv_song_name)
            tvSongName.text = allwatchlater[absoluteAdapterPosition].rootTitle

            val tvSingerName: TextView = itemView.findViewById(R.id.tv_singer_name)
            tvSingerName.text = allwatchlater[absoluteAdapterPosition].artist

            val tvSongLength: TextView = itemView.findViewById(R.id.tv_song_length)
            tvSongLength.text =
                TimeParser.secToMin(allwatchlater[absoluteAdapterPosition].timeStamp)
            val menu: ImageView = itemView.findViewById(R.id.iv_song_menu_icon)
            menu.setOnClickListener {
                onItemClickCallback.onClickBottomItemVideo(allwatchlater[absoluteAdapterPosition])
            }
        }
    }
}