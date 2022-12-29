package com.shadhinmusiclibrary.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.SearchItemCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.model.search.CommonSearchData
import com.shadhinmusiclibrary.utils.UtilHelper


internal class SearchVideoAdapter(
    private val seaItemCallback: SearchItemCallBack
) : RecyclerView.Adapter<SearchVideoAdapter.ViewHolder>() {
    private var searchVideodata: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_search_video_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(searchVideodata[position])
        holder.itemView.setOnClickListener {
            seaItemCallback.onClickPlayVideoItem(searchVideodata, position)
        }
    }

    override fun getItemCount(): Int {
        return searchVideodata.size

    }

    fun setSearchVideo(
        rootModel: CommonSearchData
    ) {
        this.searchVideodata = UtilHelper.getSearchDataToRootData(rootModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(searchVideodata: IMusicModel) {
            val imageView: ImageView = itemView.findViewById(R.id.video_thumb)
            val textTitle: TextView = itemView.findViewById(R.id.song_name)
            Glide.with(context)
                .load(searchVideodata.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
            val textArtist: TextView = itemView.findViewById(R.id.artist_name)
            //  val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            textTitle.text = searchVideodata.titleName
            textArtist.text = searchVideodata.artistName
        }
    }
}






