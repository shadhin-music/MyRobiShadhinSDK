package com.myrobi.shadhinmusiclibrary.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


internal class HomePodcastAdapter(
    val homePatchItem: HomePatchItemModel,
    private val homeCallBack: HomeCallBack,
    val podcastTrackClick : PodcastTrackCallback
) :
    RecyclerView.Adapter<HomePodcastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_podcast_pp_type_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
            //homeCallBack.onClickItemAndAllItem(position, homePatchItem)
            Log.e("TAG","DATA: " + homePatchItem.Data[position].isPaid )
            Log.e("TAG","DATA: " + homePatchItem.Data[position].playingUrl)
            Log.e("TAG","DATA: " + homePatchItem.Data[position].content_Type)
            if(homePatchItem.Data[position].isPaid.toString().equals("false",true)){
                podcastTrackClick.onClickItem(homePatchItem.Data.toMutableList(),position)
            } else{
                it.findNavController().navigate(R.id.to_subscription)
            }

        }
    }
    override fun getItemCount(): Int {
        return homePatchItem.Data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems() {
            val imageView: ImageView = itemView.findViewById(R.id.image)
            val textView: TextView = itemView.findViewById(R.id.txt_name)
            val url: String = homePatchItem.Data[absoluteAdapterPosition].imageUrl.toString()
            textView.text = homePatchItem.Data[absoluteAdapterPosition].titleName ?: ""
//            val textViewArtist: TextView = itemView.findViewById(R.id.txt_name)
//            textViewArtist.text = homePatchItem.Data[absoluteAdapterPosition].artistName ?: ""
            Glide.with(mContext)
                .load(UtilHelper.getImageUrlSize450(url))
                .into(imageView)

        }
    }
}






