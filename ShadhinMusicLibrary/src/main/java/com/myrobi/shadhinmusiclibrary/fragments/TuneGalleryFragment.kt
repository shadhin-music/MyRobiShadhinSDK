package com.myrobi.shadhinmusiclibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myrobi.shadhinmusiclibrary.R

/**
 * Rezaul Khan
 * https://github.com/rezaulkhan111
 **/
internal class TuneGalleryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_fragment_tune_gallery, container, false)
    }
}