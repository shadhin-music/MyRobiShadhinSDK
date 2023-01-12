package com.shadhinmusiclibrary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.MusicVideoAdapter
import com.shadhinmusiclibrary.fragments.artist.PopularArtistViewModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.DataContentType
import com.shadhinmusiclibrary.utils.Status

internal class MusicVideoFragment : BaseFragment() {
    lateinit var viewModel: PopularArtistViewModel

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.popularArtistViewModelFactory
            )[PopularArtistViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(
            R.layout.my_bl_sdk_fragment_featured_popular_artist,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        observeData()
        kotlin.runCatching {
            val title = arguments?.getString(DataContentType.TITLE)
            view.findViewById<TextView>(R.id.tvTitle)?.text = title
        }
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }
    }
    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
      /*  startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })*/
    }
    fun observeData() {
        viewModel.fetchLatestVideo()
        viewModel.latestVideoContent.observe(viewLifecycleOwner) { response ->
            if (response.status == Status.SUCCESS) {
                val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
                recyclerView.layoutManager =
                    GridLayoutManager(requireContext(), 2)

                response.data?.data?.let {
                    for (item in it) {
                        item.Artist = item.fav
                    }
                    recyclerView.adapter = MusicVideoAdapter(it)
                }
            } else {
            }
        }
    }
}


