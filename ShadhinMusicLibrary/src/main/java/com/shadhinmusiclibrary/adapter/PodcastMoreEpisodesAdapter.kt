package com.shadhinmusiclibrary.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.model.podcast.DataModel
import com.shadhinmusiclibrary.data.model.podcast.EpisodeModel


internal class PodcastMoreEpisodesAdapter(val data: DataModel?, val homeCallBack: HomeCallBack) :
    RecyclerView.Adapter<PodcastMoreEpisodesAdapter.PodcastMoreEpisodesViewHolder>() {
    var episode: MutableList<EpisodeModel> = ArrayList()
    private var filteredItem: MutableList<EpisodeModel>? = null
    private var selectedId:Int?=null
    val selectedIdFuncX:SelectedIdFunc = {
        selectedId = it
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PodcastMoreEpisodesViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_item_you_might_like, parent, false)
        return PodcastMoreEpisodesViewHolder(v)
    }


    override fun onBindViewHolder(holder: PodcastMoreEpisodesViewHolder, position: Int) {
        Log.i("setData", "setData: ${episode.map { it.Code }}")
        holder.bindItems()


    }

    override fun getItemViewType(position: Int) = VIEW_TYPE
    override fun getItemCount(): Int {
        return 1
    }

    fun setData(episodeList: MutableList<EpisodeModel>) {

        this.episode = episodeList
        Log.i("setData", "setData: ${episodeList.map { it.Code }}")
    }

    inner class PodcastMoreEpisodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems() {
            if (episode.isNotEmpty()) {

                val index = episode.indexOfFirst { it.Id == selectedId }
                if(index !=-1){
                    episode.removeAt(index)
                }

                val textView: TextView = itemView.findViewById(R.id.tvTitle)
                textView.text= "More Episode"
                val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
                recyclerView.layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

                recyclerView.adapter = PodcastMoreEpisodesListAdapter(episode,homeCallBack).apply {
                    this.selectedIdFunc = selectedIdFuncX
                }
            }





        }

    }
    companion object {
        const val VIEW_TYPE = 3
    }
}






