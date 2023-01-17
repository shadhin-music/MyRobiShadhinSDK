package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.PatchCallBack
import com.myrobi.shadhinmusiclibrary.data.model.PodcastDetailsModel
import com.myrobi.shadhinmusiclibrary.utils.CircleImageView
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class FeaturedPopularArtistAdapter(
    val homePatchItem1: List<PodcastDetailsModel>,
    private val homeCallBack: PatchCallBack
) : RecyclerView.Adapter<FeaturedPopularArtistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_layout_circle_image_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mPodcastDetMod = homePatchItem1[position]
        holder.bindItems(mPodcastDetMod)
        holder.itemView.setOnClickListener {
//            val homePatchItem = homePatchItem1[position]
            homeCallBack.onClickItemAndAllItem(position, homePatchItem1)
        }
    }

    override fun getItemCount(): Int {
        return homePatchItem1.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context

        fun bindItems(mPodcastMod: PodcastDetailsModel) {
            val textViewName = itemView.findViewById(R.id.tv_person_name) as TextView
            val imageView = itemView.findViewById(R.id.civ_person_image) as CircleImageView
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(mPodcastMod.Image ?: ""))
                .into(imageView)
            textViewName.text = mPodcastMod.ArtistName
            itemView.setOnClickListener {
            }
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
//            getActorName(entityId!!)
//            textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId
        }
    }
}