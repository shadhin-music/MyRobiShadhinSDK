package com.myrobi.shadhinmusiclibrary.adapter


import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.activities.video.VideoActivity
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable


internal class TopTrendingVideosAdapter(
    val homePatchItemModel: HomePatchItemModel,
    val homePatchDetail: List<HomePatchDetailModel>
) : RecyclerView.Adapter<TopTrendingVideosAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_trending_music_video_list, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()


    }

    override fun getItemCount(): Int {
        return homePatchItemModel.Data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {

            val textViewName = itemView.findViewById(R.id.txt_title) as TextView
            val textViewArtist = itemView.findViewById(R.id.txt_name) as TextView
            val imageView = itemView.findViewById(R.id.image) as ImageView
            val url: String = homePatchItemModel.Data[position].imageUrl ?: ""
            textViewName.text = homePatchItemModel.Data[position].titleName
            textViewArtist.text = homePatchItemModel.Data[absoluteAdapterPosition].artistName
            Glide.with(itemView.context).load(UtilHelper.getImageUrlSize300(url)).into(imageView)
            itemView.setOnClickListener {
                Log.e("TAG","Heello")
              //  val intent = Intent(itemView.context, VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in homePatchItemModel.Data) {
                    val video = VideoModel()
                    video.setData(item)
                    videoArray.add(video)
                }
                val videos: ArrayList<VideoModel> = videoArray
                val bundle = Bundle().apply {
                    putSerializable(
                        VideoActivity.INTENT_KEY_POSITION,
                        absoluteAdapterPosition
                    )
                    putSerializable(VideoActivity.INTENT_KEY_DATA_LIST,
                      videos
                    )

                }
//                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, absoluteAdapterPosition)
//                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videos)
               //itemView.context.startActivity(intent)
                itemView.findNavController().navigate(R.id.to_videoActivity,bundle)
            }


        }

    }

}






