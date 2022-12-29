package com.shadhinmusiclibrary.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.SearchItemCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.model.search.CommonSearchData
import com.shadhinmusiclibrary.utils.UtilHelper


internal class SearchAlbumsAdapter(
    val searchCallBack: SearchItemCallBack
) :
    RecyclerView.Adapter<SearchAlbumsAdapter.ViewHolder>() {
    private var searchAlbumData: MutableList<IMusicModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_search_album_layout, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchAlbumItem = searchAlbumData[position]
        holder.bindItems(searchAlbumItem)

        holder.itemView.setOnClickListener {
            searchCallBack.onClickSearchItem(searchAlbumItem)
        }
    }

    fun setSearchAlbums(rootModel: CommonSearchData) {
        this.searchAlbumData = UtilHelper.getSearchDataToRootData(rootModel)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return searchAlbumData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        fun bindItems(searchAlbumdata: IMusicModel) {
            val imageView: ImageView = itemView.findViewById(R.id.thumb)
            val textTitle: TextView = itemView.findViewById(R.id.title)
            Glide.with(context)
                .load(searchAlbumdata.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(imageView)
            val textArtist: TextView = itemView.findViewById(R.id.similarArtist)
            //  val textDuration: TextView = itemView.findViewById(R.id.tv_song_length)
            textTitle.text = searchAlbumdata.titleName
            textArtist.text = searchAlbumdata.artistName
            // textDuration.text = TimeParser.secToMin(dataSongDetail.duration)
            //Log.e("TAG","DATA123: "+ artistContent?.image)

//            val linearLayout: LinearLayout = itemView.findViewById(R.id.linear)
//            entityId = banner.entityId
            //getActorName(entityId!!)
//            //textViewName.setText(banner.name)
//            textViewName.text = LOADING_TXT
//            textViewName.tag = banner.entityId
        }
    }
}






