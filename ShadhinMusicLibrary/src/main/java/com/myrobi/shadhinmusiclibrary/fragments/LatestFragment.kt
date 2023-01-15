package com.myrobi.shadhinmusiclibrary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R

/**
 * Rezaul Khan
 * https://github.com/rezaulkhan111
 **/
internal class LatestFragment : Fragment() {
    private lateinit var rvLatestMusic: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRef = inflater.inflate(R.layout.my_bl_sdk_fragment_latest, container, false)
        rvLatestMusic = viewRef.findViewById(R.id.rv_latest_music)

        return viewRef;
    }
}