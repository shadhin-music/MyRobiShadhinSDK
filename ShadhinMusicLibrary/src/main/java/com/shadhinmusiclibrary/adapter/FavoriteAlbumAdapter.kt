package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
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
import com.shadhinmusiclibrary.fragments.fav.onFavAlbumClick
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class FavoriteAlbumAdapter(
    private val lrOnCallBack: DownloadedSongOnCallBack,
    private val openMenu: CommonPSVCallback,
    private val cacheRepository: CacheRepository,
    private val albumClick: onFavAlbumClick
) : RecyclerView.Adapter<FavoriteAlbumAdapter.ViewHolder>() {
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
        if (mSongDetails.content_Type?.toUpperCase() == "R") {
            holder.itemView.setOnClickListener {
                val filterData =
                    allDownloads.filter { it.content_Type?.toUpperCase() == "R" }.toMutableList()
                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }

                albumClick.onFavAlbumClick2(
                    clickIndex,
                    filterData
                )
                //lrOnCallBack.onClickFavItem(allDownloads as MutableList<FavData>, position)
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemSongs(mSongDetails)
            }
        }
//        if (mSongDetails.content_Type?.substring(0, 2) == "PD") {
//        menu.setOnClickListener {
//            openMenu.onClickBottomItemPodcast(mSongDetails)
//        }
//        }
    }

    override fun getItemCount(): Int {
        return allDownloads.size
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.getContext()
        fun bindItems() {
            val sivSongIcon: ImageView = itemView.findViewById(R.id.siv_song_icon)
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(allDownloads[absoluteAdapterPosition].imageUrl!!))
                .into(sivSongIcon)
            val tvSongName: TextView = itemView.findViewById(R.id.tv_song_name)
            tvSongName.text = allDownloads[absoluteAdapterPosition].titleName

            val tvSingerName: TextView = itemView.findViewById(R.id.tv_singer_name)
            tvSingerName.text = allDownloads[absoluteAdapterPosition].artistName

            val tvSongLength: TextView = itemView.findViewById(R.id.tv_song_length)
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
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
            }
        }
    }
}