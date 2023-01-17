package com.myrobi.shadhinmusiclibrary.adapter

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
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.myrobi.shadhinmusiclibrary.utils.TimeParser
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class PlaylistTrackAdapter(
    private val itemClickCB: CommonPlayControlCallback,
    private val bsDialogItemCallback: CommonBottomCallback,
    val cacheRepository: CacheRepository
) : RecyclerView.Adapter<PlaylistTrackAdapter.PlaylistTrackVH>() {

    var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_latest_music_view_item, parent, false)
        return PlaylistTrackVH(parentView!!)
    }

    override fun onBindViewHolder(holder: PlaylistTrackVH, position: Int) {
        val mSongDetails = dataSongDetail[position]
        holder.bindTrackItem(mSongDetails)
        holder.itemView.setOnClickListener {
            itemClickCB.onClickItem(dataSongDetail, position)
        }

        val ivSongMenuIcon: ImageView = holder.itemView.findViewById(R.id.iv_song_menu_icon)
        ivSongMenuIcon.setOnClickListener {
            val songDetail = dataSongDetail[position]
            bsDialogItemCallback.onClickBottomItem(songDetail)
            Log.e("TAG","DATA: "+ songDetail.content_Id)
            Log.e("TAG","DATA: "+ songDetail.titleName)
        }

        if (mSongDetails.isPlaying) {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(holder.mContext, R.color.my_sdk_color_primary)
            )
        } else {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(
                    holder.mContext,
                    R.color.my_sdk_black2
                )
            )
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return dataSongDetail.size
    }

    fun setData(
        data: MutableList<SongDetailModel>,
        rootPatch: HomePatchDetailModel,
        mediaId: String?
    ) {
        this.dataSongDetail = mutableListOf()
        for (songItem in data) {
            dataSongDetail.add(
                UtilHelper.getMixdUpIMusicWithRootData(
                    songItem.apply {
                        isSeekAble = true
                    }, rootPatch
                )
            )
        }

        if (mediaId != null) {
            setPlayingSong(mediaId)
        }

        notifyDataSetChanged()
    }

    fun setPlayingSong(mediaId: String) {
        val newList: List<IMusicModel> =
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, dataSongDetail)
        val callback = AnyTrackDiffCB(dataSongDetail, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        dataSongDetail.clear()
        dataSongDetail.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class PlaylistTrackVH(private val viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        val mContext = itemView.getContext()
        var tvSongName: TextView? = null
        fun bindTrackItem(mSongDetail: IMusicModel) {
            val sivSongIcon: ImageView = viewItem.findViewById(R.id.siv_song_icon)
            Glide.with(mContext)
                .load(mSongDetail.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(sivSongIcon)
            tvSongName = viewItem.findViewById(R.id.tv_song_name)
            tvSongName!!.text = mSongDetail.titleName

            val tvSingerName: TextView = viewItem.findViewById(R.id.tv_singer_name)
            tvSingerName.text = mSongDetail.artistName

            val tvSongLength: TextView = viewItem.findViewById(R.id.tv_song_length)
            tvSongLength.text = TimeParser.secToMin(mSongDetail.total_duration)
            val ivSongMenuIcon: ImageView = viewItem.findViewById(R.id.iv_song_menu_icon)
            val progressIndicator: CircularProgressIndicator = itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicator.tag = mSongDetail.content_Id
            progressIndicator.visibility = View.GONE
            downloaded.visibility = View.GONE
            val isDownloaded = cacheRepository.isTrackDownloaded(mSongDetail.content_Id.toString()) ?: false
            Log.e("TAG","DATA: "+ mSongDetail.content_Id)
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