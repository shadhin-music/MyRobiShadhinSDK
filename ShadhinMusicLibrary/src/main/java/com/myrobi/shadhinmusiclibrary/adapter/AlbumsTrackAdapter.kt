package com.myrobi.shadhinmusiclibrary.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.AnyTrackDiffCB
import com.myrobi.shadhinmusiclibrary.utils.TimeParser
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class AlbumsTrackAdapter(
    private val itemClickCB: CommonPlayControlCallback,
    private val bsDialogItemCallback: CommonBottomCallback,
    var cacheRepository: CacheRepository?
) : RecyclerView.Adapter<AlbumsTrackAdapter.ViewHolder>() {
    var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null
    private var contentId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_video_podcast_epi_single_item, parent, false)
        return ViewHolder(parentView!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mSongDetails = dataSongDetail[position]
        holder.bindItems(mSongDetails)
        holder.itemView.setOnClickListener {
            itemClickCB.onClickItem(dataSongDetail, position)
        }

        val ivSongMenuIcon: ImageView = holder.itemView.findViewById(R.id.iv_song_menu_icon)
        ivSongMenuIcon.setOnClickListener {
            val songDetail = dataSongDetail[position]
            bsDialogItemCallback.onClickBottomItem(songDetail)
        }

        if (mSongDetails.isPlaying) {
            holder.tvSongName?.setTextColor(
                ContextCompat.getColor(
                    holder.context,
                    R.color.my_sdk_color_primary
                )
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

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return dataSongDetail.size
    }

    fun setData(
        data: MutableList<SongDetailModel>,
        rootPatch: HomePatchDetailModel,
        mediaId: String?
    ) {
        this.dataSongDetail = mutableListOf()
        for (songItem in data) {
            dataSongDetail.add(
                UtilHelper.getMixdUpIMusicWithRootData(
                    songItem.apply {
                        isSeekAble = true
                    }, rootPatch
                )
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
            UtilHelper.getCurrentRunningSongToNewSongList(mediaId, dataSongDetail)
        val callback = AnyTrackDiffCB(dataSongDetail, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        dataSongDetail.clear()
        dataSongDetail.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tag: String? = null
        val context = itemView.getContext()
        var tvSongName: TextView? = null

        @SuppressLint("SetTextI18n")
        fun bindItems(dataSongDetail: IMusicModel) {
            val imageView: ShapeableImageView? = itemView.findViewById(R.id.siv_song_icon)
            val progressIndicator: CircularProgressIndicator =
                itemView.findViewById(R.id.progress)
            val downloaded: ImageView = itemView.findViewById(R.id.iv_song_type_icon)
            Glide.with(context)
                .load(dataSongDetail.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView!!)
            tvSongName = itemView.findViewById(R.id.tv_song_name)
            val textArtist: TextView = itemView.findViewById(R.id.tv_singer_name)
            val tvSongLength: TextView = itemView.findViewById(R.id.tv_song_length)
            tvSongName!!.text = dataSongDetail.titleName
            textArtist.text = dataSongDetail.artistName
            tvSongLength.text = TimeParser.secToMin(dataSongDetail.total_duration)
            progressIndicator.tag = dataSongDetail.content_Id
            downloaded.tag = 200
            progressIndicator.visibility = View.GONE
            downloaded.visibility = View.GONE
            val isDownloaded =
                cacheRepository?.isTrackDownloaded(dataSongDetail.content_Id) ?: false
            if (isDownloaded) {
                downloaded.visibility = View.VISIBLE
                progressIndicator.visibility = View.GONE
            }else{
                progressIndicator.visibility = View.GONE
                downloaded.visibility = View.GONE
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 2
    }
}