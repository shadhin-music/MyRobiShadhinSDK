package com.shadhinmusiclibrary.fragments.podcast

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.adapter.ParentAdapter
import com.shadhinmusiclibrary.adapter.PodcastSeeAllDetailsAdapter
import com.shadhinmusiclibrary.adapter.PodcastTNTypeAdapter
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.FeaturedPodcastDetailsModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.library.player.utils.isPlaying
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable


internal class PodcastSeeAllDetailsFragment : BaseFragment(),
    PodcastDetailsCallback {

    lateinit var viewModel: FeaturedPodcastViewModel
    private lateinit var navController: NavController
    private var dataAdapter: PodcastSeeAllDetailsAdapter? = null
    private lateinit var footerAdapter: HomeFooterAdapter
    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.featuredpodcastViewModelFactory
            )[FeaturedPodcastViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_common_rv_layout, container, false)
        navController = findNavController()
        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()

        viewModel.fetchPodcastSeeAll(false)
        observeData()
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
    }

    fun observeData() {
        val progress: ProgressBar = requireView().findViewById(R.id.progress_bar)
        val favViewModel =
            ViewModelProvider(
                this,
                injector.factoryFavContentVM
            )[FavViewModel::class.java]
        val cacheRepository = CacheRepository(requireContext())
        footerAdapter = HomeFooterAdapter()

        viewModel.podcastSeeAllContent.observe(viewLifecycleOwner) { res ->
            dataAdapter = PodcastSeeAllDetailsAdapter(this, cacheRepository, favViewModel, injector)
           // progress.visibility = GONE
            val recyclerView: RecyclerView? = view?.findViewById(R.id.recyclerView)
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView?.layoutManager = layoutManager
            val config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(false)
                .build()
            val concatAdapter = ConcatAdapter(config,dataAdapter, footerAdapter)
            val dataRange = res.data?.data?.indices
            if (dataRange != null) {
                for (item in dataRange) {
                    if (res.data.data.get(item).let { isValidDesign(it.PatchType) } == -1) {
                        res?.data.data.get(item).PatchType = ""
                    }
                    if (res?.data.data.get(item).PatchType.isNotEmpty() == true) {
                        res.data.data[item].let { it1 ->
                            dataAdapter?.setData(listOf(it1))
                            //dataAdapter?.notifyItemChanged(pageNum)
                            dataAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            recyclerView?.adapter = concatAdapter
        }
    }

    private fun isValidDesign(patchType: String): Int {
        return when (patchType) {
            "PP" -> PodcastSeeAllDetailsAdapter.VIEW_PP
            "TN" -> PodcastSeeAllDetailsAdapter.VIEW_TN
            "SS" -> PodcastSeeAllDetailsAdapter.VIEW_SS
            else -> {
                -1
            }
        }
    }

    override fun onPodcastLatestShowClick(
        patchItem: List<FeaturedPodcastDetailsModel>,
        position: Int
    ) {
        val mEpisode = UtilHelper.getHomePatchDetailToFeaturedPodcastDetails(patchItem[position])

        navController.navigate(
            R.id.to_podcast_details,
            Bundle().apply {

                putSerializable(
                    AppConstantUtils.PatchDetail,
                    mEpisode as Serializable
                )
            })
    }

    override fun onPodcastEpisodeClick(data: List<FeaturedPodcastDetailsModel>, position: Int) {
        val mEpisode = UtilHelper.getHomePatchDetailToFeaturedPodcastDetails(data[position])
        navController.navigate(
            R.id.to_podcast_details,
            Bundle().apply {

                putSerializable(
                    AppConstantUtils.PatchDetail,
                    mEpisode as Serializable
                )
            })
    }

    override fun onPodcastTrackClick(data: List<FeaturedPodcastDetailsModel>, position: Int) {
        setMusicPlayerInitData(data as MutableList<FeaturedPodcastDetailsModel>, position)
    }

    override fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        data: List<FeaturedPodcastDetailsModel>,
    ) {
        val trackViewHolder = currentVH as PodcastTNTypeAdapter.ViewHolder

        trackViewHolder.let {
            if ( isAdded) {
                playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMusic ->
                    if (itMusic != null) {
                        trackViewHolder.item.let {
                            if (it?.TracktId == itMusic.mediaId &&
                                it?.ContentType == itMusic.rootType){
                                playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
                                    playPauseStatePodcast(itPla.isPlaying, trackViewHolder.ivPlayBtn!!)
                                }
                            }else{
                                trackViewHolder.ivPlayBtn?.let { playPauseStatePodcast(false, it) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun playPauseStatePodcast(playing: Boolean, ivPlayPause: ImageView) {
        if (playing) {
            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_pd_play)
        } else {
            ivPlayPause.setImageResource(R.drawable.ic_pd_pause)
        }
    }

    fun setMusicPlayerInitData(
        mSongDetails: MutableList<FeaturedPodcastDetailsModel>,
        clickItemPosition: Int,
    ) {

        if (playerViewModel.currentMusic != null && (mSongDetails[clickItemPosition].TracktId == playerViewModel.currentMusic?.rootId)) {
            if ((mSongDetails[clickItemPosition].TracktId != playerViewModel.currentMusic?.mediaId)) {
                playerViewModel.skipToQueueItem(clickItemPosition)
            } else {
                playerViewModel.togglePlayPause()
            }
        } else {
            playerViewModel.unSubscribe()
            playerViewModel.subscribe(
                MusicPlayList(
                    UtilHelper.getMusicListToPodcastDetailsList(mSongDetails),
                    0
                ),
                false,
                clickItemPosition
            )
        }
    }
    private fun openSearch() {
        startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })
    }
}

interface PodcastDetailsCallback {
    fun onPodcastLatestShowClick(patchItem: List<FeaturedPodcastDetailsModel>, position: Int)
    fun onPodcastEpisodeClick(data: List<FeaturedPodcastDetailsModel>, position: Int)
    fun onPodcastTrackClick(data: List<FeaturedPodcastDetailsModel>, position: Int)
    fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        data: List<FeaturedPodcastDetailsModel>,
    )
}