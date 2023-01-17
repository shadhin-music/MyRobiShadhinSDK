package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.myrobi.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback

import com.myrobi.shadhinmusiclibrary.utils.UtilHelper



internal class PodcastMoreEpisodeShowAdapter(
    var patchItem: List<FeaturedPodcastDetailsModel>,
   val podcastDetailsCallback: PodcastDetailsCallback,
) : RecyclerView.Adapter<PodcastMoreEpisodeShowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_top_trending_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        //holder.item = patchItem.Data[position]
      // podcastDetailsCallback.getCurrentVH(holder, patchItem.Data)
        holder.itemView.setOnClickListener {
          podcastDetailsCallback.onPodcastEpisodeClick(patchItem,position)

        }

    }

    override fun getItemCount(): Int {
        return patchItem.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems() {

            val imageView = itemView.findViewById(R.id.image) as ImageView
            val name = itemView.findViewById(R.id.txt_name) as TextView
            name.text = patchItem.get(absoluteAdapterPosition).ShowName
            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize300(
                        patchItem.get(absoluteAdapterPosition).ImageUrl ?: ""
                    )
                )
                .into(imageView)

        }
    }
}