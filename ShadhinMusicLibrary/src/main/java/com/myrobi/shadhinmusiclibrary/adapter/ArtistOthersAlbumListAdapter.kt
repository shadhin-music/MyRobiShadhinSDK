package com.myrobi.shadhinmusiclibrary.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.fragments.artist.ArtistAlbumModelData


internal class ArtistOthersAlbumListAdapter(
    val artistAlbumModel: MutableList<ArtistAlbumModelData>,
    private val homeCallBack: HomeCallBack
) :
    RecyclerView.Adapter<ArtistOthersAlbumListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_top_trending_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(artistAlbumModel[position])
        holder.itemView.setOnClickListener {
           // homeCallBack.onArtistAlbumClick(position, artistAlbumModel!!.data)
        }
    }

    override fun getItemCount(): Int {
        return artistAlbumModel?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context
        fun bindItems(artistAlbumModel: ArtistAlbumModelData) {
            val imageView: ShapeableImageView = itemView.findViewById(R.id.image)
            val textView: TextView = itemView.findViewById(R.id.txt_title)
            val url: String? =
                artistAlbumModel.imageUrl
            textView.text = artistAlbumModel.titleName
            val textViewArtist: TextView = itemView.findViewById(R.id.txt_name)
            textViewArtist.text = artistAlbumModel.artistName
            //Log.d("TAG","ImageUrl: " + url.replace("<\$size\$>","300"))
            Glide.with(mContext)
                .load(url?.replace("<\$size\$>", "300"))
                .into(imageView)
//            itemView.setOnClickListener {
//                val manager: FragmentManager = (mContext as AppCompatActivity).supportFragmentManager
//               manager.beginTransaction()
//                    .replace(R.id.container, ArtistAlbumsDetails2Fragment.newInstance(artistAlbumModel,
//                        artistAlbumModel!!.data[absoluteAdapterPosition]))
//                    .addToBackStack("Trending")
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






