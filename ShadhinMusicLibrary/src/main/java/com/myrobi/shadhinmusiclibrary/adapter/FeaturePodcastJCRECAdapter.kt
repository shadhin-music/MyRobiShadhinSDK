package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.FeaturedPodcastOnItemClickCallback
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel

internal class FeaturePodcastJCRECAdapter(var cilckCallBack: FeaturedPodcastOnItemClickCallback) :
    RecyclerView.Adapter<FeaturePodcastJCRECAdapter.ViewHolder>() {
    var data: MutableList<FeaturedPodcastDetailsModel> = mutableListOf()
    var showName: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.my_bl_sdk_item_you_might_like, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    @JvmName("setData1")
    fun setData(data: List<FeaturedPodcastDetailsModel>?, showName: String) {
        this.data = data as MutableList<FeaturedPodcastDetailsModel>
        this.showName = showName
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()

        fun bindItems() {
            val textViewName = itemView.findViewById(R.id.tvTitle) as TextView
            textViewName.text = showName
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(
                context,
                2,
                RecyclerView.HORIZONTAL,
                false
            )
            recyclerView.adapter = FeaturedPodcastJCAdapter(data, cilckCallBack)
            //ArtistAlbumListAdapter(homePatchItem!!, artistAlbumModel, homeCallBack)
//            val textViewName = itemView.findViewById(R.id.tvTitle) as TextView
//            textViewName.text = data[1].ShowName
//            val imageView2 = itemView.findViewById(R.id.image) as ImageView
//            itemView.setOnClickListener {
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
        const val VIEW_TYPE = 15
    }
}






