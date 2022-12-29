package com.shadhinmusiclibrary.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.shadhinmusiclibrary.data.model.podcast.SongTrackModel

typealias SelectedIdFunc = ((id:Int) -> Unit)

internal class PodcastMoreEpisodesListAdapter(
    var episode: MutableList<EpisodeModel>,
    val homeCallBack: HomeCallBack
) :
    RecyclerView.Adapter<PodcastMoreEpisodesListAdapter.ViewHolder>() {

    var selectedIdFunc:SelectedIdFunc?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_top_trending_list, parent, false)
        return ViewHolder(v)
    }

    private var filteredItem: MutableList<EpisodeModel>? = null


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(position)


        holder.itemView.setOnClickListener {
            homeCallBack.onClickItemPodcastEpisode(position, episode)
            selectedIdFunc?.invoke( episode[position].Id)

        }
    }

    override fun getItemCount(): Int {
        return episode?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context

        fun bindItems(position: Int) {
            val imageView: ShapeableImageView = itemView.findViewById(R.id.image)
            val textView: TextView = itemView.findViewById(R.id.txt_title)
            val url: String? = episode[position].ImageUrl
            textView.text = episode[position].Name

            Glide.with(mContext)
                .load(url?.replace("<\$size\$>", "300"))
                .into(imageView)
        }
    }
}






