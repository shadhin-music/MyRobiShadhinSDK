package com.myrobi.shadhinmusiclibrary.fragments.fav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.adapter.FavoriteViewPagerAdapter


internal class FavoriteFragment : Fragment() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.my_bl_sdk_fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tab)
        viewPager = view.findViewById(R.id.viewPager)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = "My Favourites"

        val searchBar: AppCompatImageView = requireView().findViewById(R.id.search_bar)
        searchBar.setOnClickListener {
            openSearch()
        }


        tabLayout.tabGravity = TabLayout.GRAVITY_START

        val adapter = FavoriteViewPagerAdapter(
            requireContext(), childFragmentManager,
            tabLayout.tabCount
        )

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.setCurrentItem(tab.getPosition())
                //Toast.makeText(context, "testing", Toast.LENGTH_SHORT).show();
//                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        viewPager.offscreenPageLimit = 2
        val selectedTabIndex = 0
        viewPager.setCurrentItem(selectedTabIndex, false)
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            /* if (ShadhinMusicSdkCore.pressCountDecrement() == 0) {
                 requireActivity().finish()
             } else {
                 navController.popBackStack()
             }*/
            requireActivity().onBackPressed()
        }
    }
    private fun openSearch() {
        findNavController().navigate(R.id.to_search)
       /* startActivity(Intent(requireContext(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })*/
    }
}