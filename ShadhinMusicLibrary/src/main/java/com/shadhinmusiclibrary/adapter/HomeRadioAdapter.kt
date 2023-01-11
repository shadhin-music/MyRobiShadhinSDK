package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.utils.UtilHelper

internal class HomeRadioAdapter(
    val homePatchItemModel: HomePatchItemModel,
    val podcastTrackClick: PodcastTrackCallback
) : RecyclerView.Adapter<HomeRadioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.radio_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        val radioTrackItem = homePatchItemModel.Data[position]
        holder.itemView.setOnClickListener {
              //data.let { it1 -> clickCallback.onClickItem(it1, position) }
             // homeCallBack.onClickItemAndAllItem(0, homePatchItemModel)
            // homeCallBack.onClickItem(position, Track(data.get()) )
            podcastTrackClick.onClickRadioItem(radioTrackItem)
        }
    }

    override fun getItemCount(): Int {
        return homePatchItemModel.Data.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems() {

            val imageView = itemView.findViewById(R.id.artist_img) as ImageView
            val url: String? =
                homePatchItemModel.Data[absoluteAdapterPosition].imageUrl?.let {
                    UtilHelper.getImageUrlSize300(it)
                }
           // textViewArtist.text = homePatchItemModel.Data[absoluteAdapterPosition].titleName
            Glide.with(context)
                .load(url)
                .into(imageView)


        }
    }
}