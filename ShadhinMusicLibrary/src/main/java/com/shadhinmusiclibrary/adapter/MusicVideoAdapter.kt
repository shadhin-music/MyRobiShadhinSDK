package com.shadhinmusiclibrary.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.video.VideoActivity
import com.shadhinmusiclibrary.data.model.LatestVideoModelDataModel
import com.shadhinmusiclibrary.data.model.VideoModel

internal class MusicVideoAdapter(var data: List<LatestVideoModelDataModel>) :
    RecyclerView.Adapter<MusicVideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_video_item_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemCount(): Int {
        return data?.size!!
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {
            val textArtist = itemView.findViewById(R.id.txt_title) as TextView
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
            textViewName.text = data[absoluteAdapterPosition].fav
            textArtist.text = data[absoluteAdapterPosition].title
            val imageView = itemView.findViewById(R.id.image) as ImageView
            val url: String = data[absoluteAdapterPosition].getImageUrl300Size()
            Glide.with(itemView.context).load(url).into(imageView)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in data) {
                    val video = VideoModel()
                    video.setData2(item)
                    videoArray.add(video)
                }
                val videos: ArrayList<VideoModel> = videoArray
                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, absoluteAdapterPosition)
                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videos)
                itemView.context.startActivity(intent)
            }
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)

//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId
        }
    }
}