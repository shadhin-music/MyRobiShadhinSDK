package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.SearchClickCallBack
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel

import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class ArtistAdapter(
    var homePatchItem: HomePatchItemModel?,
    private val homeCallBack: HomeCallBack,
    var artistIDtoSkip: String? = null
) : RecyclerView.Adapter<ArtistAdapter.ViewHolder>() {

    private var filteredHomePatchItem: HomePatchItemModel? = null

    init {
        initialize()
    }

    fun initialize() {
        var items = homePatchItem?.Data
        items = items?.filter { it.artist_Id != artistIDtoSkip }
        filteredHomePatchItem = homePatchItem?.copy()
        if (items != null) {
            filteredHomePatchItem?.Data = items
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_artist_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.itemView.setOnClickListener {
            val artistID = filteredHomePatchItem!!.Data[position].artist_Id
            val index = homePatchItem!!.Data.indexOfFirst { it.artist_Id == artistID }
            homeCallBack.onClickItemAndAllItem(index, homePatchItem!!)
        }
    }

    override fun getItemCount(): Int {
        return filteredHomePatchItem?.Data?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
            val imageView2 = itemView.findViewById(R.id.image) as CircleImageView

            Glide.with(mContext)
                .load(
                    UtilHelper.getImageUrlSize300(
                        filteredHomePatchItem!!.Data[absoluteAdapterPosition].imageUrl ?: ""
                    )
                )
                .into(imageView2)
            textViewName.text = filteredHomePatchItem!!.Data[absoluteAdapterPosition].artistName
        }
    }
}