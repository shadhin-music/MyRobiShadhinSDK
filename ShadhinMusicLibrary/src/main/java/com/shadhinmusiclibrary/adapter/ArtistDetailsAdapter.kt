package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.utils.UtilHelper


internal class ArtistDetailsAdapter(val homePatchItem: HomePatchItemModel?) :
    RecyclerView.Adapter<ArtistDetailsAdapter.DataAdapterViewHolder>() {
    private val adapterData = mutableListOf<HomePatchDetailModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            VIEW_ARTIST_HEADER -> R.layout.my_bl_sdk_artist_details_header
            VIEW_DOWNLOAD -> R.layout.my_bl_sdk_latest_music_view_item
            VIEW_ALBUM -> R.layout.my_bl_sdk_item_release_patch
            VIEW_YOU_MIGHT_LIKE -> R.layout.my_bl_sdk_item_you_might_like
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return DataAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(adapterData[position])
        // holder.bind()
    }

    override fun getItemCount(): Int = adapterData.size

    override fun getItemViewType(position: Int): Int {
        return when (homePatchItem?.Design) {
            "Artist" -> VIEW_ARTIST_HEADER
//            is GenreDataModel.Artist -> VIEW_ARTIST_HEADER
//            is GenreDataModel.Artist2 -> VIEW_DOWNLOAD
//            is GenreDataModel.Artist3 -> VIEW_ALBUM
//            is GenreDataModel.Artist4 -> VIEW_YOU_MIGHT_LIKE

            else -> {
                throw IllegalArgumentException("Invalid view type")

            }
        }
    }

    fun setData(data: HomePatchDetailModel?) {
        adapterData.apply {
            clear()
            if (data != null) {
                add(data)
            }
        }
    }

    class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        private fun bindArtist(dataModel: HomePatchDetailModel) {

            val imageView: ImageView = itemView.findViewById(R.id.thumb)
//            var url: String =
            // val textArtist:TextView = itemView.findViewById(R.id.txt_name)
            //textArtist.setText(data.Data[absoluteAdapterPosition].Artist)
            // textView.setText(data.Data[absoluteAdapterPosition].title)
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(dataModel.imageUrl!!))
                .into(imageView)

//            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
//            recyclerView.layoutManager =
//                GridLayoutManager(itemView.context,2)
//            recyclerView.adapter = GenresAdapter()

        }

        private fun bindArtist2() {
//            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
//            recyclerView.layoutManager =
//                GridLayoutManager(itemView.context,2)
//            recyclerView.adapter = GenresAdapter()

        }

        private fun bindArtist3() {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            //  recyclerView.adapter = TopTrendingAdapter(data)
        }

        private fun bindArtist4() {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            // recyclerView.adapter = ArtistAdapter(data)
        }


        fun bind(dataModel: HomePatchDetailModel) {
            when (dataModel.artist_Id) {
                dataModel.artist_Id -> bindArtist(dataModel)
//                is GenreDataModel.Artist -> bindArtist()
//                is GenreDataModel.Artist2 -> bindArtist2()
//                is GenreDataModel.Artist3 -> bindArtist3()
//                is GenreDataModel.Artist4 -> bindArtist4()
//                is DataModel.BlOffers -> bindBlOffers(dataModel)
            }
        }
    }


    companion object {
        val VIEW_ARTIST_HEADER = 0
        val VIEW_DOWNLOAD = 1
        val VIEW_ALBUM = 2
        val VIEW_YOU_MIGHT_LIKE = 3
    }
}