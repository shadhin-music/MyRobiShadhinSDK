package com.myrobi.shadhinmusiclibrary.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.activities.video.BottomsheetDialog
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.TimeParser
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal typealias VideoItemClickFunc = (VideoModel, isMenuClick: Boolean) -> Unit

internal class VideoAdapter(
    private val context: Context,
    val bottomsheetDialog: BottomsheetDialog,
    val cacheRepository: CacheRepository
) :
    ListAdapter<VideoModel, RecyclerView.ViewHolder>(
        VideoDiffCallBack()
    ) {
    val layoutManager: GridLayoutManager = GridLayoutManager(context, 1)
    val isList: Boolean
        get() = layoutManager.spanCount == 1
    val isGrid: Boolean
        get() = layoutManager.spanCount == 2
    private var videoItemClickFunc: VideoItemClickFunc? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.LIST.ordinal -> crateListViewHolder(parent)
            ViewType.GRID.ordinal -> crateGridViewHolder(parent)
            else -> crateListViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
                holder.bind(getItem(position))
            }
            is GridViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    fun changeToList() {
        if (isGrid) {
            layoutManager.spanCount = 1
            notifyItemRangeChanged(0, itemCount)
        }
    }

    fun changeToGrid() {
        if (isList) {
            layoutManager.spanCount = 2
            notifyItemRangeChanged(0, itemCount)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isList -> ViewType.LIST.ordinal
            isGrid -> ViewType.GRID.ordinal
            else -> ViewType.LIST.ordinal
        }
    }

    fun onItemClickListeners(videoItemClickFunc: VideoItemClickFunc) {
        this.videoItemClickFunc = videoItemClickFunc
    }

    private fun crateListViewHolder(parent: ViewGroup): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.my_bl_sdk_row_video_li, parent, false)
        return ListViewHolder(view)
    }

    private fun crateGridViewHolder(parent: ViewGroup): GridViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.my_bl_sdk_row_video_gr, parent, false)
        return GridViewHolder(view)
    }

    fun currentItem(playing: Boolean, mediaId: String?) {
        val newItem = currentList.map {
            val tItem = it.copy()
            if (it.contentID == mediaId) {
                tItem.isPlaying = true
                tItem.isPlaystate = playing
            } else {
                tItem.isPlaying = false
                tItem.isPlaystate = false
            }
            tItem
        }
        submitList(newItem)
    }

    inner class ListViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleTextView: TextView = itemView.findViewById(R.id.videoTitle)
        private var subTitleTextView: TextView = itemView.findViewById(R.id.videoDesc)
        private var durationTextView: TextView = itemView.findViewById(R.id.video_duration)
        private var playPauseImage: ImageView = itemView.findViewById(R.id.play_pause)
        private var videoImage: ImageView = itemView.findViewById(R.id.videoImage)
        private var threeDotButton: ImageButton = itemView.findViewById(R.id.threeDotButton)

        fun bind(item: VideoModel) {
            titleTextView.text = item.title
//            durationTextView.text = createTimeLabel(item.duration?.toLong() ?: 0L)
            durationTextView.text = TimeParser.secToMin(item.duration)
            if (item.isPlaying) {
                titleTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.my_sdk_color_primary
                    )
                )
            } else {
                titleTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.my_sdk_down_title
                    )
                )
            }
            subTitleTextView.text = item.artist
            if (item?.image?.contains("size")==true){
                Glide.with(itemView.context)
                    .load(UtilHelper.getImageUrlSize450(item?.image!!))
                    .placeholder(R.drawable.my_bl_sdk_default_video)
                    .into(videoImage)
            }
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.my_bl_sdk_default_video)
                .into(videoImage)

            if (item.isPlaystate) {
                playPauseImage.setImageResource(R.drawable.my_bl_sdk_ic_pause_n)
            } else {
                playPauseImage.setImageResource(R.drawable.my_bl_sdk_ic_play_n)
            }
            itemView.setOnClickListener {
                videoItemClickFunc?.invoke(getItem(absoluteAdapterPosition), false)
            }
            threeDotButton.setOnClickListener {
                bottomsheetDialog.openDialog(item)
            }
            val progressIndicator: CircularProgressIndicator = itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicator.tag = item.contentID
            progressIndicator.visibility = View.GONE
            downloaded.visibility = View.GONE
//            downloaded.tag = 200
            val isDownloaded = cacheRepository.isVideoDownloaded(item.contentID)
            if (isDownloaded) {
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
                progressIndicator.layoutParams = LinearLayout.LayoutParams(0, 0)
            } else {
                downloaded.visibility = View.GONE
                progressIndicator.visibility = View.GONE
                //  progressIndicator.layoutParams = LinearLayout.LayoutParams(10.dp,10.dp)
            }
        }
    }

    inner class GridViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleTextView: TextView = itemView.findViewById(R.id.videoTitle)
        private var subTitleTextView: TextView = itemView.findViewById(R.id.videoDesc)
        private var playPauseImage: ImageView = itemView.findViewById(R.id.play_pause)
        private var videoImage: ImageView = itemView.findViewById(R.id.videoImage)

        fun bind(item: VideoModel) {
            titleTextView.text = item.title
            if (item.isPlaying) {
                titleTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.my_sdk_color_primary
                    )
                )
            } else {
                titleTextView.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.my_sdk_down_title
                    )
                )
            }
            subTitleTextView.text = item.artist

            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.my_bl_sdk_default_video)
                .into(videoImage)


            if (item.isPlaystate) {
                playPauseImage.setImageResource(R.drawable.my_bl_sdk_ic_pause_n)
            } else {
                playPauseImage.setImageResource(R.drawable.my_bl_sdk_ic_play_n)
            }
            itemView.setOnClickListener {
                videoItemClickFunc?.invoke(getItem(absoluteAdapterPosition), false)
            }
        }
    }

    enum class ViewType {
        LIST,
        GRID
    }
}

internal class VideoDiffCallBack : DiffUtil.ItemCallback<VideoModel>() {
    override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
        return oldItem.contentID == newItem.contentID
    }

    override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
        return oldItem == newItem
    }
}