package com.shadhinmusiclibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.fragments.base.BaseFragment


internal class PodcastVideoFragment : BaseFragment() {

    private lateinit var parentAdapter: ConcatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_fragment_podcast_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupAdapters()
    }

    private fun setupAdapters() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val config = ConcatAdapter.Config.Builder().apply { setIsolateViewTypes(false) }.build()

        val parentRecycler: RecyclerView = requireView().findViewById(R.id.recyclerView)

//        parentAdapter = ConcatAdapter(
//            config,
//            VideoPodcastHeaderAdapter(),
//            HeaderAdapter(),
//            PodcastMoreEpisodesAdapter(data),
//            PodcastCommentAdapter()
//        )
//        parentRecycler.setLayoutManager(layoutManager)
//        parentRecycler.setAdapter(parentAdapter)
    }
}