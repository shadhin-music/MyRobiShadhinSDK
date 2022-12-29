package com.shadhinmusiclibrary.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.SearchItemCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.model.search.CommonSearchData
import com.shadhinmusiclibrary.data.model.search.SearchDataModel
import com.shadhinmusiclibrary.utils.CircleImageView
import com.shadhinmusiclibrary.utils.UtilHelper


internal class SearchArtistAdapter(
    val searchCallBack: SearchItemCallBack
) : RecyclerView.Adapter<SearchArtistAdapter.ViewHolder>() {
    private var searchArtistdata: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_search_artist_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val seaDataItem = searchArtistdata[position]
        holder.bindItems(seaDataItem)

        holder.itemView.setOnClickListener {
            searchCallBack.onClickSearchItem(seaDataItem)
        }
    }

    override fun getItemCount(): Int {
        return searchArtistdata.size
    }

    fun setSearchArtist(rootModel: CommonSearchData) {
        this.searchArtistdata = UtilHelper.getSearchDataToRootData(rootModel)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(artistDetails: IMusicModel) {
            val imageView: CircleImageView = itemView.findViewById(R.id.artist_img)
            Glide.with(context)
                .load(artistDetails.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
            val textArtist: TextView = itemView.findViewById(R.id.artist_name)
            textArtist.text = artistDetails.artistName
        }
    }
}