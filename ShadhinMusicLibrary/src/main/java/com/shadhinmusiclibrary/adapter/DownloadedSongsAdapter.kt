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
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPSVCallback
import com.shadhinmusiclibrary.callBackService.DownloadedSongOnCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.download.room.DownloadedContent
import com.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class DownloadedSongsAdapter(
    private val lrOnCallBack: DownloadedSongOnCallBack,
    private val openMenu: CommonPSVCallback,
) : RecyclerView.Adapter<DownloadedSongsAdapter.DownloadSongViewHolder>() {
    private var allDownloads: MutableList<IMusicModel> = mutableListOf()
    private var contentId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadSongViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_download_songs_item, parent, false)
        return DownloadSongViewHolder(v)
    }


    override fun onBindViewHolder(holder: DownloadSongViewHolder, position: Int) {
        val mSongDetails = allDownloads[position]

        holder.bindItems(mSongDetails)
        val menu: ImageView = holder.itemView.findViewById(R.id.iv_song_menu_icon)
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

                lrOnCallBack.onClickItem(filterData, clickIndex)
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


                lrOnCallBack.onClickItem(filterData, clickIndex)
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

    fun upDateData(data: MutableList<DownloadedContent>?) {
        allDownloads.clear()
        data?.let { allDownloads.addAll(it) }
        notifyDataSetChanged()

    }

    inner class DownloadSongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.getContext()
        var tvSongName: TextView? = null
        fun bindItems(mImusicItem: IMusicModel) {
            val sivSongIcon: ImageView = itemView.findViewById(R.id.siv_song_icon)
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(allDownloads[absoluteAdapterPosition].imageUrl!!))
                .into(sivSongIcon)
            tvSongName = itemView.findViewById(R.id.tv_song_name)
            tvSongName?.text = mImusicItem.titleName

            val tvSingerName: TextView = itemView.findViewById(R.id.tv_singer_name)
            tvSingerName.text = mImusicItem.artistName

            val tvSongLength: TextView = itemView.findViewById(R.id.tv_song_length)
            tvSongLength.text =
                TimeParser.secToMin(mImusicItem.total_duration)
        }
    }
}

