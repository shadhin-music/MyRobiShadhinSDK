package com.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.autoimageslider.SliderViewAdapter
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.fragments.home.newReleaseTrackCallback
import com.shadhinmusiclibrary.utils.TimeParser
import com.shadhinmusiclibrary.utils.UtilHelper

internal class NewReleaseSliderpagerAdapter(
    val homePatchDetailModel: MutableList<HomePatchDetailModel>,
    val homeCallBack: HomeCallBack,
    val homePatchItemModel: HomePatchItemModel,
    val newReleaseTrackCallback: newReleaseTrackCallback
) :
    SliderViewAdapter<NewReleaseSliderpagerAdapter.ViewHolder>() {
var sliderList: MutableList<HomePatchDetailModel> = homePatchDetailModel
    override fun getCount(): Int {

        return homePatchDetailModel.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {

        val inflate: View =
            LayoutInflater.from(parent?.context).inflate(R.layout.new_release_slider_layout, null)

        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {

        if (viewHolder != null) {
            val image = sliderList.get(position).imageUrl
            val banner = sliderList.get(position).bannerImage

            var toConvert = homePatchItemModel.Data.toMutableList()
            var root = HomePatchDetailModel()
            root.content_Id = toConvert.get(position).content_Id
            root.content_Type = "A"
            root.imageUrl = toConvert.get(position).imageUrl
            var items =  UtilHelper.getHomeDetailToMusic(toConvert)
           viewHolder.item= homePatchItemModel.Data[position]
            newReleaseTrackCallback.getCurrentVH(viewHolder,items)
//            viewHolder.imageView?.alpha = 1f
//            viewHolder.imageView?.scaleX = 1f
//            viewHolder.imageView?.scaleY = 1f
            viewHolder.artistName?.text = sliderList.get(position).artistName
            viewHolder.time?.text=   TimeParser.secToMin(sliderList.get(position).total_duration)
            viewHolder.title?.text = "New release from"
            viewHolder.trackName?.text = sliderList.get(position).titleName
            Log.e("TAG","DATA: "+ sliderList.get(position).imageUrl)
            viewHolder.itemView?.let {
                viewHolder.imageView?.let { it1 ->
                    Glide.with(it).load(UtilHelper.getImageUrlSize300(banner.toString()))
                        .into(it1)
                }
            }
            viewHolder.itemView?.let {
                viewHolder.imageView2?.let { it1 ->
                    Glide.with(it).load(UtilHelper.getImageUrlSize300(image.toString()))
                        .into(it1)
                }
            }
        }
        viewHolder?.itemView?.setOnClickListener {
            homeCallBack.onClickItemAndAllItem(position,homePatchItemModel)
        }
        viewHolder?.ivPlayBtn?.setOnClickListener {
            var toConvert = homePatchItemModel.Data.toMutableList()
            var root = HomePatchDetailModel()
            root.content_Id = toConvert.get(position).content_Id
            root.content_Type = "A"
            root.imageUrl = toConvert.get(position).imageUrl
            var items =  UtilHelper.getHomeDetailToMusic(toConvert)
            newReleaseTrackCallback.onTrackClick(items,position)
        }
    }


    inner class ViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {
        var item:HomePatchDetailModel ?= null
        var ivPlayBtn: ImageView? = itemView?.findViewById(R.id.btn_play_pause)
        var imageView: ImageView? = itemView?.findViewById(R.id.artist_img)
        var imageView2: ImageView? = itemView?.findViewById(R.id.image)
       // var imageViewPlay:ImageView?= itemView?.findViewById(R.id.btn_play_pause)
        var trackName: TextView? = itemView?.findViewById(R.id.track_name)
        var time:TextView? = itemView?.findViewById(R.id.duration)
        var title:TextView? = itemView?.findViewById(R.id.title)
        var artistName: TextView? = itemView?.findViewById(R.id.artist_name)
    }
}