package com.shadhinmusiclibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.adapter.BrowseAllPlaylistAdapter
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment


internal class AllGenresDetailsFragment : BaseFragment() {
    var homePatchItem: HomePatchItemModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_item_all_genres, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            homePatchItem = it.getSerializable("data") as HomePatchItemModel?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = homePatchItem?.let { BrowseAllPlaylistAdapter(it) }

//        val dataAdapter = BrowseAllPlaylistAdapter()
//        //dataAdapter.setData()
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        recyclerView.adapter = dataAdapter

        val title: TextView = view.findViewById(R.id.tvTitle)
        title.setText(homePatchItem!!.Name)
        val button: AppCompatImageView = view.findViewById(R.id.imageBack)
        button.setOnClickListener {

        }
    }
}