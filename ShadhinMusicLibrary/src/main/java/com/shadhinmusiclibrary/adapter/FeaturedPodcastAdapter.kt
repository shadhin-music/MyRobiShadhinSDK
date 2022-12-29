package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.FeaturedPodcastOnItemClickCallback
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.shadhinmusiclibrary.utils.UtilHelper

internal class FeaturedPodcastAdapter(
    var data: MutableList<FeaturedPodcastDetailsModel>,
    var clickCallback: FeaturedPodcastOnItemClickCallback
) : RecyclerView.Adapter<FeaturedPodcastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_featured_podcast_video_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
            data.let { it1 -> clickCallback.onClickItem(it1, position) }
//            homeCallBack.onClickItemAndAllItem(position, homePatchitem)
            // homeCallBack.onClickItem(position, Track(data.get()) )
        }
    }

    override fun getItemCount(): Int {
        return data.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.txt_title) as TextView
            val textViewArtist = itemView.findViewById(R.id.txt_name) as TextView
            val imageView = itemView.findViewById(R.id.image) as ImageView
            val url: String? =
                UtilHelper.getImageUrlSize300(data[absoluteAdapterPosition].ImageUrl)
            textViewArtist.text = data[absoluteAdapterPosition].TrackName
            Glide.with(context)
                .load(url)
                .into(imageView)

            textViewName.text = data[absoluteAdapterPosition].EpisodeName
            // textViewArtist.text = data?.get(absoluteAdapterPosition)?.EpisodeName
//            val textViewName = itemView.findViewById(R.id.tv_person_name) as TextView
//            val imageView2 = itemView.findViewById(R.id.civ_person_image) as CircleImageView
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)
//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId
        }
    }
}