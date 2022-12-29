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
            val image = sliderList.get(position).bannerImage
          //  Log.e("TAG","DATA: "+ sliderList.get(position).imageUrl)
            viewHolder?.itemView?.let {
                viewHolder?.imageView?.let { it1 ->
                    Glide.with(it).load(image?.replace("<\$size\$>", "984")).fitCenter()
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