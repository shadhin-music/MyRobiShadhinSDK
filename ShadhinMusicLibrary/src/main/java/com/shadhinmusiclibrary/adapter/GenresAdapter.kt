package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel

internal class GenresAdapter(
    val homePatchItem: HomePatchItemModel,
    private val homeCallBack: HomeCallBack
) : RecyclerView.Adapter<GenresAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_big_video_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homeItemMod = homePatchItem.Data[position]
        holder.bindItems(homeItemMod)
        holder.itemView.setOnClickListener {
            homeCallBack.onClickItemAndAllItem(position, homePatchItem)
        }
    }

    override fun getItemCount(): Int {
        return homePatchItem.Data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        fun bindItems(homePathitem: HomePatchDetailModel) {
            val url: String = homePathitem.imageUrl ?: ""
            val imageView = itemView.findViewById(R.id.image) as ImageView
            Glide.with(context)
                .load(url)
                .into(imageView)
            itemView.setOnClickListener {
            }
//            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)
//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId
        }
    }
}