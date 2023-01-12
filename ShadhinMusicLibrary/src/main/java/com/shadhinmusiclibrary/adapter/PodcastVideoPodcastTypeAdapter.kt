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
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.shadhinmusiclibrary.utils.CircleImageView
import com.shadhinmusiclibrary.utils.UtilHelper
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation


internal class PodcastVideoPodcastTypeAdapter(
    var patchItem: List<FeaturedPodcastDetailsModel>,
   val podcastDetailsCallback: PodcastDetailsCallback,
) : RecyclerView.Adapter<PodcastVideoPodcastTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_podcast_layout, parent, false)
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
            val textViewName = itemView.findViewById(R.id.title) as TextView
            val textViewSubtitle = itemView.findViewById(R.id.artist_name) as TextView
//            val show_des = itemView.findViewById(R.id.show_des) as TextView
//            val txt_time =itemView.findViewById(R.id.txt_time) as TextView
            val imageView = itemView.findViewById(R.id.thumb1) as CircleImageView
            val backgroundImg = itemView.findViewById(R.id.backgroundImg) as ImageView
           val  brightness =  BrightnessFilterTransformation(0.6f);
            val multi = MultiTransformation(
                brightness,
                BlurTransformation(100, 2))

//            ivPlayBtn = itemView.findViewById(R.id.pd_play)
            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize300(
                        patchItem.get(absoluteAdapterPosition).ImageUrl ?: ""
                    )
                )
                .into(imageView)
            Glide.with(mContext)
                .load( UtilHelper.getImageUrlSize300(
                    patchItem.get(absoluteAdapterPosition).ImageUrl ?: ""
                    )
                ) .apply(RequestOptions.bitmapTransform(multi)).transition(DrawableTransitionOptions.withCrossFade(1000))
                .into(backgroundImg)
            textViewName.text =patchItem.get(absoluteAdapterPosition).ShowName
//            txt_time.text = patchItem.Data[absoluteAdapterPosition].Duration
            textViewSubtitle.text =patchItem.get(absoluteAdapterPosition).Presenter
           // show_des.text = result
           // Log.e("TAG","DATA: "+ patchItem.Data.size)

        }
    }
}