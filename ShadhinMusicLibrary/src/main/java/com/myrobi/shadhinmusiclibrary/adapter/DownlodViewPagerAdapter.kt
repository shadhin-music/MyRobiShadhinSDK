package com.myrobi.shadhinmusiclibrary.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.myrobi.shadhinmusiclibrary.fragments.AllDownloadDetailsFragment
import com.myrobi.shadhinmusiclibrary.fragments.PodcastDownloadFragment
import com.myrobi.shadhinmusiclibrary.fragments.SongsDownloadFragment
import com.myrobi.shadhinmusiclibrary.fragments.VideosDownloadFragment

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