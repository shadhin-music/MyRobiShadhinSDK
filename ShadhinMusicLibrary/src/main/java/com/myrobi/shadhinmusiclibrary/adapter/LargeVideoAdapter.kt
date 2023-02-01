package com.myrobi.shadhinmusiclibrary.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.activities.video.VideoActivity
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.VideoModel
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper


internal class LargeVideoAdapter(val argHomePatchItem: HomePatchItemModel?) :
    RecyclerView.Adapter<LargeVideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_big_video_view_item, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()


    }

    override fun getItemCount(): Int {
        return argHomePatchItem?.Data?.size!!
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {
            val textArtist = itemView.findViewById(R.id.txt_title) as TextView
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
            textViewName.text = argHomePatchItem?.Data?.get(absoluteAdapterPosition)?.artistName
            textArtist.text = argHomePatchItem?.Data?.get(absoluteAdapterPosition)?.titleName
            val imageView = itemView.findViewById(R.id.image) as ImageView
            val imageView2 = itemView.findViewById<ImageView>(R.id.ib_play_btn)
            val url: String = argHomePatchItem?.Data?.get(absoluteAdapterPosition)?.imageUrl ?: ""
            Glide.with(itemView.context).load(UtilHelper.getImageUrlSize300(url)).into(imageView)
            imageView2.visibility = VISIBLE
            itemView.setOnClickListener {
                //Log.e("TAG","DATA: "+ argHomePatchItem.Data[0].playingUrl)
                val intent = Intent(itemView.context, VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in argHomePatchItem?.Data!!) {
                    val video = VideoModel()
                    video.setData(item)
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






