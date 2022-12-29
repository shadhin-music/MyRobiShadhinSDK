package com.shadhinmusiclibrary.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.ItemClickListener
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.fragments.create_playlist.UserPlaylistData
import kotlin.random.Random

internal class CreatePlaylistListAdapter(
    val data: List<UserPlaylistData>,
    val itemClick: ItemClickListener,
    val mSongDetails: IMusicModel
) : RecyclerView.Adapter<CreatePlaylistListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_add_songs_to_playlist_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        val tvSongLength: TextView = holder.itemView.findViewById(R.id.tv_song_length)
        if(data[position].data == null){
            tvSongLength.text = "0" + " " + "Songs"
        }else {
            tvSongLength.text =
                data[position].data?.size.toString() + " " + "Songs"
        }
//        if(allDownloads[position].rootType.equals("V")){
//             holder.itemView.setOnClickListener {
//                 val intent = Intent(holder.itemView.context, VideoActivity::class.java)
//                 val videoArray = ArrayList<Video>()
//                 for (item in allDownloads) {
//                     val video = Video()
//                     video.setDataDownload(item)
//                     videoArray.add(video)
//                 }
//                 val videos :ArrayList<Video> = videoArray
//                 intent.putExtra(VideoActivity.INTENT_KEY_POSITION, position)
//                 intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videos)
//                 holder.itemView.context.startActivity(intent)
//             }
//        }
//        if(allDownloads[position].rootType.equals("S")){
        holder.itemView.setOnClickListener {
            itemClick.onClick(position, mSongDetails, data[position].id)
            Log.e("TAG","ALL Downloads: "+ position)
        }
        //}
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.context
        var rnd = Random
        //        var currentColor: Int =
//            Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        val images = arrayListOf<Int>(
            R.drawable.my_bl_sdk_playlist_first_gradient,
            R.drawable.my_bl_sdk_playlist_second_gradient,
            R.drawable.my_bl_sdk_playlist_third_gradient,
            R.drawable.my_bl_sdk_playlist_fourth_gradient,
            R.drawable.my_bl_sdk_playlist_fifth_gradient
        )
        var randomNumber: Int = rnd.nextInt(images.size)
        val imageId: Int = images[randomNumber]

        @SuppressLint("SetTextI18n")
        fun bindItems() {
            val imageView: ImageView = itemView.findViewById(R.id.siv_song_icon)
            imageView.setImageResource(imageId)
            imageView.setBackgroundResource(R.drawable.my_bl_sdk_playlist_bg)
            val tvSongName: TextView = itemView.findViewById(R.id.tv_song_name)
            tvSongName.text = data[absoluteAdapterPosition].name
//            val tvSingerName: TextView = itemView.findViewById(R.id.tv_singer_name)
//            tvSingerName.text = allDownloads[absoluteAdapterPosition].artist

        }
    }
}