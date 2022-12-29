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
import com.shadhinmusiclibrary.data.model.search.CommonSearchData
import com.shadhinmusiclibrary.utils.UtilHelper


internal class SearchPodcastTracksAdapter(
    private val seaItemCallback: SearchItemCallBack
) : RecyclerView.Adapter<SearchPodcastTracksAdapter.ViewHolder>() {
    private var searchPodcastTrack: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_search_album_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(searchPodcastTrack[position])
        holder.itemView.setOnClickListener {
            seaItemCallback.onClickPlaySearchItem(searchPodcastTrack, position)
        }
    }

    override fun getItemCount(): Int {
        return searchPodcastTrack.size
    }

    fun setSearchPodcastTracks(
        rootModel: CommonSearchData
    ) {
        this.searchPodcastTrack = UtilHelper.getSearchDataToRootData(rootModel)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(searchPodcastTrack: IMusicModel) {
            val imageView: ImageView = itemView.findViewById(R.id.thumb)
            val textTitle: TextView = itemView.findViewById(R.id.title)
            Glide.with(context)
                .load(searchPodcastTrack.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
            val textArtist: TextView = itemView.findViewById(R.id.similarArtist)
            textTitle.text = searchPodcastTrack.titleName
            textArtist.text = searchPodcastTrack.artistName
        }
    }
}






