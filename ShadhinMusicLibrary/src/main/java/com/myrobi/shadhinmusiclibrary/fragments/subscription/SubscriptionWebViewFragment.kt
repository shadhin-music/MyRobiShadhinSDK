package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint

internal class SubscriptionWebViewFragment:Fragment(),FragmentEntryPoint {

    private var webView: WebView?=null
    private var titleTextview: TextView?=null
    private var backButton: ImageView?=null
    private var progressBar: ProgressBar?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subscription_webview_fragment,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        readData()
    }

    private fun readData() {
        val title =arguments?.getString(TITLE_ARGS)
        val url = arguments?.getString(URL_ARGS)
        titleTextview?.text = title?:""
        backButton?.setOnClickListener {
            requireActivity().onBackPressed()
        }
        Log.i(TAG, "readData: ${url}")
        url?.let { openWebView(it) }

    }

    private fun setupUi(view: View) {
        titleTextview = view.findViewById(R.id.toolbar_title)
        backButton = view.findViewById(R.id.back_btn)
        webView = view.findViewById(R.id.webview)
        progressBar = view.findViewById(R.id.progressBar)
    }
    private fun openWebView(url: String) {

        //  mWebview.settings.javaScriptEnabled =true
        webView?.getSettings()?.setLoadsImagesAutomatically(true)
        webView?.getSettings()?.setJavaScriptEnabled(true)
        webView?.getSettings()?.setAllowContentAccess(true)

        webView?.getSettings()?.setUseWideViewPort(true)
        webView?.getSettings()?.setLoadWithOverviewMode(true)
        webView?.getSettings()?.setDomStorageEnabled(true)
        webView?.clearView()
        webView?.setHorizontalScrollBarEnabled(false)
        // mWebview.getSettings().setAppCacheEnabled(true)
       webView?.getSettings()?.setDatabaseEnabled(true)
       webView?.setVerticalScrollBarEnabled(false)
       webView?.getSettings()?.setBuiltInZoomControls(true)
       webView?.getSettings()?.setDisplayZoomControls(false)
       webView?.getSettings()?.setAllowFileAccess(true)
       webView?.getSettings()?.setPluginState(WebSettings.PluginState.OFF)
       webView?.setScrollbarFadingEnabled(false)
       webView?.getSettings()?.setCacheMode(WebSettings.LOAD_NO_CACHE)
       webView?.getSettings()?.setDefaultZoom(WebSettings.ZoomDensity.FAR)
       webView?.setWebViewClient(WebViewClient())
       webView?.getSettings()?.setRenderPriority(WebSettings.RenderPriority.HIGH)
       webView?.setInitialScale(1)
       webView?.loadUrl(url)
       webView?.setWebViewClient(MyWebViewClient())

    }
    inner class MyWebViewClient() :WebViewClient() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {

            if (request.url.toString().contains("http://micropay.techapi24.com/api/")) {
                requireActivity().onBackPressed()
                return true
            }
            if (request.url.toString().contains("funbox.com")) {

                requireActivity().onBackPressed()
                return true
            }
            if (request.url.toString()
                    .contains("https://micropay.techapi24.com/api/bkpayredirect")
            ) {
                val status = request.url.getQueryParameter("status")
                requireActivity().onBackPressed()
                return true
            }
            if (request.url.toString().startsWith("https://shadhinmusic.com")) {
                requireActivity().onBackPressed()
            }
            view.loadUrl(request.url.toString())
            return false
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.e("WEBURL", "shouldOverrideUrlLoading$url")
            view.loadUrl(url)
            return false
        }


        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            progressBar?.visibility = View.GONE
            if (url.startsWith("http://shadhin.co/?statusCode=200&referenceCode=")) {
                requireActivity().onBackPressed()
            }
            if (url.startsWith("https://apis.techapi24.com/dpdpapi/SubsRequestSDHN.aspx?serviceid=")) {
                requireActivity().onBackPressed()
            }
            if (url.startsWith("https://apis.techapi24.comdpdpapi/UnSubs.aspx?serviceid")) {
                requireActivity().onBackPressed()
            }
            if (url.startsWith("http://shadhin.co/dpdpapi/vMsisdnSDHN.aspx?serviceid=")) {
                requireActivity().onBackPressed()
            }
        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        webView = null
        titleTextview   = null
        backButton  = null
        progressBar = null
        super.onDestroyView()
    }
    companion object{
        const val TITLE_ARGS:String = "title_args"
        const val URL_ARGS:String = "url_args"
    }

}