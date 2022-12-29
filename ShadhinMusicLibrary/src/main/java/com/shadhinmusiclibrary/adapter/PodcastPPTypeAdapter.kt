package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.shadhinmusiclibrary.utils.UtilHelper

internal class PodcastPPTypeAdapter(
    var PatchItem: FeaturedPodcastDataModel,
    val podcastDetailsCallback: PodcastDetailsCallback
) : RecyclerView.Adapter<PodcastPPTypeAdapter.ViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_top_trending_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
         podcastDetailsCallback.onPodcastLatestShowClick(PatchItem.Data,position)
        }
    }

    override fun getItemCount(): Int {
        return PatchItem.Data.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
            val imageView2 = itemView.findViewById(R.id.image) as ImageView

            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize300(
                        PatchItem.Data[absoluteAdapterPosition].ImageUrl ?: ""
                    )
                )
                .into(imageView2)
            textViewName.text = PatchItem.Data[absoluteAdapterPosition].ShowName
        }
    }
}