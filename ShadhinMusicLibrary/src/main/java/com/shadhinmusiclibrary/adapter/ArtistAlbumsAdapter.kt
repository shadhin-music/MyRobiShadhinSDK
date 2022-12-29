package com.shadhinmusiclibrary.adapter

import android.annotation.SuppressLint
import android.util.Log
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.ArtistAlbumModel
import java.lang.Exception

internal class ArtistAlbumsAdapter(
    var homePatchItem: HomePatchItemModel?,
    val homeCallBack: HomeCallBack
) : RecyclerView.Adapter<ArtistAlbumsAdapter.ViewHolder>() {
    var artistAlbumModel: ArtistAlbumModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_item_you_might_like, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (homePatchItem?.Code != "") {
            holder.bindItems(homePatchItem, artistAlbumModel)
        }
//         holder.itemView.setOnClickListener {
//         }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE
    override fun getItemCount(): Int {
        return 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(artistAlbumModel: ArtistAlbumModel?) {
        this.artistAlbumModel = artistAlbumModel
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(homePatchItem: HomePatchItemModel?, artistAlbumModel: ArtistAlbumModel?) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            if(artistAlbumModel?.data.isNullOrEmpty() == true){
                tvTitle.visibility = GONE
            }
            tvTitle.text = "Albums"

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            try {

                recyclerView.adapter =
                    ArtistAlbumListAdapter(homePatchItem, artistAlbumModel, homeCallBack)

            } catch (e: Exception) {

            }
//            val textViewName = itemView.findViewById(R.id.txt_name) as TextView
//            val imageView2 = itemView.findViewById(R.id.image) as ImageView
//            itemView.setOnClickListener {
//                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
//                manager.beginTransaction()
//                    .replace(R.id.container , PlaylistFragment.newInstance())
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
        const val VIEW_TYPE = 3
    }
}