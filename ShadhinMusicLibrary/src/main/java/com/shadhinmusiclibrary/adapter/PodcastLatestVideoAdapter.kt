package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.shadhinmusiclibrary.utils.CircleImageView
import com.shadhinmusiclibrary.utils.UtilHelper
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation


internal class PodcastLatestVideoAdapter(
    var patchItem: List<FeaturedPodcastDetailsModel>,
   val podcastDetailsCallback: PodcastDetailsCallback,
) : RecyclerView.Adapter<PodcastLatestVideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.large_video_layout, parent, false)
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
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
            val textViewSubtitle = itemView.findViewById(R.id.txt_artist) as TextView
                textViewSubtitle.visibility = VISIBLE
//            val show_des = itemView.findViewById(R.id.show_des) as TextView
//            val txt_time =itemView.findViewById(R.id.txt_time) as TextView
            val imageView = itemView.findViewById(R.id.image) as ImageView

            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize1280(
                        patchItem.get(absoluteAdapterPosition).ImageUrl ?: ""
                    )
                )
                .into(imageView)

            textViewName.text =patchItem.get(absoluteAdapterPosition).ShowName
//            txt_time.text = patchItem.Data[absoluteAdapterPosition].Duration
            textViewSubtitle.text =patchItem.get(absoluteAdapterPosition).Presenter
           // show_des.text = result
           // Log.e("TAG","DATA: "+ patchItem.Data.size)

        }
    }
}