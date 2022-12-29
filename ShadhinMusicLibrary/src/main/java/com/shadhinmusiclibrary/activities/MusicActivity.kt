package com.shadhinmusiclibrary.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.utils.AppConstantUtils

internal class MusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_bl_sdk_activity_music)

        val imageBackBtn: AppCompatImageView = findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            onBackPressed()
        }
        val acivSearchBar: AppCompatImageView = findViewById(R.id.search_bar)
        acivSearchBar.setOnClickListener {
            openSearch()
        }
    }

    private fun openSearch() {
        startActivity(Intent(this, SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_Search
                )
            })
    }
}