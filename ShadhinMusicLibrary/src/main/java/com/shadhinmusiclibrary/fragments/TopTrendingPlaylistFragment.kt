package com.shadhinmusiclibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment

internal class TopTrendingPlaylistFragment : BaseFragment() {
    private lateinit var navController: NavController
    var homePatchItem: HomePatchItemModel? = null
    var homePatchDetail: HomePatchDetailModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
        navController = findNavController()

        return viewRef
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            navController.popBackStack()
        }
    }
}