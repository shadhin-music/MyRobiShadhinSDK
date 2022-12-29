package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.adapter.view_holder.BaseViewHolder
import com.shadhinmusiclibrary.callBackService.RadioTrackCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.utils.UtilHelper

internal class RadioTrackAdapter(private val radioCallback: RadioTrackCallBack) :
    RecyclerView.Adapter<RadioTrackAdapter.RadioTrackVH>() {

    private var listRadioTrack: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioTrackVH {
        return RadioTrackVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_music_radio_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RadioTrackVH, position: Int) {
        holder.onBind(position)
        val radioTrackItem = listRadioTrack[position]
        radioCallback.getCurrentVH(holder, listRadioTrack)
        holder.itemView.setOnClickListener {
            radioCallback.onClickItem(radioTrackItem)
        }
    }

    override fun getItemCount(): Int {
        return listRadioTrack.size
    }

    fun setRadioTrackData(
        dataRadios: MutableList<HomePatchDetailModel>,
        mediaId: String?,
        globalRootConId: String
    ) {
        this.listRadioTrack = mutableListOf()
        for (songItem in dataRadios) {
            listRadioTrack.add(
//                UtilHelper.getHomeRadioSong(
                songItem.apply {
                    isSeekAble = false
                    rootContentId = globalRootConId
                }
//                )
            )
        }

        notifyDataSetChanged()
    }

    inner class RadioTrackVH(itemView: View) : BaseViewHolder(itemView) {
        var ivRadioPlay: ImageView? = null
        var rootId: String = ""
        var sivRadioIcon: ShapeableImageView?
        var tvRadioSongName: TextView?

        init {
            ivRadioPlay = itemView.findViewById(R.id.iv_radio_play)
            sivRadioIcon = itemView.findViewById(R.id.siv_radio_icon)
            tvRadioSongName = itemView.findViewById(R.id.tv_radio_song_name)

        }

        override fun onBind(position: Int) {
            val mSongDetMod = listRadioTrack[position]
            rootId = mSongDetMod.content_Id
            sivRadioIcon?.let {
                Glide.with(itemView.context)
                    .load(mSongDetMod.imageUrl?.let { it1 -> UtilHelper.getImageUrlSize300(it1) })
                    .into(it)
            }
            tvRadioSongName?.text = mSongDetMod.titleName
        }
    }
}