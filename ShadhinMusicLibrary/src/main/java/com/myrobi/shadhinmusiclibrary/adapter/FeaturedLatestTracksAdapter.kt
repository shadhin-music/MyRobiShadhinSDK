package com.myrobi.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.LatestReleaseOnCallBack
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedSongDetailModel
import com.myrobi.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.myrobi.shadhinmusiclibrary.utils.TimeParser
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


internal class FeaturedLatestTracksAdapter(
    private val lrOnCallBack: LatestReleaseOnCallBack
) : RecyclerView.Adapter<FeaturedLatestTracksAdapter.ViewHolder>() {

    private var listSongDetail: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val v = LayoutInflater.from(parent.context)
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_release_item_list, parent, false)
        return ViewHolder(parentView!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mFeaturedSongDetail = listSongDetail[position]
        holder.bindItems(mFeaturedSongDetail)
        holder.itemView.setOnClickListener {
            lrOnCallBack.onClickItem(listSongDetail, position)
        }

        if (mFeaturedSongDetail.isPlaying) {
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
        return listSongDetail.size
    }

    fun setData(
        data: MutableList<FeaturedSongDetailModel>,
        /*   rootPatch: HomePatchDetail,*/
        mediaId: String?
    ) {
//        this.listSongDetail = mutableListOf()
        for (songItem in data) {
            listSongDetail.add(
                songItem.apply {
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
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, listSongDetail)
        val callback = AnyTrackDiffCB(listSongDetail, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        listSongDetail.clear()
        listSongDetail.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        var tvSongName: TextView? = null
        fun bindItems(mSongDetails: IMusicModel) {
            val imageView: ShapeableImageView? = itemView.findViewById(R.id.siv_song_icon)
            val textArtist: TextView = itemView.findViewById(R.id.tv_singer_name)
            val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            tvSongName = itemView.findViewById(R.id.tv_song_name)
            Glide.with(context)
                .load(mSongDetails.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView!!)
            tvSongName?.text = mSongDetails.titleName
            Log.e("TAG","Song: "+ mSongDetails.titleName )
            textArtist.text = mSongDetails.artistName
            textDuration.text = TimeParser.secToMin(mSongDetails.total_duration)
        }
    }
}