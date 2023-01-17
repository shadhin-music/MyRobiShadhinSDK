package com.myrobi.shadhinmusiclibrary.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistAlbumModelData
import com.myrobi.shadhinmusiclibrary.utils.TimeParser
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


internal class ArtistSpecificAlbumAdapter() :
    RecyclerView.Adapter<ArtistSpecificAlbumAdapter.DataAdapterViewHolder>() {
    private var rootDataContent: ArtistAlbumModelData? = null
    private var dataSongDetail: List<SongDetailModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            VIEW_ALBUM -> R.layout.my_bl_sdk_playlist_header
            VIEW_TRACK_ITEM -> R.layout.my_bl_sdk_latest_music_view_item
            else -> throw IllegalArgumentException("Invalid view type")
        }
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return DataAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> holder.bindRoot(rootDataContent!!)
            1 -> holder.bindTrackItem(dataSongDetail[position - 1])
        }
//        holder.bind(dataContent[position])
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
    fun setData(data: List<SongDetailModel>) {
        this.dataSongDetail = data
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRootData(data: ArtistAlbumModelData?) {
        this.rootDataContent = data
        notifyDataSetChanged()
    }

    inner class DataAdapterViewHolder(private val viewItem: View) :
        RecyclerView.ViewHolder(viewItem) {
        val mContext = viewItem.context
        private lateinit var ivThumbCurrentPlayItem: ImageView
        private lateinit var tvCurrentAlbumName: TextView
        private lateinit var tvArtistName: TextView

        //        private lateinit var ivFavorite: ImageView
        private lateinit var ivPlayBtn: ImageView

        //        private lateinit var ivShareBtnFab: ImageView
        fun bindRoot(root: ArtistAlbumModelData) {
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

        fun bindTrackItem(mSongDetail: SongDetailModel) {
            val sivSongIcon: ImageView = viewItem.findViewById(R.id.siv_song_icon)
            Glide.with(mContext)
                .load(UtilHelper.getImageUrlSize300(mSongDetail.imageUrl ?: ""))
                .into(sivSongIcon)
            val tvSongName: TextView = viewItem.findViewById(R.id.tv_song_name)
            tvSongName.text = mSongDetail.titleName

            val tvSingerName: TextView = viewItem.findViewById(R.id.tv_singer_name)
            tvSingerName.text = mSongDetail.artistName

            val tvSongLength: TextView = viewItem.findViewById(R.id.tv_song_length)
            tvSongLength.text = TimeParser.secToMin(mSongDetail.total_duration)
            val ivSongMenuIcon: ImageView = viewItem.findViewById(R.id.iv_song_menu_icon)
            ivSongMenuIcon.setOnClickListener {
//                showBottomSheetDialog(viewItem.context)
            }
        }

        private fun showBottomSheetDialog(context: Context) {
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
            val contentView =
                View.inflate(context, R.layout.my_bl_sdk_bottomsheet_three_dot_menu_layout, null)
            bottomSheetDialog.setContentView(contentView)
            bottomSheetDialog.show()
        }
    }

    private companion object {
        const val VIEW_ALBUM = 0
        const val VIEW_TRACK_ITEM = 1
    }
}