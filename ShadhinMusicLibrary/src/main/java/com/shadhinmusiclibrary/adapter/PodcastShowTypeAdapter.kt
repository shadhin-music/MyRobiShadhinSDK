package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.shadhinmusiclibrary.utils.UtilHelper

internal class PodcastShowTypeAdapter(
    var patchItem: HomePatchItemModel,
    val podcastDetailsCallback: HomeCallBack
) : RecyclerView.Adapter<PodcastShowTypeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_top_trending_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
            podcastDetailsCallback.onClickItemAndAllItem(position, patchItem)
//         podcastDetailsCallback.onPodcastLatestShowClick(PatchItem.Data,position)
        }
    }

    override fun getItemCount(): Int {
        return patchItem.Data.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.txt_title) as TextView
            val textViewArtist = itemView.findViewById(R.id.txt_name) as TextView
            val imageView= itemView.findViewById(R.id.image) as ImageView
            val imageViewPaid = itemView.findViewById(R.id.img_premium) as ImageView
            if(patchItem.Data[absoluteAdapterPosition].isPaid == true){
                imageViewPaid.visibility = View.VISIBLE
            } else{
                imageViewPaid.visibility = View.GONE
            }
            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize300(
                        patchItem.Data[absoluteAdapterPosition].imageUrl ?: ""
                    )
                )
                .into(imageView)
            textViewName.text = patchItem.Data[absoluteAdapterPosition].titleName
            textViewArtist.text = patchItem.Data[absoluteAdapterPosition].artistName
        }
    }
}