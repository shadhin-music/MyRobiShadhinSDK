package com.myrobi.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper

internal class PDPSAdapter(val homePatchItemModel: HomePatchItemModel,val homeCallBack: HomeCallBack) : RecyclerView.Adapter<PDPSAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_show_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
//        var toConvert = homePatchItemModel.Data.toMutableList()
//        var items =  UtilHelper.getHomeDetailToMusic(toConvert)
        holder.itemView.setOnClickListener {
            //data.let { it1 -> clickCallback.onClickItem(it1, position) }
           homeCallBack.onClickItemAndAllItem(position, HomePatchItemModel(homePatchItemModel.Code,homePatchItemModel.ContentType,
             UtilHelper.getHomePatchDetailToPodcast(homePatchItemModel.Data as MutableList<HomePatchDetailModel>),homePatchItemModel.Design,homePatchItemModel.Name,homePatchItemModel.Sort,homePatchItemModel.Total,homePatchItemModel.customData))
            // homeCallBack.onClickItem(position, Track(data.get()) )
        }
    }

    override fun getItemCount(): Int {
        return homePatchItemModel.Data.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems() {
//            val textViewName = itemView.findViewById(R.id.txt_title) as TextView
//            val textViewArtist = itemView.findViewById(R.id.txt_name) as TextView
            val imageView = itemView.findViewById(R.id.image) as ImageView
            val url: String? =
                homePatchItemModel.Data[absoluteAdapterPosition].imageUrl?.let {
                    UtilHelper.getImageUrlSize300(it)
                }
            Log.e("TAG","DATA: " + homePatchItemModel.Data[absoluteAdapterPosition].imageUrl )
           // textViewArtist.text = homePatchItemModel.Data[absoluteAdapterPosition].titleName
            Glide.with(context)
                .load(url)
                .into(imageView)

           // textViewName.text = homePatchItemModel.Data[absoluteAdapterPosition].artistName
            // textViewArtist.text = data?.get(absoluteAdapterPosition)?.EpisodeName
//            val textViewName = itemView.findViewById(R.id.tv_person_name) as TextView
//            val imageView2 = itemView.findViewById(R.id.civ_person_image) as CircleImageView
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)
//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId
        }
    }
}