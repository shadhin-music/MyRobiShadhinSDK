package com.shadhinmusiclibrary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.GenrePlaylistAdapter
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.callBackService.CommonBottomCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.utils.AppConstantUtils

internal class STypeDetailsFragment : BaseFragment(),
    CommonBottomCallback {

    private lateinit var navController: NavController
    private lateinit var adapter: GenrePlaylistAdapter
    private lateinit var listSongDetail: MutableList<SongDetailModel>
    private lateinit var footerAdapter: HomeFooterAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_common_rv_layout, container, false)
        navController = findNavController()
        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        footerAdapter = HomeFooterAdapter()
        listSongDetail = mutableListOf()
        argHomePatchDetail?.apply {
            listSongDetail.add(
                SongDetailModel()
            )
        }
        val config = ConcatAdapter.Config.Builder()
            .setIsolateViewTypes(false)
            .build()
        adapter = GenrePlaylistAdapter(this)
        val concatAdapter = ConcatAdapter(config, adapter, footerAdapter)

        adapter.setRootData(argHomePatchDetail!!)
        //adapter.setData(listSongDetail)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = concatAdapter
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
        startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })
    }
    override fun onClickBottomItem(mSongDetails: IMusicModel) {
        (activity as? SDKMainActivity)?.showBottomSheetDialogForPlaylist(
            navController,
            context = requireContext(),
            mSongDetails,
            argHomePatchItem,
            argHomePatchDetail
        )
    }
}