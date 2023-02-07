package com.myrobi.shadhinmusiclibrary.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.FeaturedPodcastDataModel
import com.myrobi.shadhinmusiclibrary.di.Module
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.fragments.podcast.PodcastDetailsCallback
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository


internal class PodcastSeeAllDetails2Adapter(
    val podcastDetailsCallback: PodcastDetailsCallback,
    val cacheRepository: CacheRepository,
    val favViewModel: FavViewModel,
    val injector: Module
) : RecyclerView.Adapter<PodcastSeeAllDetails2Adapter.DataAdapterViewHolder>() {
    private var listData: MutableList<FeaturedPodcastDataModel> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {

            VIEW_PP -> R.layout.my_bl_sdk_pp_type_podcast_layout
            VIEW_TN -> R.layout.my_bl_sdk_pp_type_podcast_layout
            VIEW_SS -> R.layout.my_bl_sdk_pp_type_podcast_layout
            VIEW_VP -> R.layout.my_bl_sdk_pp_type_podcast_layout
            VIEW_VL -> R.layout.my_bl_sdk_pp_type_podcast_layout
            VIEW_LE -> R.layout.my_bl_sdk_pp_type_podcast_layout
            VIEW_PS -> R.layout.my_bl_sdk_pp_type_podcast_layout
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return DataAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {

        holder.bind(listData.get(position))
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemViewType(position: Int): Int {
        return when (listData[position].PatchType) {
            "PP" -> VIEW_PP
            "TN" -> VIEW_TN
            "SS" -> VIEW_SS
//            "VP" -> VIEW_VP
            "VL" -> VIEW_VL
            "LE" -> VIEW_LE
            "PS" -> VIEW_PS


            else -> {
                -1
            }
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<FeaturedPodcastDataModel>) {
        val size = this.listData.size

        this.listData.addAll(data)
        val sizeNew = this.listData.size
        notifyItemRangeChanged(size, sizeNew)

    }

    inner class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context


        private fun bindPP(patchItem: FeaturedPodcastDataModel) {
//            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
//            seeAll.visibility = GONE
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = patchItem.PatchName
//            seeAll.setOnClickListener {
//                //PopularArtistsFragment
//                homeCallBack.onClickSeeAll(homePatchItem)
//            }

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PodcastPPTypeAdapter(patchItem,podcastDetailsCallback)
        }

        fun bindTN(patchItem: FeaturedPodcastDataModel) {

            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = patchItem.PatchName

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)

            Log.i("bindTN", "bindTN: ${patchItem.Data.size}")

            recyclerView.adapter = PodcastTNTypeAdapter(patchItem,podcastDetailsCallback, cacheRepository, favViewModel, injector)


        }
        fun bindSS(patchItem: FeaturedPodcastDataModel) {

            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = patchItem.PatchName

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PodcastSSTypeAdapter(patchItem.Data,podcastDetailsCallback)
        }

        private fun bindVL(patchItem: FeaturedPodcastDataModel) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = patchItem.PatchName

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PodcastLatestVideoAdapter(patchItem.Data,podcastDetailsCallback)
        }
        private fun bindPS(patchItem: FeaturedPodcastDataModel) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = patchItem.PatchName

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PodcastPopularShowsAdapter(patchItem.Data,podcastDetailsCallback)
        }
        private fun bindLE(patchItem: FeaturedPodcastDataModel) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = patchItem.PatchName

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = PodcastMoreEpisodeShowAdapter(patchItem.Data,podcastDetailsCallback)
        }
        fun bind(homePatchItemModel: FeaturedPodcastDataModel) {
            when (homePatchItemModel?.PatchType) {
                    "PP"-> bindPP(homePatchItemModel)
                    "TN"-> bindTN(homePatchItemModel)
                    "SS"-> bindSS(homePatchItemModel)
//                    "VP"-> bindVP(homePatchItemModel)
                    "VL"-> bindVL(homePatchItemModel)
                    "PS"-> bindPS(homePatchItemModel)
                    "LE"-> bindLE(homePatchItemModel)

            }


        }
    }



    public companion object {
        val VIEW_PP = 0
        val VIEW_TN = 1
        val VIEW_SS = 2
        val VIEW_VP = 3
        val VIEW_VL = 4
        val VIEW_LE = 5
        val VIEW_PS = 6
        const val VIEW_TYPE = 102
    }
}