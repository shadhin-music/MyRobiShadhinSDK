package com.shadhinmusiclibrary.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R


internal class HomeFooterAdapter : RecyclerView.Adapter<HomeFooterAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_powered_by, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.bindItems(bannerList[position])


    }
    override fun getItemViewType(position: Int)= VIEW_TYPE
    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {

//            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
//            val imageView2 = itemView.findViewById(R.id.image) as ImageView
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)

//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId


        }

    }
    companion object {
        const val VIEW_TYPE = 20
    }
}






