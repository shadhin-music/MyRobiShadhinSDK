package com.shadhinmusiclibrary.fragments.search

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.shadhinmusiclibrary.activities.video.VideoActivity
import com.shadhinmusiclibrary.adapter.*
import com.shadhinmusiclibrary.callBackService.SearchItemCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.VideoModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.*

internal class SearchFragment : BaseFragment(), SearchItemCallBack {
    private lateinit var navController: NavController
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var progressBar: ProgressBar

    private var searchText: String = ""
    private var queryTextChangedJob: Job? = null

    private lateinit var svSearchInput: SearchView
    private lateinit var tvNoDataFound: TextView


    private lateinit var llTrendingSearchItem: LinearLayout
    private var tvTrendingSearchItem: TextView? = null
    private var cvTrendingSearchItem: CardView? = null

    private lateinit var llWeeklyTrending: LinearLayout
    private var tvWeeklyTrending: TextView? = null
    private var rvWeeklyTrending: RecyclerView? = null

    private lateinit var llTrendingVideo: LinearLayout
    private lateinit var tvTrendingVideo: TextView
    private lateinit var rvTrendingVideos: RecyclerView

    private lateinit var llArtist: LinearLayout
    private lateinit var tvArtist: TextView
    private lateinit var rvArtist: RecyclerView

    private lateinit var llAlbum: LinearLayout
    private var tvAlbums: TextView? = null
    private var rvAlbums: RecyclerView? = null

    private lateinit var llTracks: LinearLayout
    private lateinit var tvTracks: TextView
    private lateinit var rvTracks: RecyclerView

    private lateinit var llVideos: LinearLayout
    private var tvVideos: TextView? = null
    private var rvVideos: RecyclerView? = null

    private lateinit var llShows: LinearLayout
    private lateinit var tvShows: TextView
    private lateinit var rvShows: RecyclerView

    private lateinit var llEpisode: LinearLayout
    private lateinit var tvEpisodes: TextView
    private lateinit var rvEpisodes: RecyclerView

    private lateinit var llPodcastTracks: LinearLayout
    private lateinit var tvPodcastTracks: TextView
    private lateinit var rvPodcastTracks: RecyclerView

    var mSuggestionAdapter: SearchSuggestionAdapter? = null


    private lateinit var searchArtistAdapter: SearchArtistAdapter
    private lateinit var searchAlbumsAdapter: SearchAlbumsAdapter
    private lateinit var searchTracksAdapter: SearchTracksAdapter
    private lateinit var searchVideoAdapter: SearchVideoAdapter
    private lateinit var searchShowAdapter: SearchShowAdapter
    private lateinit var searchEpisodeAdapter: SearchEpisodeAdapter
    private lateinit var searchPodcastTracksAdapter: SearchPodcastTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_fragment_search, container, false)
        navController = findNavController()

        initUI(viewRef)
        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setupViewModel()
        searchArtistAdapter = SearchArtistAdapter(this)
        searchAlbumsAdapter = SearchAlbumsAdapter(this)
        searchTracksAdapter = SearchTracksAdapter(this)
        searchVideoAdapter = SearchVideoAdapter(this)
        searchShowAdapter = SearchShowAdapter(this)
        searchEpisodeAdapter = SearchEpisodeAdapter(this)
        searchPodcastTracksAdapter = SearchPodcastTracksAdapter(this)

        observeDataTrendingItems("S")

        svSearchInput = view.findViewById(R.id.sv_search_input)
        val chipArtist: Chip = requireView().findViewById(R.id.chip_1)
        val chipHabib: Chip = requireView().findViewById(R.id.chip_2)
        val chipVideo: Chip = requireView().findViewById(R.id.chip_3)
        val chipTahsan: Chip = requireView().findViewById(R.id.chip_4)
        val chipKona: Chip = requireView().findViewById(R.id.chip_5)

        chipArtist.setOnClickListener {
            routeDataPatch(DataContentType.CONTENT_TYPE_A_RC203)
        }
        setTextOnSearchBar(chipHabib)
        chipVideo.setOnClickListener {
            routeDataPatch(DataContentType.CONTENT_TYPE_V_RC204)
        }
        setTextOnSearchBar(chipTahsan)
        setTextOnSearchBar(chipKona)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
        svSearchInput.setSearchableInfo(searchManager?.getSearchableInfo(activity?.componentName))

        mSuggestionAdapter = SearchSuggestionAdapter(requireContext(), null, 0)
        svSearchInput.suggestionsAdapter = mSuggestionAdapter
        svSearchInput.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                val cursor: Cursor? = getRecentSuggestions(newText)
                if (newText.length > 1) {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(2000)
                        observeData(searchText)
                        mSuggestionAdapter?.swapCursor(cursor)
                        hideKeyboard(requireActivity())
                    }
                    observeDataTrendingItems("\"\"")
                    searchText = newText
                } else if (newText.isEmpty()) {
                    queryTextChangedJob?.cancel()
                    queryTextChangedJob = lifecycleScope.launch(Dispatchers.Main) {
                        delay(1000)
                        observeData(searchText)
                        observeData("\"\"")

                            mSuggestionAdapter?.swapCursor(cursor)
                        //}
//                        tvTrendingSearchItem.visibility = GONE
//                        cvTrendingSearchItem.visibility = GONE
//                        rvWeeklyTrending.visibility = GONE
//                        rvTrendingVideos.visibility = GONE
//                        tvWeeklyTrending.visibility = GONE
//                        tvTrendingVideo.visibility = GONE
                    }
                    observeDataTrendingItems("S")
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                 var query: String? = requireActivity()?.intent?.getStringExtra(SearchManager.QUERY)
                hideKeyboard(requireActivity())
                if (query != null) {
                    searchText = query
                }
                val suggestions = SearchRecentSuggestions(
                   requireContext(),
                    MySuggestionProvider.AUTHORITY,
                    MySuggestionProvider.MODE
                )
                suggestions.saveRecentQuery(query, null)
                svSearchInput.clearFocus()
                observeDataTrendingItems("\"\"")
                observeData(searchText)
//                tvTrendingSearchItem.visibility = GONE
//                rvWeeklyTrending.visibility = GONE
//                rvTrendingVideos.visibility = GONE
//                tvWeeklyTrending.visibility = GONE
//                tvTrendingVideo.visibility = GONE
                return true
            }
        })

        svSearchInput.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                svSearchInput.setQuery(mSuggestionAdapter!!.getSuggestionText(position), true)
                return true
            }
        })
        observe()
//        var url = ""
//        var GET_PODCAST_DETAILS = "\(BASE_URL)/Podcast/Podcast"
//        if (0 == 0) {
//            url = "V3?podType=\(podcastType)&contentTYpe=\(podcastShowCode.lowercased())"
//        } else {
//            url += "byepisodeIdV3?podType=\(podcastType)&episodeId=\(specificEpisodeID)&contentTYpe=\(podcastShowCode.lowercased())"
//        }
//        url = url + "&isPaid=\(ShadhinCore.instance.isUserPro)"
    }

    private fun initUI(viewRef: View) {
        progressBar = viewRef.findViewById(R.id.progress_bar)
        tvNoDataFound = viewRef.findViewById(R.id.tvNoDataFound)

        tvTrendingSearchItem = viewRef.findViewById(R.id.tvTrendingSearchItem)
        cvTrendingSearchItem = viewRef.findViewById(R.id.cvTrendingSearchItem)
        llTrendingSearchItem = viewRef.findViewById(R.id.llTrendingSearchItem)

        llWeeklyTrending = viewRef.findViewById(R.id.llWeeklyTrending)

        tvWeeklyTrending = viewRef.findViewById(R.id.tvWeeklyTrending)
        rvWeeklyTrending = viewRef.findViewById(R.id.rvWeeklyTrending)

        llTrendingVideo = viewRef.findViewById(R.id.llTrendingVideo)

        llArtist = viewRef.findViewById(R.id.llArtist)
        tvArtist = viewRef.findViewById(R.id.tvArtist)
        rvArtist = viewRef.findViewById(R.id.rvArtist)

        llAlbum = viewRef.findViewById(R.id.llAlbum)
        rvAlbums = viewRef.findViewById(R.id.rvAlbums)
        tvAlbums = viewRef.findViewById(R.id.tvAlbums)

        llTracks = viewRef.findViewById(R.id.llTracks)
        llVideos = viewRef.findViewById(R.id.llVideos)
        llShows = viewRef.findViewById(R.id.llShows)
        llEpisode = viewRef.findViewById(R.id.llEpisode)
        llPodcastTracks = viewRef.findViewById(R.id.llPodcastTracks)
    }

    private fun setTextOnSearchBar(chipCommon: Chip) {
        chipCommon.setOnClickListener {
            svSearchInput.setQuery(chipCommon.text, true)
        }
    }

    private fun setupViewModel() {
        searchViewModel =
            ViewModelProvider(this, injector.searchViewModelFactory)[SearchViewModel::class.java]
    }

    private fun observeDataTrendingItems(type: String) {
        searchViewModel.getTopTrendingItems(type)
        searchViewModel.topTrendingContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.isNotEmpty() == true) {
                    llTrendingSearchItem.visibility = VISIBLE
                    llWeeklyTrending.visibility = VISIBLE
                    tvNoDataFound.visibility = GONE

                    response.data.data.let {
                        rvWeeklyTrending?.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rvWeeklyTrending?.adapter = TopTenItemAdapter(this).apply {
                            setData(it.toMutableList())
                        }
                    }
                } else {
                    llTrendingSearchItem.visibility = GONE
                    llWeeklyTrending.visibility = GONE

                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
    }

    private fun observeData(searchText: String) {
//        llTrendingSearchItem.visibility = GONE
//        llWeeklyTrending.visibility = GONE
        searchViewModel.getSearchArtist(searchText)
        searchViewModel.getSearchAlbum(searchText)
        searchViewModel.getSearchTracks(searchText)
        searchViewModel.getSearchVideo(searchText)
        searchViewModel.getSearchPodcastEpisode(searchText)
        searchViewModel.getSearchPodcastShow(searchText)
        searchViewModel.getSearchPodcastTrack(searchText)


    }

    fun observe() {
        searchViewModel.searchArtistContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.Artist?.data?.isNotEmpty() == true) {
                    tvNoDataFound.visibility = GONE
                    llArtist.visibility = VISIBLE
                    rvArtist.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    searchArtistAdapter.setSearchArtist(response.data.data.Artist)
                    rvArtist.adapter = searchArtistAdapter
                } else {
                    llArtist.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
        searchViewModel.searchAlbumContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.Album?.data?.isNotEmpty() == true) {
                    tvNoDataFound.visibility = GONE
                    llAlbum.visibility = VISIBLE
                    rvAlbums?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    searchAlbumsAdapter.setSearchAlbums(response.data.data.Album)
                    rvAlbums?.adapter = searchAlbumsAdapter
                } else {
                    llAlbum.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
        searchViewModel.searchTracksContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.Track?.data?.isNotEmpty() == true) {
                    rvTracks = requireView().findViewById(R.id.rvTracks)
                    tvTracks = requireView().findViewById(R.id.tvTracks)
                    tvNoDataFound.visibility = GONE
                    llTracks.visibility = VISIBLE

                    rvTracks.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    searchTracksAdapter.setSearchTrackData(response.data.data.Track.data)
                    rvTracks.adapter = searchTracksAdapter
                } else {
                    llTracks.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
        searchViewModel.searchVideoContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.Video?.data?.isNotEmpty() == true) {
                    rvVideos = view?.findViewById(R.id.rvVideos)
                    tvVideos = view?.findViewById(R.id.tvVideos)
                    tvNoDataFound.visibility = GONE
                    llVideos.visibility = VISIBLE
                    rvVideos?.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    searchVideoAdapter.setSearchVideo(response.data.data.Video)
                    rvVideos?.adapter = searchVideoAdapter
                } else {
                    llVideos.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
        searchViewModel.searchPodcastShowContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.PodcastShow?.data?.isNotEmpty() == true) {
                    rvShows = requireView().findViewById(R.id.rvShows)
                    tvShows = requireView().findViewById(R.id.tvShows)

                    tvNoDataFound.visibility = GONE
                    llShows.visibility = VISIBLE

                    rvShows.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    searchShowAdapter.setSearchPodcastShow(
                        response.data.data.PodcastShow
                    )
                    rvShows.adapter = searchShowAdapter
                } else {
                    llShows.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
        searchViewModel.searchPodcastEpisodeContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.PodcastEpisode?.data?.isNotEmpty() == true) {
                    rvEpisodes = requireView().findViewById(R.id.rvEpisodes)
                    tvEpisodes = requireView().findViewById(R.id.tvEpisodes)
                    tvNoDataFound.visibility = GONE
                    llEpisode.visibility = VISIBLE

                    rvEpisodes.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    searchEpisodeAdapter.setSearchPodcastEpisode(
                        response.data.data.PodcastEpisode
                    )
                    rvEpisodes.adapter = searchEpisodeAdapter
                } else {
                    llEpisode.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
        searchViewModel.searchPodcastTrackContent.observe(viewLifecycleOwner) { response ->
            if (response != null && response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                if (response.data?.data?.PodcastTrack?.data?.isNotEmpty() == true) {
                    rvPodcastTracks = requireView().findViewById(R.id.rvPodcastTracks)
                    tvPodcastTracks = requireView().findViewById(R.id.tvPodcastTracks)

                    tvNoDataFound.visibility = GONE
                    llPodcastTracks.visibility = VISIBLE

                    rvPodcastTracks.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    searchPodcastTracksAdapter.setSearchPodcastTracks(
                        response.data.data.PodcastTrack
                    )
                    rvPodcastTracks.adapter = searchPodcastTracksAdapter
                } else {
                    llPodcastTracks.visibility = GONE
                    tvNoDataFound.visibility = VISIBLE
                }
            } else {
                progressBar.visibility = GONE
            }
        }
    }

    private fun routeDataPatch(contentType: String) {
        // hideKeyboard(requireActivity())
        when (contentType.toUpperCase(Locale.ENGLISH)) {
            DataContentType.CONTENT_TYPE_A_RC203 -> {
                setupNavGraphAndArg(R.id.featured_popular_artist_fragment,
                    Bundle().apply {
                        putString(DataContentType.TITLE, "Popular Artists")
                    })
            }
            DataContentType.CONTENT_TYPE_V_RC204 -> {
                setupNavGraphAndArg(R.id.music_video_fragment,
                    Bundle().apply {
                        putString(DataContentType.TITLE, "Music Video")
                    })
            }
        }
    }

    fun getRecentSuggestions(query: String): Cursor? {
        val uriBuilder: Uri.Builder = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(MySuggestionProvider.AUTHORITY)
        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY)
        val selection = "?"
        val selArgs = arrayOf(query)
        val uri: Uri = uriBuilder.build()
        return requireActivity().contentResolver.query(uri, null, selection, selArgs, null)
    }

    @SuppressLint("DefaultLocale")
    override fun onClickSearchItem(searchData: IMusicModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val patchItem = Bundle().apply {
            putSerializable(
                AppConstantUtils.PatchItem,
                HomePatchItemModel("", "A", listOf(), "", "", 0, 0)
            )
            putSerializable(
                AppConstantUtils.PatchDetail,
                UtilHelper.getHomePatchDetailToSearchDataModel(searchData) as Serializable
            )
        }
        val patchItemPodcastShow = Bundle().apply {
            putSerializable(
                AppConstantUtils.PatchItem,
                searchData.content_Type?.let { HomePatchItemModel("", it,
                    listOf(searchData) as List<HomePatchDetailModel>, "", "", 0, 0) }
            )
            putSerializable(
                AppConstantUtils.PatchDetail,
                UtilHelper.getHomePatchDetailToSearchPodcastShowDataModel(searchData) as Serializable
            )
        }
//     if(searchData.rootContentType?.contains("PD") == true){
//         setupNavGraphAndArg(
//                    R.id.to_podcast_details,
//                    patchItemPodcastShow
//                )
//     }
        when (searchData.rootContentType?.toUpperCase()) {

            DataContentType.CONTENT_TYPE_A -> {
                //open artist details
                setupNavGraphAndArg(
                    R.id.to_artist_details,
                    patchItem
                )
            }
            DataContentType.CONTENT_TYPE_R -> {
                //open album details
                setupNavGraphAndArg(
                    R.id.to_album_details,
                    patchItem
                )
            }
//            DataContentType.CONTENT_TYPE_PD_BC -> {
//                //open playlist
//                setupNavGraphAndArg(
//                    R.id.to_podcast_details,
//                    patchItem
//                )
//            }
            DataContentType.CONTENT_TYPE_PS -> {
                //open songs
                setupNavGraphAndArg(
                    R.id.to_podcast_details,
                     patchItemPodcastShow
                )
                Log.e("TAG","DATA: " + patchItemPodcastShow)
            }

            DataContentType.CONTENT_TYPE_PE -> {
                //open podcast
                setupNavGraphAndArg(
                    R.id.to_podcast_details,
                    patchItem
                )
            }
        }
    }

    //Top Tend play. whene fast search fragment came
    override fun onClickPlayItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int) {
        Log.e(
            "SF",
            "onClickPlayItem: "
                    + songItem[clickItemPosition].content_Type + " "
                    + songItem[clickItemPosition].rootContentType + " "
                    + songItem[clickItemPosition].content_Id + " "
                    + songItem[clickItemPosition].artistName
        )
        if (playerViewModel.currentMusic != null) {
            if ((songItem[clickItemPosition].rootContentType == playerViewModel.currentMusic?.rootType)) {
                if ((songItem[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                    playerViewModel.skipToQueueItem(clickItemPosition)
                } else {
                    playerViewModel.togglePlayPause()
                }
            } else {
                playItem(songItem, clickItemPosition)
            }
        } else {
            playItem(songItem, clickItemPosition)
        }
    }

    //after search play item
    override fun onClickPlaySearchItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int) {
        when (songItem[clickItemPosition].rootContentType?.toUpperCase()) {
            DataContentType.CONTENT_TYPE_V -> {
                //open video
                val intent = Intent(context, VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in songItem) {
                    //val video = Video()
                    //TODO need add this line of code
                    videoArray.add(UtilHelper.getVideoToIMusic(item))
                }
                val videos: ArrayList<VideoModel> = videoArray
                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, clickItemPosition)
                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videos)
                startActivity(intent)
            }
            DataContentType.CONTENT_TYPE_S -> {
                playerViewModel.unSubscribe()
                playerViewModel.subscribe(
                    MusicPlayList(
                        UtilHelper.getMusicListToSongDetailList(songItem),
                        0
                    ),
                    false,
                    clickItemPosition
                )
//                if (playerViewModel.currentMusic != null) {
//                    if ((songItem[clickItemPosition].rootContentType == playerViewModel.currentMusic?.rootType)) {
//                        if ((songItem[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
//                            playerViewModel.skipToQueueItem(clickItemPosition)
//                        } else {
//                            playerViewModel.togglePlayPause()
//                        }
//                    } else {
//                        playItem(
//                            songItem,
//                            clickItemPosition
//                        )
//                    }
//                } else {
                   // playItem(songItem, clickItemPosition)
               // }
            }
            DataContentType.CONTENT_TYPE_PT -> {
                //play Podcast Track
                if (playerViewModel.currentMusic != null) {
                    if ((songItem[clickItemPosition].rootContentType == playerViewModel.currentMusic?.rootType)) {
                        if ((songItem[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                            playerViewModel.skipToQueueItem(clickItemPosition)
                        } else {
                            playerViewModel.togglePlayPause()
                        }
                    } else {
                        playItem(
                            songItem,
                            clickItemPosition
                        )
                    }
                } else {
                    playItem(songItem, clickItemPosition)
                }
            }
        }
    }

    override fun onClickPlayVideoItem(songItem: MutableList<IMusicModel>, clickItemPosition: Int) {
        when (songItem[clickItemPosition].content_Type) {
            DataContentType.CONTENT_TYPE_V -> {
                val intent = Intent(requireContext(), VideoActivity::class.java)
                val videoArray = ArrayList<VideoModel>()
                for (item in songItem) {
//                    videoArray.add(UtilHelper.getVideoToSearchData(item))
                    videoArray.add(UtilHelper.getVideoToIMusic(item))
                }
                val videos: ArrayList<VideoModel> = videoArray
                intent.putExtra(VideoActivity.INTENT_KEY_POSITION, clickItemPosition)
                intent.putExtra(VideoActivity.INTENT_KEY_DATA_LIST, videos)
                startActivity(intent)
            }
        }
    }

    private fun setupNavGraphAndArg(graphResId: Int, bundleData: Bundle) {
        navController.navigate(graphResId, bundleData)
    }

    private fun hideKeyboard(mContext: Context) {
        val imm: InputMethodManager =
            activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity?.currentFocus
        if (view == null) {
            view = View(mContext)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
//    override fun onDestroyView() {
//        searchViewModel.removeObservers(parentFragment?.viewLifecycleOwner)
//        super.onDestroyView()
//
//    }

/*    override fun onClickAlbumItem(albumModelData: SearchAlbumdata) {
        ShadhinMusicSdkCore.pressCountIncrement()
        val data2 = Bundle()
        data2.putSerializable(
            AppConstantUtils.Album,
            albumModelData as Serializable
        )
//        navController.navigate(R.id.action_search_fragment_to_album_details_fragment,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItem("", "", listOf(), "", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    HomePatchDetail(
                        AlbumId = albumModelData.AlbumId,
                        ArtistId = albumModelData.ContentID,
                        ContentID = albumModelData.ContentID,
                        ContentType = albumModelData.ContentType,
                        PlayUrl = albumModelData.PlayUrl,
                        AlbumName = albumModelData.title,
                        AlbumImage = albumModelData.image,
                        fav = "",
                        Banner = "",
                        Duration = albumModelData.Duration,
                        TrackType = "",
                        image = albumModelData.image,
                        ArtistImage = "",
                        Artist = albumModelData.Artist,
                        CreateDate = "",
                        Follower = "",
                        imageWeb = "",
                        IsPaid = false,
                        NewBanner = "",
                        PlayCount = 0,
                        PlayListId = "",
                        PlayListImage = "",
                        PlayListName = "",
                        RootId = "",
                        RootType = "",
                        Seekable = false,
                        TeaserUrl = "",
                        title = albumModelData.title,
                        Type = ""

                    ) as Serializable
                )
            })
    }*/
}