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

//        val data = intent.extras?.getBundle("loginData")
//        if (data != null) {
//            val mLoginData: LoginData = data.getSerializable("loginData") as LoginData
//            Log.e("AA", "onCreate: " + mLoginData.accessToken)
//            ShadhinMusicSdkCore.initializeSDK(
//                applicationContext,
//                mLoginData.accessToken.toString(),
//                this
//            )
            //ShadhinMusicSdkCore.openShadhin(applicationContext, "01234556666666")
      //  }


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
  //  var USER_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkJlYXJlciJ9.eyJjbGllbnQiOiJNWUJMIiwiZnVsbE5hbWUiOiIiLCJtc2lzZG4iOiI4ODAxOTAwMDAwMDAwIiwiaW1hZ2VVUkwiOiIiLCJnZW5kZXIiOiIiLCJkZXZpY2VUb2tlbiI6IiIsIm5iZiI6MTY3Mzk0NDIyOCwiZXhwIjoxNjczOTkyODI0LCJpYXQiOjE2NzM5NDQyMjgsImlzcyI6IkJMTVVTSUMgIiwiYXVkIjoiU2hhZGhpbiAifQ.VvIHFyUcVSKBI0knxLEHQscMo9BHLbgICpRwecjuAy_C66ir_eBvFKzNumA_zYGnlBDmJbirRwfeIFHH5twG4g"
}