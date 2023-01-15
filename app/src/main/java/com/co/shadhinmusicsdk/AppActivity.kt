package com.co.shadhinmusicsdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager



import com.co.shadhinmusicsdk.data.model.LoginData


import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.myrobi.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.myrobi.shadhinmusiclibrary.ShadhinSDKCallback
import com.myrobi.shadhinmusiclibrary.utils.share.ShareRC


class AppActivity : AppCompatActivity(), ShadhinSDKCallback {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_activity)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val data = intent.extras?.getBundle("loginData")
        if (data != null) {
            val mLoginData: LoginData = data.getSerializable("loginData") as LoginData
            Log.e("AA", "onCreate: " + mLoginData.accessToken)
            ShadhinMusicSdkCore.initializeSDK(
                applicationContext,
                mLoginData.accessToken.toString(),
                this
            )

        }


        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPagerAdapter(
            this, supportFragmentManager,
            tabLayout.tabCount
        )

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        viewPager.offscreenPageLimit = 2
        val selectedTabIndex = 0
        viewPager.setCurrentItem(selectedTabIndex, false)
    }

    override fun onDestroy() {
        ShadhinMusicSdkCore.destroySDK(applicationContext)
        super.onDestroy()
    }

    override fun tokenStatus(isTokenValid: Boolean, error: String) {
        Log.i("APPActivity", "isTokenValid: $isTokenValid $error ")
    }

    override fun onShare(rc: String) {
        Log.i("onShare", "onShare: ${ShareRC(rc)}")
    }

}