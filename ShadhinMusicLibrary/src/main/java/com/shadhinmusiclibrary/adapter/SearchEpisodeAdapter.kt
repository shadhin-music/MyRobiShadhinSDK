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
import com.shadhinmusiclibrary.data.model.search.CommonSearchData
import com.shadhinmusiclibrary.utils.UtilHelper


internal class SearchEpisodeAdapter(
    private val seaItemCallback: SearchItemCallBack
) : RecyclerView.Adapter<SearchEpisodeAdapter.ViewHolder>() {
    private var searchEpisodedata: List<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_search_album_layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchData = searchEpisodedata[position]
        holder.bindItems(searchData)
        holder.itemView.setOnClickListener {
            seaItemCallback.onClickSearchItem(searchData)
        }
    }

    override fun getItemCount(): Int {
        return searchEpisodedata.size

    }

    fun setSearchPodcastEpisode(
        rootModel: CommonSearchData
    ) {
        this.searchEpisodedata = UtilHelper.getSearchDataToRootData(rootModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(searchEpisodedata: IMusicModel) {
            val imageView: ImageView = itemView.findViewById(R.id.thumb)
            val textTitle: TextView = itemView.findViewById(R.id.title)
            Glide.with(context)
                .load(searchEpisodedata.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
            val textArtist: TextView = itemView.findViewById(R.id.similarArtist)
            //  val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            textTitle.text = searchEpisodedata.titleName
            textArtist.text = searchEpisodedata.artistName
            // textDuration.text = TimeParser.secToMin(dataSongDetail.duration)
        }
    }
}






