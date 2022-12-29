package com.shadhinmusiclibrary.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R


internal class PodcastTrackHeaderAdapter() : RecyclerView.Adapter<PodcastTrackHeaderAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_header_layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()


    }

    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems() {
            val textView= itemView.findViewById(R.id.tvTitle) as TextView
            textView.text ="Episodes"
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

}






