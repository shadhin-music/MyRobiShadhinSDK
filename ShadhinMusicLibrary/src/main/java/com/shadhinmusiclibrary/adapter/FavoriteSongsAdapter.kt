package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class FavoriteSongsAdapter(
    private val lrOnCallBack: DownloadedSongOnCallBack,
    private val openMenu: CommonPSVCallback,
    private val cacheRepository: CacheRepository
) : RecyclerView.Adapter<FavoriteSongsAdapter.ViewHolder>() {
    private var allDownloads: MutableList<IMusicModel> = mutableListOf()
    private var contentId: String = ""

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
        if (mSongDetails.content_Type?.toUpperCase().equals("S")) {
            holder.itemView.setOnClickListener {
                val filterData =
                    allDownloads.filter { it.content_Type?.toUpperCase() == "S" }.toMutableList()

                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }

                lrOnCallBack.onClickFavItem(filterData, clickIndex)
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemSongs(mSongDetails)
            }
        }
//        val contentPodcast = allDownloads[position].content_Type

        if (mSongDetails.content_Type?.contains("PD") == true) {
            holder.itemView.setOnClickListener {
                val filterData =
                    allDownloads.filter {
                        it.content_Type?.toUpperCase()?.contains("PD") == true
                    }.toMutableList()
                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }



                lrOnCallBack.onClickFavItem(filterData, clickIndex)
            }

            menu.setOnClickListener {
                openMenu.onClickBottomItemPodcast(mSongDetails)
            }
        }

        if (mSongDetails.isPlaying) {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(holder.context, R.color.my_sdk_color_primary)
            )
        } else {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(
                    holder.context,
                    R.color.my_sdk_black2
                )
            )
        }
    }

    fun setData(
        data: MutableList<IMusicModel>,
        rootPatch: HomePatchDetailModel,
        mediaId: String?
    ) {
        for (songItem in data) {
            allDownloads.add(
                songItem.apply {
                    isSeekAble = true
                    rootContentId = "00$content_Id"
                    rootContentType = content_Type
                }
            )
        }

        if (mediaId != null) {
            setPlayingSong(mediaId)
        }

        notifyDataSetChanged()
    }

    fun setPlayingSong(mediaId: String) {
        contentId = mediaId
        val newList: List<IMusicModel> =
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, allDownloads)
        val callback = AnyTrackDiffCB(allDownloads, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        allDownloads.clear()
        allDownloads.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return allDownloads.size
    }

    fun updateData(songsFavoriteContent: List<FavDataModel>?) {
        allDownloads.clear()
        songsFavoriteContent?.let { allDownloads.addAll(it) }
        notifyDataSetChanged()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.getContext()
        var tvSongName: TextView? = null
        fun bindItems() {
            val sivSongIcon: ImageView = itemView.findViewById(R.id.siv_song_icon)
            Glide.with(context)
                .load(allDownloads[absoluteAdapterPosition].imageUrl?.let {
                    UtilHelper.getImageUrlSize300(it)
                })
                .into(sivSongIcon)
            tvSongName = itemView.findViewById(R.id.tv_song_name)
            tvSongName?.text = allDownloads[absoluteAdapterPosition].titleName
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
                cacheRepository.isTrackDownloaded(allDownloads[absoluteAdapterPosition].content_Id)
                    ?: false
            if (isDownloaded) {
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
            }
        }
    }
}