package com.myrobi.shadhinmusiclibrary.activities.subscription

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.myrobi.shadhinmusiclibrary.R

class SubscriptionWebViewActivity : AppCompatActivity() {
    private var webView:WebView?=null
    private var title:TextView?=null
    private var backButton:ImageView?=null
    private var progressBar:ProgressBar?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_webview)
        setupUi()
    }

    private fun setupUi() {
        title = findViewById(R.id.toolbar_title)
        backButton = findViewById(R.id.back_btn)
        webView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.progressBar)
    }
}