package com.shadhinmusiclibrary.adapter

import com.shadhinmusiclibrary.callBackService.HomeCallBack


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.HomePatchItemModel


internal class BottomsheetArtistsYouMightLikeAdapter(
    var homePatchItem: HomePatchItemModel?,
    val homeCallBack: HomeCallBack,
    var artistIDToSkip: String? = null
) : RecyclerView.Adapter<BottomsheetArtistsYouMightLikeAdapter.ViewHolder>() {

    var adapter: ArtistAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_item_you_might_like, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(homePatchItem)


    }
    override fun getItemViewType(position: Int) = VIEW_TYPE
    override fun getItemCount(): Int {
        return 1
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  context = itemView.getContext()
        fun bindItems(homePatchItem: HomePatchItemModel?) {
            val textView: TextView = itemView.findViewById(R.id.tvTitle)
            textView.text = "You might like also"
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ArtistAdapter(homePatchItem, homeCallBack = homeCallBack, artistIDToSkip)
            recyclerView.adapter = adapter

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
        const val VIEW_TYPE = 4
    }
}






