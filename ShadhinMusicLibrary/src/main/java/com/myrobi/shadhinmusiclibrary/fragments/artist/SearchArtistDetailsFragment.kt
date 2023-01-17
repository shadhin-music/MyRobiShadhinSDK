package com.myrobi.shadhinmusiclibrary.fragments.artist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.adapter.*
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.myrobi.shadhinmusiclibrary.callBackService.HomeCallBack
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.ArtistContentModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.myrobi.shadhinmusiclibrary.data.model.search.SearchArtistDataModel
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.library.player.utils.isPlaying
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable


internal class SearchArtistDetailsFragment : BaseFragment(),
    HomeCallBack,
    CommonPlayControlCallback,
    CommonBottomCallback {
    private lateinit var navController: NavController
    var artistContent: ArtistContentModel? = null
    private lateinit var viewModel: ArtistViewModel
    private lateinit var viewModelArtistBanner: ArtistBannerViewModel
    private lateinit var viewModelArtistSong: ArtistContentViewModel
    private lateinit var viewModelArtistAlbum: ArtistAlbumsViewModel
    private lateinit var parentAdapter: ConcatAdapter
    private lateinit var footerAdapter: HomeFooterAdapter
    private lateinit var artistHeaderAdapter: SearchArtistHeaderAdapter
    private lateinit var artistsYouMightLikeAdapter: ArtistsYouMightLikeAdapter
    private lateinit var searchartistTrackAdapter: SearchArtistTrackAdapter
    private lateinit var artistAlbumsAdapter: ArtistAlbumsAdapter
    private lateinit var parentRecycler: RecyclerView
    private lateinit var searchArtistdata: SearchArtistDataModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
        return viewRef
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argHomePatchItem = it.getSerializable(AppConstantUtils.PatchItem) as HomePatchItemModel?
            searchArtistdata = ((it.getSerializable("searchArtistdata") as SearchArtistDataModel?)!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()

        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun initialize() {
        setupAdapters()
        setupViewModel()
        observeData()
    }

    private fun setupAdapters() {
        parentRecycler = requireView().findViewById(R.id.recyclerView)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder().apply { setIsolateViewTypes(false) }.build()
        artistHeaderAdapter = SearchArtistHeaderAdapter(searchArtistdata, this)
        searchartistTrackAdapter = SearchArtistTrackAdapter(this, this)
        artistAlbumsAdapter = ArtistAlbumsAdapter(argHomePatchItem, this)
        artistsYouMightLikeAdapter =
            ArtistsYouMightLikeAdapter(argHomePatchItem, this, argHomePatchDetail?.artist_Id)
        footerAdapter = HomeFooterAdapter()
        parentAdapter = ConcatAdapter(
            config,
            artistHeaderAdapter,
            searchartistTrackAdapter,
            artistAlbumsAdapter,
            artistsYouMightLikeAdapter,
            footerAdapter
        )
        parentAdapter.notifyDataSetChanged()
        parentRecycler.setLayoutManager(layoutManager)
        parentRecycler.setAdapter(parentAdapter)
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this, injector.factoryArtistVM)[ArtistViewModel::class.java]
        viewModelArtistBanner = ViewModelProvider(
            this,
            injector.factoryArtistBannerVM
        )[ArtistBannerViewModel::class.java]
        viewModelArtistSong = ViewModelProvider(
            this,
            injector.factoryArtistSongVM
        )[ArtistContentViewModel::class.java]
        viewModelArtistAlbum = ViewModelProvider(
            this,
            injector.artistAlbumViewModelFactory
        )[ArtistAlbumsViewModel::class.java]
    }

    private fun observeData() {
        searchArtistdata.let {
            viewModel.fetchArtistBioData(it.Artist)
            Log.e("TAG", "DATA: " + it.Artist)
        }
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        viewModel.artistBioContent.observe(viewLifecycleOwner) { response ->

            if (response.status == Status.SUCCESS) {
                artistHeaderAdapter.artistBio(response.data)
                Log.e("TAG", "DATA321: " + response.data?.artist)
//                Log.e("TAG","DATA: "+ response.message)
                progressBar.visibility = GONE
            } else {
                progressBar.visibility = GONE
            }
        }
        searchArtistdata.let {
            viewModelArtistBanner.fetchArtistBannerData(it.ContentID)
        }

        viewModelArtistBanner.artistBannerContent.observe(viewLifecycleOwner) { response ->
            if (response.status == Status.SUCCESS) {
                progressBar.visibility = GONE
                artistHeaderAdapter.artistBanner(response.data)
            } else {
                progressBar.visibility = VISIBLE
            }
        }

        searchArtistdata.let {
            viewModelArtistSong.fetchArtistSongData(it.ContentID)
            viewModelArtistSong.artistSongContent.observe(viewLifecycleOwner) { res ->
                if (res.status == Status.SUCCESS) {
                    searchartistTrackAdapter.artistContent(res.data)
                } else {
                    showDialog()
                }
            }
        }
        searchArtistdata.let {
            viewModelArtistAlbum.fetchArtistAlbum("r", it.ContentID)
            viewModelArtistAlbum.artistAlbumContent.observe(viewLifecycleOwner) { res ->

                if (res.status == Status.SUCCESS) {
                    artistAlbumsAdapter.setData(res.data)
                } else {
                    showDialog()
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext()) //set icon
            .setIcon(android.R.drawable.ic_dialog_alert) //set title
            .setTitle("An error occurred") //set message
            .setMessage("Go back to previous page") //set positive button
            .setPositiveButton(
                "Okay"
            ) { _, _ ->
                requireActivity().finish()
            }

            .show()
    }

    override fun onClickItemAndAllItem(
        itemPosition: Int,
        selectedHomePatchItem: HomePatchItemModel
    ) {
        Log.e("TAG", "DATA ARtist: " + selectedHomePatchItem)
        //  setAdapter(patch)
//        argHomePatchDetail = selectedHomePatchItem.Data[itemPosition]
//        artistHeaderAdapter.setData(argHomePatchDetail!!)
//        observeData()
//        artistsYouMightLikeAdapter.artistIDToSkip = argHomePatchDetail!!.ArtistId
//        parentAdapter.notifyDataSetChanged()
//        parentRecycler.scrollToPosition(0)
    }

    fun loadNewArtist(patchDetails: HomePatchDetailModel) {
        Log.e("Check", "loadNewArtist")
    }

    override fun onArtistAlbumClick(
        itemPosition: Int,
        artistAlbumModelData: List<ArtistAlbumModelData>,
    ) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val mArtAlbumMod = artistAlbumModelData[itemPosition]
        navController.navigate(R.id.to_album_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    HomePatchItemModel("", "", listOf(), "", "", 0, 0)
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    UtilHelper.getHomePatchDetailToAlbumModel(mArtAlbumMod) as Serializable
                )
            })
    }

    override fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<EpisodeModel>) {
    }


    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItemModel) {
    }

    override fun onRootClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        val lSongDetails = searchartistTrackAdapter.artistSongList
        if (lSongDetails.size > clickItemPosition) {
            if (playerViewModel.currentMusic != null) {
                if (lSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId) {
                    playerViewModel.togglePlayPause()
                }
            } else {
                playItem(
                    lSongDetails,
                    clickItemPosition
                )
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        if (playerViewModel.currentMusic != null) {
            if ((mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)) {
                if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                    playerViewModel.skipToQueueItem(clickItemPosition)
                    playerViewModel.play()
                } else {
                    playerViewModel.togglePlayPause()
                }
            }
        } else {
            playItem(
                mSongDetails,
                clickItemPosition
            )
        }
    }

    override fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        songDetails: MutableList<IMusicModel>
    ) {
        val mSongDet = searchartistTrackAdapter.artistSongList
        val albumVH = currentVH as SearchArtistHeaderAdapter.ArtistHeaderVH
        if (mSongDet.size > 0 && isAdded) {
            //DO NOT USE requireActivity()
            playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMusic ->
                if (itMusic != null) {
                    if ((mSongDet.indexOfFirst {
                            it.rootContentType == itMusic.rootType &&
                                    it.content_Id == itMusic.mediaId
                        } != -1)
                    ) {
                        playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
                            if (itPla != null)
                                albumVH.ivPlayBtn?.let { playPauseStateRed(itPla.isPlaying, it) }
                        }
                    }
                }
            }
        }
    }

    override fun onClickBottomItem(mSongDetails: IMusicModel) {
    }
}