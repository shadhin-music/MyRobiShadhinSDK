package com.shadhinmusiclibrary.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shadhinmusiclibrary.fragments.AllDownloadDetailsFragment
import com.shadhinmusiclibrary.fragments.PodcastDownloadFragment
import com.shadhinmusiclibrary.fragments.SongsDownloadFragment
import com.shadhinmusiclibrary.fragments.VideosDownloadFragment

@Suppress("DEPRECATION")
internal class DownlodViewPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
//        return  SearchFragment()
        return when (position) {
            0 -> {
                AllDownloadDetailsFragment()
            }
            1 -> {
                SongsDownloadFragment()
            }
            2 -> {
                VideosDownloadFragment()
            }
            3 -> {
                PodcastDownloadFragment()

            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}