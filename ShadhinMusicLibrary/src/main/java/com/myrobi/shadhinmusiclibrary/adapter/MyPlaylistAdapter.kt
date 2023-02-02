package com.myrobi.shadhinmusiclibrary.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.OnItemClickCallBack
import com.myrobi.shadhinmusiclibrary.fragments.create_playlist.UserPlaylistData
import kotlin.random.Random


internal class MyPlaylistAdapter(val data: List<UserPlaylistData>?,val onClickItem:OnItemClickCallBack) :
    RecyclerView.Adapter<MyPlaylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_my_playlist_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
            val userPlaylistData = data?.get(position)
            Log.e("TAG","Item: "+ userPlaylistData)
            onClickItem.GotoPlaylistDetails(userPlaylistData?.id,userPlaylistData?.name,holder.imageId)
        }
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        var rnd = Random


//        var currentColor: Int =
//            Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        val images = arrayListOf<Int>(R.drawable.my_bl_sdk_playlist_first_gradient,R.drawable.my_bl_sdk_playlist_second_gradient,R.drawable.my_bl_sdk_playlist_third_gradient,
          R.drawable.my_bl_sdk_playlist_fourth_gradient, R.drawable.my_bl_sdk_playlist_fifth_gradient)
        var randomNumber: Int = rnd.nextInt(images.size)
        val imageId: Int = images.get(randomNumber)
        fun bindItems() {

            val imageView = itemView.findViewById(R.id.image) as ImageView
               imageView.setImageResource(imageId)
               imageView.setBackgroundResource(R.drawable.my_bl_sdk_playlist_bg)
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
               textViewName.text= data?.get(absoluteAdapterPosition)?.name.toString()
            val textSongCount = itemView.findViewById<TextView>(R.id.txt_song_count)
              if(data?.get(absoluteAdapterPosition)?.data?.size== null){
                  textSongCount.text = "0 Song"
              }else {
                  textSongCount.text =
                      data?.get(absoluteAdapterPosition)?.data?.size.toString() + " Songs"
                  Log.e("TAG", "PLAYLISTCreate:" + data?.get(absoluteAdapterPosition)?.data?.size.toString())
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






