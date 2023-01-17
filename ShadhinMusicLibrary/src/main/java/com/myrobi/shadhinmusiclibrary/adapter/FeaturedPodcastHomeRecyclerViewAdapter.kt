package com.myrobi.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.FeaturedPodcastOnItemClickCallback
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel

internal class FeaturedPodcastHomeRecyclerViewAdapter(
    var cilckCallBack: FeaturedPodcastOnItemClickCallback,
    val featuredPodcastDataModel: List<FeaturedPodcastDataModel>?,
) : RecyclerView.Adapter<FeaturedPodcastHomeRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_item_you_might_like, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.bindItems(featuredPodcastDataModel)
    }

    override fun getItemCount(): Int {
        return featuredPodcastDataModel!!?.size!!
    }
//
//    @JvmName("setData1")
//    fun setData(data: List<FeaturedPodcastDetailsModel>?, showName: String?) {
//        this.data = data as MutableList<FeaturedPodcastDetailsModel>
//        this.showName = showName
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  context = itemView.getContext()
        fun bindItems(item: List<FeaturedPodcastDataModel>?) {
            val textViewName = itemView.findViewById(R.id.tvTitle) as TextView
            textViewName.text = item?.get(absoluteAdapterPosition)?.PatchName
            Log.e("TAG","DATA: "+ (item?.get(absoluteAdapterPosition) ?: 0))
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(
                context,
                2,
                RecyclerView.HORIZONTAL,
                false
            )
            recyclerView.adapter =
                FeaturedPodcastAdapter(item?.get(absoluteAdapterPosition)?.Data as MutableList<FeaturedPodcastDetailsModel>, cilckCallBack)
//            val textViewName = itemView.findViewById(R.id.txt_title) as TextView
//            val textViewArtist = itemView.findViewById(R.id.txt_name) as TextView
//            val imageView = itemView.findViewById(R.id.image) as ImageView
//           val url: String? = data?.get(absoluteAdapterPosition)?.getImageUrl300Size()
//            textViewArtist.text = data?.get(absoluteAdapterPosition)?.TrackName
//            Glide.with(context)
//                .load(url)
//                .into(imageView)
//            textViewName.text= data?.get(absoluteAdapterPosition)?.EpisodeName
           // textViewArtist.text = data?.get(absoluteAdapterPosition)?.EpisodeName
//            val textViewName = itemView.findViewById(R.id.tv_person_name) as TextView
//            val imageView2 = itemView.findViewById(R.id.civ_person_image) as CircleImageView
//            itemView.setOnClickListener {
//                val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
//                manager.beginTransaction()
//                    .replace(R.id.container ,PodcastDetailsFragment.newInstance())
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
}