package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R

internal class VideoPodcastHeaderAdapter : RecyclerView.Adapter<VideoPodcastHeaderAdapter.PodcastHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastHeaderViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_podcast_header_layout, parent, false)
        return PodcastHeaderViewHolder(v)
    }


    override fun onBindViewHolder(holder:PodcastHeaderViewHolder, position: Int) {
         holder.bindItems()


    }
    override fun getItemViewType(position: Int) = VIEW_TYPE
    override fun getItemCount(): Int {
        return 1
    }

    inner class PodcastHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  context = itemView.getContext()
        fun bindItems() {

//            val textViewName = itemView.findViewById(R.id.tv_person_name) as TextView
//            val imageView2 = itemView.findViewById(R.id.civ_person_image) as CircleImageView
//            itemView.setOnClickListener {
//                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
//                manager.beginTransaction()
//                    .replace(R.id.container , ArtistDetailsFragment.newInstance())
//                    .commit()
//            }
//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)

//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId


        }

    }
    companion object {
        const val VIEW_TYPE = 1
    }
}