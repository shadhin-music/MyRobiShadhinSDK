package com.shadhinmusiclibrary.adapter

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
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback

import com.shadhinmusiclibrary.utils.UtilHelper



internal class PodcastPopularShowsAdapter(
    var patchItem: List<FeaturedPodcastDetailsModel>,
   val podcastDetailsCallback: PodcastDetailsCallback,
) : RecyclerView.Adapter<PodcastPopularShowsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_popular_show_layout, parent, false)
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