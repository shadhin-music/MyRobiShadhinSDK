package com.shadhinmusiclibrary.adapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.adapter.view_holder.BaseViewHolder
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.utils.UtilHelper


internal class MusicPlayAdapter(
    private val parentContext: Context
) :
    RecyclerView.Adapter<MusicPlayAdapter.MusicPlayVH>() {
    private var listMusicData: MutableList<IMusicModel>? = null
    private var finalWidth = 0
    private var mpHolder: MusicPlayVH? = null

    init {
        determinePhoneWidth()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicPlayVH {
        return MusicPlayVH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.my_bl_sdk_music_play_item_view, parent, false)
        )
    }

    fun setMusicData(data: MutableList<IMusicModel>) {
        this.listMusicData = data
        notifyDataSetChanged()
    }

    private fun determinePhoneWidth() {
        val x: Int = UtilHelper.getScreenHeightWidth(parentContext, 0)
        finalWidth = if (!isTablet()) {
            val y = x / 100
            val v = y * 30
            x - v
        } else {
            val fivePercent = x / 5
            fivePercent * 2
        }
    }

    private fun isTablet(): Boolean {
        return ((parentContext.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    override fun onBindViewHolder(holder: MusicPlayVH, position: Int) {
        holder.cvBannerParent.layoutParams.width = finalWidth
        holder.cvBannerParent.layoutParams.height = finalWidth

        holder.onBind(position)
        mpHolder = holder
    }

    override fun getItemCount(): Int {
        return listMusicData!!.size
    }

    fun getViewHolder(): MusicPlayVH? {
        return mpHolder
    }

    inner class MusicPlayVH(itemView: View) : BaseViewHolder(itemView) {
        val cvBannerParent: CardView =
            itemView.findViewById(R.id.cv_banner_parent)
        val ivCurrentPlayImage: ImageView =
            itemView.findViewById(R.id.iv_current_play_image)
        val ivCurrentPlayLiveImage: ImageView =
            itemView.findViewById(R.id.imgLive)
        lateinit var sMusicData: IMusicModel

        override fun onBind(position: Int) {
            super.onBind(position)
            sMusicData = listMusicData!![position]
            if (sMusicData.trackType=="LM"){
                ivCurrentPlayLiveImage.visibility =  VISIBLE
            }else{
                ivCurrentPlayLiveImage.visibility =  GONE
            }
            Glide.with(itemView.context)
                .load(sMusicData.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .transition(DrawableTransitionOptions().crossFade(500))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA))
                .placeholder(R.drawable.my_bl_sdk_default_song)
                .error(R.drawable.my_bl_sdk_default_song)
                .into(ivCurrentPlayImage)
        }
    }
}