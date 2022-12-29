package com.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.HomeDataModel


internal class PopularPodcastAdapter() : RecyclerView.Adapter<PopularPodcastAdapter.DataAdapterViewHolder>() {
    private val adapterData = mutableListOf<HomeDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {

            VIEW_TRENDING_PODCAST ->  R.layout.my_bl_sdk_item_you_might_like
            VIEW_VIDEO_PODCASTS -> R.layout.my_bl_sdk_item_genres
            VIEW_BHOOT -> R.layout.my_bl_sdk_item_you_might_like
            VIEW_POPULAR_SHOWS-> R.layout.my_bl_sdk_item_you_might_like
            VIEW_MORE_VIDEO ->R.layout.my_bl_sdk_item_genres
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return DataAdapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(adapterData[position])
    }

    override fun getItemCount(): Int = adapterData.size

    override fun getItemViewType(position: Int): Int {
        return when (adapterData[position]) {

//            is GenreDataModel.Artist -> VIEW_TRENDING_PODCAST
//            is GenreDataModel.Artist2 -> VIEW_VIDEO_PODCASTS
//            is GenreDataModel.Artist3 -> VIEW_BHOOT
//            is GenreDataModel.Artist4 -> VIEW_POPULAR_SHOWS

            else -> {
                throw IllegalArgumentException("Invalid view type")

            }
        }
    }

//    fun setData(data: List<GenreDataModel>) {
//        adapterData.apply {
//            clear()
//            addAll(data)
//        }
//    }

    class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun bindArtist() {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false )
          //  recyclerView.adapter = TopTrendingAdapter(data)

        }

        private fun bindArtist2() {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false )
            recyclerView.adapter = PopularVideoPodcastAdapter()

//            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
//            recyclerView.layoutManager =
//                GridLayoutManager(itemView.context,2)
//            recyclerView.adapter = GenresAdapter()

        }
        private fun bindArtist3() {
//            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
//            recyclerView.layoutManager =
//                GridLayoutManager(itemView.context,)
//            recyclerView.adapter = GenresAdapter()

        }


        fun bind(dataModel: HomeDataModel) {
            when (dataModel) {
//                is GenreDataModel.Artist -> bindArtist()
//                is GenreDataModel.Artist2 -> bindArtist2()
//                is GenreDataModel.Artist3 -> bindArtist2()
//                is GenreDataModel.Artist4 -> bindArtist2()

//                is DataModel.BlOffers -> bindBlOffers(dataModel)


            }
        }
    }


    companion object {

        val VIEW_TRENDING_PODCAST = 0
        val VIEW_VIDEO_PODCASTS = 2
        val VIEW_BHOOT = 3
        val VIEW_POPULAR_SHOWS = 4
        val VIEW_MORE_VIDEO = 5

    }

}