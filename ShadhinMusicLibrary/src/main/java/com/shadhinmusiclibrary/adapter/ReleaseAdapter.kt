package com.shadhinmusiclibrary.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.utils.UtilHelper


internal class ReleaseAdapter(
    val homePatchItem: HomePatchItemModel,
    private val homeCallBack: HomeCallBack
) :
    RecyclerView.Adapter<ReleaseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_release_item_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homePatchDet = homePatchItem.Data[position]
        holder.bindItems(homePatchDet)
        holder.itemView.setOnClickListener {
            homeCallBack.onClickItemAndAllItem(position, homePatchItem)
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE
    override fun getItemCount(): Int {
        return homePatchItem.Data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems(homePatDetMod: HomePatchDetailModel) {
            val imageView: ShapeableImageView = itemView.findViewById(R.id.siv_song_icon)
            val textView: TextView = itemView.findViewById(R.id.tv_singer_name)
            val textViewArtist: TextView = itemView.findViewById(R.id.tv_song_length)

            val url: String? = homePatDetMod.imageUrl
            textView.text = homePatDetMod.titleName
            textViewArtist.text = homePatDetMod.artistName
            Glide.with(mContext)
                .load(url?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
        }
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}