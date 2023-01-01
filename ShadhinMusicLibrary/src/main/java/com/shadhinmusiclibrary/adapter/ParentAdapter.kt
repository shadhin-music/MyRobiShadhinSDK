package com.shadhinmusiclibrary.adapter



import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.shadhinmusiclibrary.autoimageslider.SliderView
import com.shadhinmusiclibrary.callBackService.DownloadClickCallBack
import com.shadhinmusiclibrary.callBackService.HomeCallBack
import com.shadhinmusiclibrary.callBackService.PodcastTrackCallback
import com.shadhinmusiclibrary.callBackService.SearchClickCallBack
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.RBTDATAModel


internal class ParentAdapter(
    var homeCallBack: HomeCallBack,
    val searchCb: SearchClickCallBack,
    val downloadClickCallBack: DownloadClickCallBack,
    val podcastTrackClick: PodcastTrackCallback,
    /*,val radioCallBack: RadioTrackCallBack*/
) : RecyclerView.Adapter<ParentAdapter.DataAdapterViewHolder>() {
    private var homeListData: MutableList<HomePatchItemModel> = mutableListOf()
    var search: HomePatchItemModel? = null
    var download: HomePatchItemModel? = null
    private var rbtData: MutableList<RBTDATAModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            VIEW_SEARCH -> R.layout.my_bl_sdk_item_search
            VIEW_ARTIST -> R.layout.my_bl_sdk_item_artist
            VIEW_PLAYLIST -> R.layout.my_bl_sdk_item_playlist
            VIEW_RELEASE -> R.layout.my_bl_sdk_item_release_patch
            VIEW_POPULAR_PODCAST -> R.layout.my_bl_sdk_item_release_patch
            VIEW_TRENDING_MUSIC_VIDEO -> R.layout.my_bl_sdk_item_trending_music_videos
            VIEW_PODCAST_LIVE -> R.layout.my_bl_sdk_item_bhoot_podcast
            VIEW_DOWNLOAD -> R.layout.my_bl_sdk_item_my_fav
            VIEW_POPULAR_AMAR_TUNES -> R.layout.my_bl_sdk_item_popular_amar_tunes
            VIEW_SHOW ->R.layout.my_bl_sdk_item_release_patch
            VIEW_BANNER ->R.layout.my_bl_sdk_banner
            VIEW_DISCOVER -> R.layout.billboard_layout
            VIEW_PDPS -> R.layout.my_bl_sdk_item_release_patch
            VIEW_LARGE_VIDEO -> R.layout.my_bl_sdk_item_release_patch
            VIEW_NEW_RELEASE_AUDIO->R.layout.billboard_layout
//            VIEW_POPULAR_PODCAST -> R.layout.item_top_trending
//            VIEW_BL_MUSIC_OFFERS -> R.layout.item_my_bl_offers
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return DataAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(homeListData.get(position))
    }

    override fun getItemCount(): Int = homeListData.size

    override fun getItemViewType(position: Int): Int {
        return when (homeListData[position].Design) {
            "search" -> VIEW_SEARCH
            "Artist" -> VIEW_ARTIST
            "Playlist" -> VIEW_PLAYLIST
            "Release" -> VIEW_RELEASE
            "Track" -> VIEW_RELEASE
            "Podcast" -> VIEW_POPULAR_PODCAST
            "SmallVideo" -> VIEW_TRENDING_MUSIC_VIDEO
            "amarTune" -> VIEW_POPULAR_AMAR_TUNES
            "download" -> VIEW_DOWNLOAD
            "PodcastLive" -> VIEW_PODCAST_LIVE
            "Show" -> VIEW_SHOW
            "Discover"-> VIEW_DISCOVER
            "PDPS"-> VIEW_PDPS
            "PodcastVideo"-> VIEW_LARGE_VIDEO
            "NewReleaseAudio" ->VIEW_NEW_RELEASE_AUDIO
            //adapterData[0].data[0].Design -> VIEW_ARTIST
            //           is DataModel.Artist -> VIEW_ARTIST
//            is DataModel.Search -> VIEW_SEARCH
//            is DataModel.Artist -> VIEW_ARTIST
//            is DataModel.TopTrending -> VIEW_TOP_TRENDING
//            is DataModel.BrowseAll -> VIEW_BROWSE_ALL
//            is DataModel.Ad -> VIEW_AD
//            is DataModel.Download -> VIEW_DOWNLOAD
//            is DataModel.PopularAmarTunes -> VIEW_POPULAR_AMAR_TUNES
//            is DataModel.PopularBands -> VIEW_POPULAR_BANDS
//            is DataModel.MadeForYou -> VIEW_MADE_FOR_YOU
//            is DataModel.LatestRelease -> VIEW_LATEST_RELEASE
//            is DataModel.PopularPodcast -> VIEW_POPULAR_PODCAST
//            is DataModel.BlOffers -> VIEW_BL_MUSIC_OFFERS
//            is DataModel.TrendingMusicVideo -> VIEW_TRENDING_MUSIC_VIDEO
            else -> {
                -1
            }
        }
    }

    var downloadNotAdded = true

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<HomePatchItemModel>) {
        val size = this.homeListData.size
        if (this.homeListData.isEmpty()) {
            for (item in data.indices) {
                search =
                    HomePatchItemModel(
                        "007",
                        "searchBar",
                        data[item].Data,
                        "search",
                        "search",
                        0,
                        0
                    )
                // download = HomePatchItem("002","download",data[item].Data,"download","download",0,0)
            }
            this.homeListData.add(search!!)
            //this.homeListData.add(download!!)
        }

        if (this.homeListData.size >= 3 && downloadNotAdded) {
            downloadNotAdded = false
            download = HomePatchItemModel("002", "download", listOf(), "download", "download", 0, 0)
            this.homeListData.add(download!!)
        }
        this.homeListData.addAll(data)
        val sizeNew = this.homeListData.size
        notifyItemRangeChanged(size, sizeNew)
//        var exists :Boolean = false
//        if (this.homeListData.isNotEmpty() && this.homeListData.size >= 3) {
//            for (item in data.indices) {
//                Log.e("TaG","Items: "+ data[item].ContentType)
//                download = HomePatchItem("002",
//                    "download",
//                    listOf(),
//                    "download",
//                    "download",
//                    0,
//                    0)
//                if (homeListData[item].ContentType==data[item].ContentType){
//                    exists = true
//                }
//            }
//
//            if(exists) {
//                Log.e("TaG","Items321: "+ exists)
//                this.homeListData.add(download!!)
//            }
//        }
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
            seeAll.setOnClickListener {
                //PodcastDetailsFragment
                homeCallBack.onClickSeeAll(homePatchItem)
            }
/*            itemView.setOnClickListener {
                Log.e("TAG", "URL1233444: " + homePatchItem.Data[absoluteAdapterPosition])
                // homeCallBack.onClickItemPodcastEpisode()
                podcastTrackClick.onClickItem(homePatchItem.Data,absoluteAdapterPosition)
            }*/
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
            }
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = LargeVideosAdapter(
                homePatchItem,
                homePatchDetail = homeListData[absoluteAdapterPosition].Data
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
                // homeCallBack.onClickItemAndAllItem(position,homePatchItemModel)
            }
            //  val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            //  Do your view assignment here from the data model
//            itemView.findViewById<ConstraintLayout>(R.id.clRoot)?.setBackgroundColor(item.bgColor)
//            itemView.findViewById<AppCompatTextView>(R.id.tvNameLabel)?.text = item.title
        }

//        private fun bundRadio(homePatchItemModel: HomePatchItemModel) {
//            val mSongDetMod = homePatchItemModel.Data[0]
//            val sivRadioIcon: ShapeableImageView = itemView.findViewById(R.id.siv_radio_icon)
//            val tvRadioSongName: TextView = itemView.findViewById(R.id.tv_radio_song_name)
////            val ivRadioFavorite: ImageView = itemView.findViewById(R.id.iv_radio_favorite)
//            var ivRadioPlay: ImageView = itemView.findViewById(R.id.iv_radio_play)
//
//            Glide.with(itemView.context)
//                .load(UtilHelper.getImageUrlSize300(mSongDetMod.image))
//                .into(sivRadioIcon)
//            tvRadioSongName.text = mSongDetMod.AlbumName
////            ivRadioPlay.setOnClickListener {
//////                radioCallBack.onClickOpenRadio(mSongDetMod.ContentID)
////            }
//        }

        private fun bindDownload(homePatchItemModel: HomePatchItemModel) {
            val download: LinearLayout = itemView.findViewById(R.id.Download)
            val watchlater: LinearLayout = itemView.findViewById(R.id.WatchLater)
            val playlist: LinearLayout = itemView.findViewById(R.id.Playlists)
            val favorite: LinearLayout = itemView.findViewById(R.id.Fav)
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
        }

        private fun bindPodcastShow(homePatchItemModel: HomePatchItemModel) {
            val seeAll: TextView = itemView.findViewById(R.id.tvSeeALL)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            tvTitle.text = homePatchItemModel.Name
            seeAll.setOnClickListener {
                homeCallBack.onClickSeeAll(homePatchItemModel)
                Log.e("TAG","PATCH: "+ homePatchItemModel)
            }

            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
             recyclerView.adapter = PodcastShowTypeAdapter(homePatchItemModel, homeCallBack)
        }

        private fun bindBanner(homePatchItemModel: HomePatchItemModel) {

            lateinit var sliderView: SliderView
            lateinit var sliderAdapter: SliderpagerAdapter
            Log.e("TAG","DATA: "+ homePatchItemModel.Data as MutableList)

            // on below line we are initializing our
            // slider adapter and adding our list to it.
            //sliderAdapter =SliderpagerAdapter(imageUrl)
            sliderView = itemView.findViewById(R.id.imageSlider)
            sliderAdapter = SliderpagerAdapter(homePatchItemModel.Data as MutableList, homeCallBack, homePatchItemModel)
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(sliderAdapter)
            sliderView.setIndicatorEnabled(true)
            sliderView.scrollTimeInSec = 2
            sliderView.isAutoCycle = true
            sliderView.startAutoCycle()
//
//
        }
        private fun bindBillboard(homePatchItemModel: HomePatchItemModel) {

            lateinit var sliderView: SliderView
            lateinit var sliderAdapter: SliderpagerAdapter
            Log.e("TAG","DATA: "+ homePatchItemModel.Data as MutableList)

            // on below line we are initializing our
            // slider adapter and adding our list to it.
            //sliderAdapter =SliderpagerAdapter(imageUrl)
            sliderView = itemView.findViewById(R.id.imageSlider)
            sliderAdapter = SliderpagerAdapter(homePatchItemModel.Data as MutableList, homeCallBack, homePatchItemModel)
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(sliderAdapter)
            sliderView.setIndicatorEnabled(true)
            sliderView.scrollTimeInSec = 2
            sliderView.isAutoCycle = true
            sliderView.startAutoCycle()
//
//
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
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
            recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                2,
                RecyclerView.HORIZONTAL,
                false
            )
            recyclerView.adapter = PDPSAdapter(homePatchItemModel, homeCallBack)

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
                homePatchDetail = homeListData[absoluteAdapterPosition].Data
            )
        }
        private fun bindNewReleaseAudio(homePatchItemModel: HomePatchItemModel){
            lateinit var sliderView: SliderView
            lateinit var sliderAdapter: NewReleaseSliderpagerAdapter
            Log.e("TAG","DATA: "+ homePatchItemModel.Data as MutableList)

            // on below line we are initializing our
            // slider adapter and adding our list to it.
            //sliderAdapter =SliderpagerAdapter(imageUrl)
            sliderView = itemView.findViewById(R.id.imageSlider)
            sliderAdapter = NewReleaseSliderpagerAdapter(homePatchItemModel.Data as MutableList, homeCallBack, homePatchItemModel)
            sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            sliderView.setSliderAdapter(sliderAdapter)
            sliderView.setIndicatorEnabled(true)
            sliderView.scrollTimeInSec = 2
            sliderView.isAutoCycle = true
            sliderView.startAutoCycle()
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
                "amarTune" -> bindPopularAmarTunes(homePatchItemModel)
                "download" -> bindDownload(homePatchItemModel)
                "PodcastLive" -> bindBhoot(homePatchItemModel)
                "Show"->bindPodcastShow(homePatchItemModel)
                "Discover"->bindBillboard(homePatchItemModel)
                "PDPS" -> bindPDPS(homePatchItemModel)
                "PodcastVideo"-> bindLargeVideos(homePatchItemModel)
                "NewReleaseAudio" ->bindNewReleaseAudio(homePatchItemModel)
//                "Playlist" -> bundRadio(homePatchItemModel)
                //"Artist"->bindPopularBands(homePatchItemModel)
//                "Artist" ->bindAd()
            }

            /*when (dataModel) {
                     dataModel-> bindArtist(dataModel!!)
 //                is DataModel.Search -> bindSearch()
 //                is DataModel.Artist -> bindArtist()
 //                is DataModel.TopTrending -> bindTopTrending()
 //                is DataModel.BrowseAll -> bindBrowseAll()
 //                is DataModel.Ad -> bindAd()
 //                is DataModel.Download -> bindDownload()
 //                is DataModel.PopularAmarTunes -> bindPopularAmarTunes()
 //                is DataModel.PopularBands -> bindPopularBands()
 //                is DataModel.MadeForYou -> bindMadeForYou()
 //                is DataModel.LatestRelease -> bindLatestRelease()
 //                is DataModel.PopularPodcast -> bindPopularPodcast()
 //                is DataModel.BlOffers -> bindBlOffers()
 //                is DataModel.TrendingMusicVideo -> bindTrendingMusic()
 ////                is DataModel.BlOffers -> bindBlOffers(dataModel)
             }*/
        }
    }



    public companion object {
        val VIEW_SEARCH = 0
        val VIEW_ARTIST = 1
        val VIEW_RELEASE = 2
        val VIEW_PLAYLIST = 3
        val VIEW_AD = 4
        val VIEW_DOWNLOAD = 5
        val VIEW_POPULAR_AMAR_TUNES = 6
        val VIEW_POPULAR_BANDS = 7
        val VIEW_MADE_FOR_YOU = 8
        val VIEW_LATEST_RELEASE = 9
        val VIEW_POPULAR_PODCAST = 10
        val VIEW_BL_MUSIC_OFFERS = 11
        val VIEW_TRENDING_MUSIC_VIDEO = 12
        val VIEW_PODCAST_LIVE = 13
        val VIEW_SHOW= 14
        val  VIEW_BANNER = 15
       val VIEW_DISCOVER = 16
       val VIEW_PDPS = 17
        val VIEW_LARGE_VIDEO =18
       val VIEW_NEW_RELEASE_AUDIO= 19
        const val VIEW_TYPE = 10
    }
}