package com.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.autoimageslider.SliderViewAdapter
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.utils.AppConstantUtils.BILLBOARD_IMAGE_URL

internal class SliderpagerAdapter(
    val homePatchDetailModel: MutableList<HomePatchDetailModel>,
    val homeCallBack: HomeCallBack,
    val homePatchItemModel: HomePatchItemModel
) :
    SliderViewAdapter<SliderpagerAdapter.SliderViewHolder>() {
var sliderList: MutableList<HomePatchDetailModel> = homePatchDetailModel
    override fun getCount(): Int {

        return homePatchDetailModel.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {

        val inflate: View =
            LayoutInflater.from(parent?.context).inflate(R.layout.my_bl_sdk_banner_image_layout, null)

        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {

        if (viewHolder != null) {
            val imageUrl = "$BILLBOARD_IMAGE_URL${sliderList[position].newBanner?.split("/")?.last()}"

            viewHolder.imageView?.alpha = 1f
            viewHolder.imageView?.scaleX = 1f
            viewHolder.imageView?.scaleY = 1f

            viewHolder.itemView?.let {
                viewHolder.imageView?.let { it1 ->
                    Glide.with(it).load(imageUrl)
                        .into(it1)
                }
            }
        }
        viewHolder?.imageView?.setOnClickListener {
            homeCallBack.onClickItemAndAllItem(position,homePatchItemModel)
        }
    }


    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {

        var imageView: ImageView? = itemView?.findViewById(R.id.imageViewMain)
    }
}