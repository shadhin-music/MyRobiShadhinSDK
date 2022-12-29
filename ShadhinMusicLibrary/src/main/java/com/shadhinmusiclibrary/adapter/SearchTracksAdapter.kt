package com.shadhinmusiclibrary.adapter

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
import com.shadhinmusiclibrary.data.model.search.SearchDataModel
import com.shadhinmusiclibrary.utils.UtilHelper

internal class SearchTracksAdapter(
    private val seaItemCallback: SearchItemCallBack
) : RecyclerView.Adapter<SearchTracksAdapter.SearchTrackVH>() {
    private var searchTrackData: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_search_track_layout, parent, false)
        return SearchTrackVH(v)
    }

    override fun onBindViewHolder(holder: SearchTrackVH, position: Int) {
        holder.bindItems(searchTrackData[position])
        holder.itemView.setOnClickListener {
            seaItemCallback.onClickPlaySearchItem(searchTrackData, position)
        }
    }

    override fun getItemCount(): Int {
        return searchTrackData.size
    }

    fun setSearchTrackData(dataSongDetail: MutableList<SearchDataModel>) {
        this.searchTrackData = UtilHelper.getIMusicModelAndRootData(dataSongDetail)
    }

    inner class SearchTrackVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(searchTrackdata: IMusicModel) {
            val imageView: ImageView = itemView.findViewById(R.id.thumb)
            val textTitle: TextView = itemView.findViewById(R.id.title)
            Glide.with(context)
                .load(searchTrackdata.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
            val textArtist: TextView = itemView.findViewById(R.id.similarArtist)
            //  val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            textTitle.text = searchTrackdata.titleName
            textArtist.text = searchTrackdata.artistName
        }
    }
}






