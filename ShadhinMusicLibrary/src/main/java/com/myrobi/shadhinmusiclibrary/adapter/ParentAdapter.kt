package com.myrobi.shadhinmusiclibrary.adapter



import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.myrobi.shadhinmusiclibrary.R

import com.myrobi.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.myrobi.shadhinmusiclibrary.autoimageslider.SliderView
import com.myrobi.shadhinmusiclibrary.callBackService.DownloadClickCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.myrobi.shadhinmusiclibrary.callBackService.SearchClickCallBack
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.fragments.home.NewReleaseTrackCallback



internal class ParentAdapter(
    var homeCallBack: HomeCallBack,
    val searchCb: SearchClickCallBack,
    val downloadClickCallBack: DownloadClickCallBack,
    val podcastTrackClick: PodcastTrackCallback,
    val newReleaseTrackCallback: NewReleaseTrackCallback
) : ListAdapter<HomePatchItemModel,ParentAdapter.DataAdapterViewHolder>(ParentAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            VIEW_SEARCH -> R.layout.my_bl_sdk_item_search
            VIEW_ARTIST -> R.layout.my_bl_sdk_item_artist
            VIEW_PLAYLIST -> R.layout.my_bl_sdk_item_playlist
            VIEW_RELEASE -> R.layout.my_bl_sdk_item_release_patch
            VIEW_POPULAR_PODCAST -> R.layout.my_bl_sdk_item_release_patch
            VIEW_TRENDING_MUSIC_VIDEO -> R.layout.my_bl_sdk_item_trending_music_videos
            VIEW_PODCAST_LIVE -> R.layout.my_bl_sdk_item_bhoot_podcast
            VIEW_DOWNLOAD -> R.layout.download_fav_playlist_layout
//            VIEW_POPULAR_AMAR_TUNES -> R.layout.my_bl_sdk_item_popular_amar_tunes
            VIEW_SHOW ->R.layout.my_bl_sdk_item_release_patch
            VIEW_BANNER ->R.layout.my_bl_sdk_banner
            VIEW_DISCOVER -> R.layout.billboard_layout
            VIEW_PDPS -> R.layout.my_bl_sdk_item_release_patch
            VIEW_LARGE_VIDEO -> R.layout.my_bl_sdk_item_release_patch
            VIEW_PODCAST_VIDEO -> R.layout.my_bl_sdk_item_release_patch
            VIEW_NEW_RELEASE_AUDIO->R.layout.billboard_layout
            VIEW_RADIO ->R.layout.my_bl_sdk_item_release_patch
            else -> R.layout.my_bl_sdk_item_empty
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return DataAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).Design) {
            "search" -> VIEW_SEARCH
            "Artist" -> VIEW_ARTIST
            "Playlist" -> VIEW_PLAYLIST
            "Release" -> VIEW_RELEASE
            "Track" -> VIEW_RELEASE
            "Podcast" -> VIEW_POPULAR_PODCAST
            "SmallVideo" -> VIEW_TRENDING_MUSIC_VIDEO
//            "amarTune" -> VIEW_POPULAR_AMAR_TUNES
            "download" -> VIEW_DOWNLOAD
            "PodcastLive" -> VIEW_PODCAST_LIVE
            "Show" -> VIEW_SHOW
            "Discover"-> VIEW_DISCOVER
            "PDPS"-> VIEW_PDPS
            "PodcastVideo"-> VIEW_LARGE_VIDEO
            "NewReleaseAudio" ->VIEW_NEW_RELEASE_AUDIO
            "LargeVideo" -> VIEW_LARGE_VIDEO
            "Radio" -> VIEW_RADIO
            else -> -1

        }
    }


    inner class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mContext = itemView.context

        private fun bindSearch(homePatchItemModel: HomePatchItemModel) {
            val search: TextView = itemView.findViewById(R.id.sv_search_input)
            search.setOnClickListener {
                //call back SearchFragment
                search.isEnabled = false
                searchCb.clickOnSearchBar(homePatchItemModel)
                search.isEnabled = true
            }
        }

        private fun bindArtist(homePatchItem: HomePatchItemModel, homeCallBack: HomeCallBack) {
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = homePatchItem.Name
            seeAll.setOnClickListener {
                //PopularArtistsFragment
                homeCallBack.onClickSeeAll(homePatchItem)
            }

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = ArtistAdapter(homePatchItem, homeCallBack)
        }

        fun bindRelease(homePatchItem: HomePatchItemModel) {
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = homePatchItem.Name
            seeAll.setOnClickListener {
                //TopTrendingFragment
                homeCallBack.onClickSeeAll(homePatchItem)
            }
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = HomeReleaseAdapter(homePatchItem, homeCallBack)
        }

        private fun bindPlaylist(homePatchItem: HomePatchItemModel) {
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = homePatchItem.Name
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItem)
            }
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = HomeContentPlaylistAdapter(homePatchItem, homeCallBack)
        }

        private fun bindPopularPodcast(homePatchItem: HomePatchItemModel) {
            for (hoPatItem in homePatchItem.Data) {
                hoPatItem.apply {
                    isSeekAble = true
                }
            }
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            title.text = homePatchItem.Name
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter =
                HomePodcastAdapter(homePatchItem, homeCallBack, podcastTrackClick)
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            seeAll.visibility = GONE
            seeAll.setOnClickListener {
                //PodcastDetailsFragment
                homeCallBack.onClickSeeAll(homePatchItem)
            }

        }

        private fun bindPopularAmarTunes(
            homePatchItem: HomePatchItemModel,
        ) {
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            title.text = homePatchItem.Name
            val image: ShapeableImageView = itemView.findViewById(R.id.image)
            Glide.with(itemView.context).load(homePatchItem.Data[0].imageUrl).into(image)
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            seeAll.setOnClickListener {
                //  homeCallBack.onClickSeeAll(homePatchItem)
                // Log.d("TAG","CLICK ITEM: "+ homePatchItem)
            }
            itemView.setOnClickListener {
                ShadhinMusicSdkCore.openPatch(itemView.context, "BNMAIN01")
            }
        }


        private fun bindLargeVideos(
            homePatchItem: HomePatchItemModel,
        ) {
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            title.text = homePatchItem.Name

            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItem)
                Log.e("TAG","PATCH: "+ homePatchItem)
            }
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = ParentLargeVideosAdapter(
                homePatchItem,
                homePatchDetail = getItem(absoluteAdapterPosition).Data
            )
        }
        private fun bindBhoot(homePatchItemModel: HomePatchItemModel) {

            val image: ShapeableImageView = itemView.findViewById(R.id.image)
            val imageurl = homePatchItemModel.Data[0].imageWeb.toString()
            Glide.with(mContext)
                .load(imageurl.replace("<\$size\$>", "984"))
                .into(image)
            itemView.setOnClickListener {
                homeCallBack.onClickItemAndAllItem(0, homePatchItemModel)
            }

        }



        private fun bindDownload(homePatchItemModel: HomePatchItemModel) {
            val cardItem: View = itemView.findViewById(R.id.ProConstraint)
            val download: View = itemView.findViewById(R.id.Download)
            val watchlater: View = itemView.findViewById(R.id.WatchLater)
            val playlist: View = itemView.findViewById(R.id.Playlists)
            val favorite: View = itemView.findViewById(R.id.Fav)
            val artist: View = itemView.findViewById(R.id.Artist)
            val podcast: View = itemView.findViewById(R.id.Podcast)

            val proText: TextView = itemView.findViewById(R.id.Protext)
            val proSubtext: TextView = itemView.findViewById(R.id.ProSubtext)
            val arrowNtn: ImageView = itemView.findViewById(R.id.arrow_btn)
            val getPro: TextView = itemView.findViewById(R.id.textView2)


            val customData = homePatchItemModel.customData

            if(customData !=null && customData is Plan && customData.status == Status.SUBSCRIBED  ){
                proText.text = "${customData.type?.name?:"Others"} Plan Activate"
                arrowNtn.visibility = View.VISIBLE
                getPro.visibility = View.GONE
            }


            download.setOnClickListener {
                downloadClickCallBack.clickOnDownload(homePatchItemModel)
            }
            watchlater.setOnClickListener {
                downloadClickCallBack.clickOnWatchLater(homePatchItemModel)
            }
            playlist.setOnClickListener {
                downloadClickCallBack.clickOnMyPlaylist(homePatchItemModel)
            }
            favorite.setOnClickListener {
                downloadClickCallBack.clickOnMyFavorite(homePatchItemModel)
            }
            artist.setOnClickListener{
            //    Log.e("TAG","DATA: " + homePatchItemModel)
              //  ShadhinMusicSdkCore.openPatch(itemView.context, "RC203")
                //homeCallBack.onClickSeeAll(homePatchItemModel)
                downloadClickCallBack.clickOnArtist(homePatchItemModel)
            }
            podcast.setOnClickListener{
                Log.e("TAG","DATA: " + homePatchItemModel)
                downloadClickCallBack.clickOnPodcast(homePatchItemModel)
            }
            cardItem.setOnClickListener {
                it.findNavController().navigate(R.id.to_subscription)
            }


        }

        private fun bindPodcastShow(homePatchItemModel: HomePatchItemModel) {
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = homePatchItemModel.Name
            seeAll.visibility = GONE
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItemModel)
               // Log.e("TAG","PATCH: "+ homePatchItemModel)
            }

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
             recyclerView.adapter = PodcastShowTypeAdapter(homePatchItemModel, homeCallBack)
        }

        private fun bindBanner(homePatchItemModel: HomePatchItemModel) {

            val sliderView: SliderView = itemView.findViewById(R.id.imageSlider)
            val sliderAdapter: SliderpagerAdapter = SliderpagerAdapter(homePatchItemModel.Data as MutableList, homeCallBack, homePatchItemModel)
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(sliderAdapter)
            sliderView.setIndicatorEnabled(true)
            sliderView.scrollTimeInSec = 2
            sliderView.isAutoCycle = true
            sliderView.startAutoCycle()

        }
        private fun bindBillboard(homePatchItemModel: HomePatchItemModel) {

            val sliderView: SliderView = itemView.findViewById(R.id.imageSlider)
            val sliderAdapter: SliderpagerAdapter = SliderpagerAdapter(homePatchItemModel.Data as MutableList, homeCallBack, homePatchItemModel)
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(sliderAdapter)
            sliderView.setIndicatorEnabled(true)
            sliderView.scrollTimeInSec = 2
            sliderView.isAutoCycle = true
            sliderView.startAutoCycle()

        }
        private fun bindLatestRelease() {
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            title.text = "Latest Release"
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            // recyclerView.adapter = TopTrendingAdapter(data)
        }

        private fun bindBlOffers() {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = MyBLOffersAdapter()
        }


        private fun bindPDPS(homePatchItemModel: HomePatchItemModel) {
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = homePatchItemModel.Name
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)

            recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                2,
                RecyclerView.HORIZONTAL,
                false
            )
            recyclerView.adapter = PDPSAdapter(homePatchItemModel, homeCallBack)
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
             seeAll.visibility = GONE
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItemModel)
              //  Log.e("TAG","PATCH: "+ homePatchItemModel)
            }
        }
        private fun bindTrendingMusic(homePatchItemModel: HomePatchItemModel) {
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            title.text = homePatchItemModel.Name

            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItemModel)
            }
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = TopTrendingVideosAdapter(
                homePatchItemModel,
                homePatchDetail = getItem(absoluteAdapterPosition).Data
            )
        }
        private fun bindNewReleaseAudio(homePatchItemModel: HomePatchItemModel){
            for (hoPatItem in homePatchItemModel.Data) {
                hoPatItem.apply {
                    isSeekAble = true
                }
            }

            val sliderView: SliderView = itemView.findViewById(R.id.imageSlider)
            val sliderAdapter: NewReleaseSliderpagerAdapter = NewReleaseSliderpagerAdapter(homePatchItemModel.Data as MutableList, homeCallBack, homePatchItemModel,newReleaseTrackCallback)
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(sliderAdapter)
            sliderView.setIndicatorEnabled(true)
            sliderView.scrollTimeInSec = 5
            sliderView.isAutoCycle = false

        }

        private fun bindRadio(homePatchItemModel: HomePatchItemModel){
            val title: TextView = itemView.findViewById(R.id.tvTitle)
            title.text = homePatchItemModel.Name

            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItemModel)
            }
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = HomeRadioAdapter(
                homePatchItemModel,podcastTrackClick
            )
        }
        fun bind(homePatchItemModel: HomePatchItemModel?) {
            when (homePatchItemModel?.Design) {
                "search" -> bindSearch(homePatchItemModel)
                "Artist" -> bindArtist(homePatchItemModel, homeCallBack)
                "Playlist" -> bindPlaylist(homePatchItemModel)
                "Release" -> bindRelease(homePatchItemModel)
                "Track" -> bindRelease(homePatchItemModel)
                "Podcast" -> bindPopularPodcast(homePatchItemModel)
                "SmallVideo" -> bindTrendingMusic(homePatchItemModel)
//                "amarTune" -> bindPopularAmarTunes(homePatchItemModel)
                "download" -> bindDownload(homePatchItemModel)
                "PodcastLive" -> bindBhoot(homePatchItemModel)
                "Show"->bindPodcastShow(homePatchItemModel)
                "Discover"->bindBillboard(homePatchItemModel)
                "PDPS" -> bindPDPS(homePatchItemModel)
                "PodcastVideo"-> bindLargeVideos(homePatchItemModel)
                "NewReleaseAudio" ->bindNewReleaseAudio(homePatchItemModel)
                "LargeVideo" -> bindLargeVideos(homePatchItemModel)
                "Radio" -> bindRadio(homePatchItemModel)

            }


        }
    }



    public companion object {
        const val VIEW_SEARCH = 0
        const val VIEW_ARTIST = 1
        const val VIEW_RELEASE = 2
        const val VIEW_PLAYLIST = 3
        const val VIEW_AD = 4
        const val VIEW_DOWNLOAD = 5
        const val VIEW_POPULAR_AMAR_TUNES = 6
        const val VIEW_POPULAR_BANDS = 7
        const val VIEW_MADE_FOR_YOU = 8
        const val VIEW_LATEST_RELEASE = 9
        const val VIEW_POPULAR_PODCAST = 10
        const val VIEW_BL_MUSIC_OFFERS = 11
        const val VIEW_TRENDING_MUSIC_VIDEO = 12
        const val VIEW_PODCAST_LIVE = 13
        const val VIEW_SHOW= 14
        const val  VIEW_BANNER = 15
        const val VIEW_DISCOVER = 16
        const val VIEW_PDPS = 17
        const val VIEW_LARGE_VIDEO =18
        const val VIEW_PODCAST_VIDEO =19
        const val VIEW_NEW_RELEASE_AUDIO= 20
        const val VIEW_RADIO = 21
        const val VIEW_TYPE = 100
    }
}
internal class ParentAdapterDiffCallback: DiffUtil.ItemCallback<HomePatchItemModel>() {
    override fun areItemsTheSame(
        oldItem: HomePatchItemModel,
        newItem: HomePatchItemModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: HomePatchItemModel,
        newItem: HomePatchItemModel
    ): Boolean {
        return oldItem.Code == newItem.Code
    }

}