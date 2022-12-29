package com.shadhinmusiclibrary.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class PlaylistAdapter(
    private val itemClickCB: CommonPlayControlCallback,
    private val bsDialogItemCallback: CommonBottomCallback,
    val cacheRepository: CacheRepository
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistVH>() {

    private var rootDataContent: HomePatchDetailModel? = null
    private var dataSongDetail: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistVH {
        val layout = when (viewType) {
            VIEW_ALBUM -> R.layout.my_bl_sdk_playlist_header
            VIEW_TRACK_ITEM -> R.layout.my_bl_sdk_latest_music_view_item
            else -> throw IllegalArgumentException("Invalid view type")
        }
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return PlaylistVH(view)
    }

    override fun onBindViewHolder(holder: PlaylistVH, position: Int) {
        when (holder.itemViewType) {
            0 -> holder.bindRoot(rootDataContent!!)
            1 -> holder.bindTrackItem(dataSongDetail[position - 1])
        }

        holder.itemView.setOnClickListener {
            if (holder.itemViewType == VIEW_TRACK_ITEM) {
                val mSongDetItem = dataSongDetail[position]

            }
        }

        if (dataSongDetail.isNotEmpty()) {
            // val mSongDetItem = dataSongDetail[position-1]
            when (holder.itemViewType) {
                0 -> {
//                    itemClickCB.getCurrentVH(holder, dataSongDetail)
                    holder.ivPlayBtn?.setOnClickListener {
                        /*
                        if player.state == playing && player.rootid == this.rootId{
                            player.pause
                        }else if player.state = paused && player.rootid == this.rootId{
                            player.play
                        }else{
                            this.startPlaying(trackArray, index = 0)
                        }
                         */
                        itemClickCB.onClickItem(dataSongDetail, position)
                    }
                }
                1 -> {
//                    itemClickCB.getCurrentVH(holder, dataSongDetail)
                    holder.itemView.setOnClickListener {
                        if (holder.itemViewType == PlaylistAdapter.VIEW_TRACK_ITEM) {
                            val mSongDetItem = dataSongDetail[position - 1]
                            itemClickCB.onClickItem(dataSongDetail, (position - 1))
                            //holder.tvSongName?.setTextColor(Color.BLUE)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        var count = dataSongDetail.size
        if (rootDataContent != null) {
            count += 1
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_ALBUM
            else -> {
                VIEW_TRACK_ITEM
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSongData(data: MutableList<SongDetailModel>) {
//        this.dataSongDetail = data
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRootData(data: HomePatchDetailModel?) {
        this.rootDataContent = data
        notifyDataSetChanged()
    }

    inner class PlaylistVH(private val viewItem: View) :
        RecyclerView.ViewHolder(viewItem) {
        val mContext = viewItem.context
        private lateinit var ivThumbCurrentPlayItem: ImageView
        private lateinit var tvCurrentAlbumName: TextView
        private lateinit var tvArtistName: TextView

        //        private lateinit var ivFavorite: ImageView
        var ivPlayBtn: ImageView? = null

        //        private lateinit var ivShareBtnFab: ImageView
        fun bindRoot(root: HomePatchDetailModel) {
            ivThumbCurrentPlayItem =
                viewItem.findViewById(R.id.iv_thumb_current_play_item)
            Glide.with(mContext)
                .load(UtilHelper.getImageUrlSize300(root.imageUrl!!))
                .into(ivThumbCurrentPlayItem)
            tvCurrentAlbumName =
                viewItem.findViewById(R.id.tv_current_album_name)
            tvCurrentAlbumName.text = root.titleName

            tvArtistName =
                viewItem.findViewById(R.id.tv_artist_name)
            tvArtistName.text = root.artistName

//            ivFavorite = viewItem.findViewById(R.id.iv_favorite)
            ivPlayBtn = viewItem.findViewById(R.id.iv_play_btn)
//            ivShareBtnFab = viewItem.findViewById(R.id.iv_share_btn_fab)
        }

        var tvSongName: TextView? = null
        fun bindTrackItem(mSongDetail: IMusicModel) {
            val sivSongIcon: ImageView = viewItem.findViewById(R.id.siv_song_icon)
            Glide.with(mContext)
                .load(mSongDetail.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(sivSongIcon)
            tvSongName = viewItem.findViewById(R.id.tv_song_name)
            tvSongName?.text = mSongDetail.titleName

            val tvSingerName: TextView = viewItem.findViewById(R.id.tv_singer_name)
            tvSingerName.text = mSongDetail.artistName

            val tvSongLength: TextView = viewItem.findViewById(R.id.tv_song_length)
            tvSongLength.text = TimeParser.secToMin(mSongDetail.total_duration)
            val ivSongMenuIcon: ImageView = viewItem.findViewById(R.id.iv_song_menu_icon)
            ivSongMenuIcon.setOnClickListener {
                bsDialogItemCallback.onClickBottomItem(mSongDetail)
            }

            val progressIndicator1: CircularProgressIndicator = itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicator1.tag = mSongDetail.content_Id
            progressIndicator1.visibility = View.GONE
            downloaded.visibility = View.GONE
            val isDownloaded = cacheRepository.isTrackDownloaded(mSongDetail.content_Id) ?: false
            if (isDownloaded) {
                downloaded.visibility = View.VISIBLE
                progressIndicator1.visibility = View.GONE
            }
        }
    }

    private companion object {
        const val VIEW_ALBUM = 0
        const val VIEW_TRACK_ITEM = 1
    }
}