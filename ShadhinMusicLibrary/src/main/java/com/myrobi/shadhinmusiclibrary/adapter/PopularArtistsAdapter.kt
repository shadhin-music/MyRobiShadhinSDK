package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.adapter.view_holder.BaseViewHolder
import com.myrobi.shadhinmusiclibrary.utils.CircleImageView

/**
 * Rezaul Khan
 * https://github.com/rezaulkhan111
 **/
internal class PopularArtistsAdapter() :
    RecyclerView.Adapter<PopularArtistsAdapter.PopularArtistsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularArtistsVH {
        return PopularArtistsVH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.my_bl_sdk_layout_circle_image_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularArtistsVH, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return 1
    }

    internal class PopularArtistsVH(itemView: View) : BaseViewHolder(itemView) {
//        val clCircleImageParent: ConstraintLayout =
//            itemView.findViewById(R.id.cl_circle_image_parent)
        private val civPersonImage: CircleImageView = itemView.findViewById(R.id.civ_person_image)
        private val tvPersonName: TextView = itemView.findViewById(R.id.tv_person_name)

//        override fun onBind(position: List<TopTrendingdata>) {
//            super.onBind(position)
//        }
    }
}