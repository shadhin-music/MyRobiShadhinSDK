package com.shadhinmusiclibrary.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.FeaturedPopularArtistAdapter
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.adapter.PopularArtistAdapter
import com.shadhinmusiclibrary.callBackService.PatchCallBack
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.LatestVideoModelDataModel
import com.shadhinmusiclibrary.data.model.PodcastDetailsModel
import com.shadhinmusiclibrary.fragments.artist.PopularArtistViewModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.DataContentType
import com.shadhinmusiclibrary.utils.Status
import com.shadhinmusiclibrary.utils.UtilHelper
import java.io.Serializable

internal class FeaturedPopularArtistFragment : BaseFragment(),
    PatchCallBack {

    private lateinit var navController: NavController
    private var homePatchitem: HomePatchItemModel? = null
    lateinit var viewModel: PopularArtistViewModel
    private lateinit var popularArtistAdapter: FeaturedPopularArtistAdapter
    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.popularArtistViewModelFactory
            )[PopularArtistViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container1: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef =
            inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container1, false)
        navController = findNavController()

        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvTitle: TextView = requireView().findViewById(R.id.tvTitle)
        //tvTitle.text =  homePatchitem?.Name
        setupViewModel()
        observeData()
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        kotlin.runCatching {
            val title = arguments?.getString(DataContentType.TITLE)
            view.findViewById<TextView>(R.id.tvTitle)?.text = title
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
    }
    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
       /* startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })*/
    }
    fun observeData() {
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        viewModel.fetchPouplarArtist()
        viewModel.popularArtistContent.observe(viewLifecycleOwner) { response ->
            if (response.status == Status.SUCCESS) {
                val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
               val footerAdapter = HomeFooterAdapter()
                response.data?.let {
                    it.data.let { it1 ->
                        popularArtistAdapter = FeaturedPopularArtistAdapter(it1, this)
                    }
                }
                val config = ConcatAdapter.Config.Builder()
                    .setIsolateViewTypes(false)
                    .build()
                val concatAdapter = ConcatAdapter(config, popularArtistAdapter, footerAdapter)
                val layoutManager = GridLayoutManager(context, 4)
                val onSpanSizeLookup: GridLayoutManager.SpanSizeLookup =
                    object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if (concatAdapter.getItemViewType(position) == HomeFooterAdapter.VIEW_TYPE) 4 else 1
                        }
                    }
                recyclerView.layoutManager = layoutManager
                layoutManager.spanSizeLookup = onSpanSizeLookup
                recyclerView.adapter = concatAdapter

              
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
//                Toast.makeText(requireContext(),"Error happened!", Toast.LENGTH_SHORT).show()
//                showDialog()
            }
        }
    }

    override fun onClickItemAndAllItem(itemPosition: Int, selectedData: List<PodcastDetailsModel>) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val sSelectedData = selectedData[itemPosition]
        navController.navigate(
            R.id.to_artist_details,
            Bundle().apply {
                putSerializable(
                    AppConstantUtils.PatchItem,
                    UtilHelper.getHomePatchItemToData(selectedData) as Serializable
                )
                putSerializable(
                    AppConstantUtils.PatchDetail,
                    UtilHelper.getHomePatchDetailToData(sSelectedData) as Serializable

                )
            })
//            AppConstantUtils.PatchDetail,
//            UtilHelper.getHomePatchDetailToData(selectedData) as Serializable
    }

//    override fun onClickSeeAll(selectedHomePatchItem: HomePatchItem) {
//
//    }

//    override fun onClickItemPodcastEpisode(itemPosition: Int, selectedEpisode: List<Episode>) {
//    }
}