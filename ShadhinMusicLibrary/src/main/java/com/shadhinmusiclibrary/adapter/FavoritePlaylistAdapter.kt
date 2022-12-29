package com.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.fragments.fav.onFavPlaylistClick
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class FavoritePlaylistAdapter(
    private val lrOnCallBack: DownloadedSongOnCallBack,
    private val openMenu: CommonPSVCallback,
    private val cacheRepository: CacheRepository,
    private val albumClick: onFavPlaylistClick
) : RecyclerView.Adapter<FavoritePlaylistAdapter.ViewHolder>() {

    private var allDownloads: MutableList<FavDataModel> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_download_songs_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mSongDetails = allDownloads[position]
        holder.bindItems()
        val menu = holder.itemView.findViewById<ImageView>(R.id.iv_song_menu_icon)
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
//        val position =allDownloads[position]
        holder.itemView.setOnClickListener {
            albumClick.onFavPlaylistClick(position, allDownloads)
            //lrOnCallBack.onClickFavItem(allDownloads as MutableList<FavData>, position)
        }
        menu.setOnClickListener {
            openMenu.onClickBottomItemSongs(allDownloads[position])
        }
        if (mSongDetails.content_Type?.contains("PD") == true) {
            menu.setOnClickListener {
                val filterData =
                    allDownloads.filter {
                        it.content_Type?.length!! > 1 &&
                                it.content_Type?.substring(0, 2) == "PD"
                    }.toMutableList()
                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }

                openMenu.onClickBottomItemPodcast(filterData[clickIndex])
            }
        }
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
        fun bindItems() {
            val sivSongIcon: ImageView = itemView.findViewById(R.id.siv_song_icon)
            Glide.with(context)
                .load(allDownloads[absoluteAdapterPosition].imageUrl?.let {
                    UtilHelper.getImageUrlSize300(it)
                })
                .into(sivSongIcon)
            val tvSongName: TextView = itemView.findViewById(R.id.tv_song_name)
            tvSongName.text = allDownloads[absoluteAdapterPosition].titleName

            val tvSingerName: TextView = itemView.findViewById(R.id.tv_singer_name)
            tvSingerName.text = allDownloads[absoluteAdapterPosition].artistName
            tvSingerName.visibility = GONE
            val tvSongLength: TextView = itemView.findViewById(R.id.tv_song_length)
            tvSongLength.visibility = GONE
            tvSongLength.text =
                TimeParser.secToMin(allDownloads[absoluteAdapterPosition].total_duration)
            val progressIndicator: CircularProgressIndicator = itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            progressIndicator.tag = allDownloads[absoluteAdapterPosition].content_Id
            progressIndicator.visibility = View.GONE
            downloaded.visibility = View.GONE
            downloaded.tag = 220
            val isDownloaded =
                cacheRepository.isTrackDownloaded(allDownloads[absoluteAdapterPosition].content_Id!!)
                    ?: false
            if (isDownloaded) {
                Log.e("TAG", "ISDOWNLOADED: " + isDownloaded)
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
            }
        }
    }
}