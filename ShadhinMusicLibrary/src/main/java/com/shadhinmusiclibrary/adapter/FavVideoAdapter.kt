package com.shadhinmusiclibrary.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.video.VideoActivity
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.model.VideoModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class FavVideoAdapter(

    private val lrOnCallBack: DownloadedSongOnCallBack,
    private val openMenu: CommonPSVCallback,
    private val cacheRepository: CacheRepository
) : RecyclerView.Adapter<FavVideoAdapter.ViewHolder>() {
    private var allDownloads: MutableList<FavDataModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_row_video_li, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mSongDetails = allDownloads[position]

        holder.bindItems()
        val menu = holder.itemView.findViewById<ImageView>(R.id.threeDotButton)
        if (mSongDetails.content_Type?.toUpperCase() == "V") {
//
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in allDownloads.filter { it.content_Type?.toUpperCase() == "V" }
                    .toMutableList()) {
                    val video = VideoModel()
                    video.setDataFavorite(item)
                    videoArray.add(video)
                }
                val clickIndex =
                    videoArray.indexOfFirst { it.contentID == mSongDetails.content_Id }
                //  val videos :ArrayList<Video> = videoArray
                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, clickIndex)
                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videoArray)
                holder.itemView.context.startActivity(intent)
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemVideo(mSongDetails)
            }
        }
//        if(allDownloads[position].rootType.equals("S")){
//            holder.itemView.setOnClickListener {
//            lrOnCallBack.onClickItem(allDownloads as MutableList<DownloadedContent>, position)
//             Log.e("TAG","ALL Downloads: "+ allDownloads)
        // }
        //}
    }

    fun setData(data: MutableList<FavDataModel>) {
        allDownloads = data.toMutableList()
        notifyDataSetChanged()

    }

    fun updateData(artistFavoriteContent: List<FavDataModel>?) {
        allDownloads.clear()
        artistFavoriteContent?.let { allDownloads.addAll(it) }
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return allDownloads.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.getContext()
        private var titleTextView: TextView = itemView.findViewById(R.id.videoTitle)
        private var subTitleTextView: TextView = itemView.findViewById(R.id.videoDesc)
        private var durationTextView: TextView = itemView.findViewById(R.id.video_duration)
        private var playPauseImage: ImageView = itemView.findViewById(R.id.play_pause)
        private var videoImage: ImageView = itemView.findViewById(R.id.videoImage)
        private var threeDotButton: ImageButton = itemView.findViewById(R.id.threeDotButton)

        fun bindItems() {
            titleTextView.text = allDownloads[absoluteAdapterPosition].titleName
            durationTextView.text =
                TimeParser.secToMin(allDownloads[absoluteAdapterPosition].total_duration)
//            if (item.isPlaying) {
//                titleTextView.setTextColor( ContextCompat.getColor(itemView.context,R.color.my_sdk_color_primary))
//            } else {
//                titleTextView.setTextColor(ContextCompat.getColor(itemView.context,R.color.my_sdk_down_title))
//            }
            subTitleTextView.text = allDownloads[absoluteAdapterPosition].artistName

            Glide.with(itemView.context)
                .load(allDownloads[absoluteAdapterPosition].imageUrl?.let {
                    UtilHelper.getImageUrlSize300(it)
                })
                .placeholder(R.drawable.my_bl_sdk_default_video)
                .into(videoImage)
            val progressIndicator: CircularProgressIndicator = itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicator.tag = allDownloads[absoluteAdapterPosition].content_Id
            progressIndicator.visibility = View.GONE
            downloaded.visibility = View.GONE
//            downloaded.tag = 200
            val isDownloaded =
                cacheRepository.isVideoDownloaded(allDownloads[absoluteAdapterPosition].content_Id)
            if (isDownloaded) {
                Log.e("TAG", "ISDOWNLOADED: " + isDownloaded)
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
                progressIndicator.layoutParams = LinearLayout.LayoutParams(0, 0)
            }
//            if (item.isPlaystate) {
//                playPauseImage.setImageResource(R.drawable.my_bl_sdk_ic_pause_n)
//            } else {
//                playPauseImage.setImageResource(R.drawable.my_bl_sdk_ic_play_n)
//            }
//            itemView.setOnClickListener {
//                videoItemClickFunc?.invoke(getItem(absoluteAdapterPosition),false)
//            }
//            threeDotButton.setOnClickListener {
//                bottomsheetDialog.openDialog(item)
//            }
        }
    }
}