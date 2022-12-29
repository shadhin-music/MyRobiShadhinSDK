package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.search.TopTrendingDataModel
import com.shadhinmusiclibrary.utils.UtilHelper

internal class TrendingItemsAdapter(var data: List<TopTrendingDataModel>) :
    RecyclerView.Adapter<TrendingItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_layout_search_top_trending_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(data, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: List<IMusicModel>, position: Int) {
            val context = itemView.context
            val songImage: ImageView = itemView.findViewById(R.id.song_img)
            val songName: TextView = itemView.findViewById(R.id.song_name)
            val artistName: TextView = itemView.findViewById(R.id.artist_name)
            val url: String? = data[position].imageUrl
            Glide.with(context).load(url?.let { UtilHelper.getImageUrlSize300(it) }).into(songImage)
            songName.text = data[position].titleName
            artistName.text = data[position].artistName
        }


    }

}