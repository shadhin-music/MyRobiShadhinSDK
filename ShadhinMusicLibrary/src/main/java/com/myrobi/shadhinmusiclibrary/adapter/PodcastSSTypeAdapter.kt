package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.myrobi.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


internal class PodcastSSTypeAdapter(
    var patchItem: List<FeaturedPodcastDetailsModel>,
    val podcastDetailsCallback: PodcastDetailsCallback,
) : RecyclerView.Adapter<PodcastSSTypeAdapter.ViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_podcast_pp_type_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
           podcastDetailsCallback.onPodcastEpisodeClick(patchItem,position)
        }
    }

    override fun getItemCount(): Int {
        return patchItem.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
           // val textViewSubtitle = itemView.findViewById(R.id.sub_title) as TextView
           // val show_des = itemView.findViewById(R.id.show_des) as TextView
            val imageView = itemView.findViewById(R.id.image) as ImageView

            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize450(
                        patchItem.get(absoluteAdapterPosition).ImageUrl ?: ""
                    )
                )
                .into(imageView)
            //val result = Html.fromHtml(homePatchItem.Data[absoluteAdapterPosition].About).toString()
            textViewName.text = patchItem.get(absoluteAdapterPosition).EpisodeName
            //show_des.text = result
        }
    }
}