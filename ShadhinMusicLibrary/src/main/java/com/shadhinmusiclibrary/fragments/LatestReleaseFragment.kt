package com.shadhinmusiclibrary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.FeaturedLatestTracksAdapter
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.callBackService.LatestReleaseOnCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.fragments.artist.FeaturedTracklistViewModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.DataContentType.TITLE
import com.shadhinmusiclibrary.utils.Status

internal class LatestReleaseFragment : BaseFragment(),
    LatestReleaseOnCallBack {

    private lateinit var featuredTrackViewModel: FeaturedTracklistViewModel

    private lateinit var featuredLatestTracksAdapter: FeaturedLatestTracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        featuredLatestTracksAdapter = FeaturedLatestTracksAdapter(this)

        setupViewModel()
        setupUI()
        observeData()

        playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { music ->
            if (music?.mediaId != null) {
                featuredLatestTracksAdapter.setPlayingSong(music.mediaId!!)
            }
        }
    }

    private fun setupUI() {
        view?.findViewById<ImageView>(R.id.imageBack)?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        kotlin.runCatching {
            val title = arguments?.getString(TITLE)
            view?.findViewById<TextView>(R.id.tvTitle)?.text = title
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
    }
    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
        /*startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })*/
    }
    private fun setupViewModel() {
        featuredTrackViewModel =
            ViewModelProvider(
                this,
                injector.featuredtrackListViewModelFactory
            )[FeaturedTracklistViewModel::class.java]
    }

    fun observeData() {
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        featuredTrackViewModel.fetchFeaturedTrackList()
        featuredTrackViewModel.featuredTracklistContent.observe(viewLifecycleOwner) { response ->
            val footerAdapter = HomeFooterAdapter()
            if (response.status == Status.SUCCESS) {
                val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
                val config = ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build()
                val concatAdapter =
                    ConcatAdapter(config, featuredLatestTracksAdapter, footerAdapter)
                val layoutManager = GridLayoutManager(context, 3)
                val onSpanSizeLookup: GridLayoutManager.SpanSizeLookup =
                    object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (concatAdapter.getItemViewType(position) == HomeFooterAdapter.VIEW_TYPE) 3 else 1
                        }
                    }
                response?.data?.data?.let {
                    featuredLatestTracksAdapter.setData(
                        it,
                        playerViewModel.currentMusic?.mediaId
                    )
                }
                recyclerView.layoutManager = layoutManager
                layoutManager.setSpanSizeLookup(onSpanSizeLookup)
                recyclerView.adapter = concatAdapter
                concatAdapter.notifyDataSetChanged()
//                recyclerView.layoutManager =
//                   GridLayoutManager(requireContext(), 3)
//
//                recyclerView.adapter = featuredLatestTracksAdapter
//                    response?.data?.data?.let { FeaturedLatestTracksAdapter(it, this) }
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
//                Toast.makeText(requireContext(),"Error happened!", Toast.LENGTH_SHORT).show()
//                showDialog()
            }
        }
    }

    override fun onClickItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
        if (playerViewModel.currentMusic != null && (mSongDetails[clickItemPosition].rootContentId == playerViewModel.currentMusic?.rootId)
        ) {
            if ((mSongDetails[clickItemPosition].content_Id != playerViewModel.currentMusic?.mediaId)) {
                playerViewModel.skipToQueueItem(clickItemPosition)
            } else {
                playerViewModel.togglePlayPause()
            }
        } else {
            playItem(
                mSongDetails,
                clickItemPosition
            )
        }
    }
}