package com.shadhinmusiclibrary.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.video.VideoActivity
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.VideoModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.fragments.fav.onFavArtistClickAll
import com.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class AllFavoriteAdapter(
    private val lrOnCallBack: DownloadedSongOnCallBack,
    private val openMenu: CommonPSVCallback,
    private val artistClick: onFavArtistClickAll
) : RecyclerView.Adapter<AllFavoriteAdapter.ViewHolder>() {
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

        if (mSongDetails.content_Type?.toUpperCase().equals("V")) {

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in allDownloads.filter { it.content_Type == "V" }
                    .toMutableList()) {
                    val video = VideoModel()
                    video.setDataFavoriteIM(item)
                    videoArray.add(video)
                }
                val clickIndex =
                    videoArray.indexOfFirst { it.contentID == mSongDetails.content_Id }
                //  val videos :ArrayList<Video> = videoArray
                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, clickIndex)
                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videoArray)
                holder.itemView.context.startActivity(intent)
//                for (item in allDownloads) {
//                    val video = VideoModel()
////                    if (item.content_Type?.toUpperCase().equals("V")) {
//                        video.setDataFavoriteIM(item)
//                        videoArray.add(video)
//                   // }
//                }
//                val clickIndex =
//                    allDownloads.indexOfFirst { it.content_Id == mSongDetails.content_Id }
//
//                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, clickIndex)
//                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videoArray)
//                holder.itemView.context.startActivity(intent)
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemVideo(allDownloads[position])
            }
        }
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
        if (mSongDetails.content_Type?.toUpperCase().equals("P")) {
            holder.itemView.setOnClickListener {
                val filterData =
                    allDownloads.filter { it.content_Type?.toUpperCase() == "P" }.toMutableList()
                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }

                artistClick.onFavPlaylistClick(clickIndex, filterData)
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemSongs(mSongDetails)
            }
        }
        if (mSongDetails.content_Type?.toUpperCase().equals("A")) {
            holder.itemView.setOnClickListener {
                val filterData =
                    allDownloads.filter { it.content_Type?.toUpperCase() == "A" }.toMutableList()
                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }

                artistClick.onFavArtistClick(
                    clickIndex,
                    filterData
                )
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemSongs(mSongDetails)
            }
        }
        if (mSongDetails.content_Type?.toUpperCase().equals("R")) {
            holder.itemView.setOnClickListener {
                val filterData =
                    allDownloads.filter { it.content_Type?.toUpperCase() == "R" }.toMutableList()
                val clickIndex =
                    filterData.indexOfFirst { it.content_Id == mSongDetails.content_Id }

                lrOnCallBack.onFavAlbumClick(
                    clickIndex,
                    filterData
                )
                //lrOnCallBack.onClickFavItem(allDownloads as MutableList<FavData>, position)
            }
            menu.setOnClickListener {
                openMenu.onClickBottomItemSongs(mSongDetails)
            }
        }
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
        data: MutableList<IMusicModel>, rootConId: String,
        mediaId: String?,
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

    fun updateData(allFavoriteContent: List<FavDataModel>?) {
        allDownloads.clear()
        allFavoriteContent?.let { allDownloads.addAll(it) }
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
        }
    }
}