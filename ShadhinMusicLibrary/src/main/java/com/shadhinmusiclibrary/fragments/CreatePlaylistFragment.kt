package com.shadhinmusiclibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.fragments.base.BaseFragment


internal class CreatePlaylistFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_fragment_create_playlist, container, false)
    }
}