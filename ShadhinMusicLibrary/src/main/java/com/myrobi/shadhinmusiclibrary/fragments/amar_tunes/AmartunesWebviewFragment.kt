package com.myrobi.shadhinmusiclibrary.fragments.amar_tunes

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.RBTDATAModel
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.library.player.data.model.Music.Companion.CONTENT_TYPE
import com.myrobi.shadhinmusiclibrary.utils.DataContentType.AMR_TUNE
import com.myrobi.shadhinmusiclibrary.utils.DataContentType.AMR_TUNE_ALL
import com.myrobi.shadhinmusiclibrary.utils.Status


internal class AmartunesWebviewFragment : BaseFragment() {
    var data: RBTDATAModel? = null
    private lateinit var viewModelAmaraTunes: AmarTunesViewModel
    private var contentType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getSerializable("data") as RBTDATAModel?
            contentType = it.getString(CONTENT_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_fragment_amar_tunes_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAmaraTunes = ViewModelProvider(
            this,
            injector.factoryAmarTuneVM
        )[AmarTunesViewModel::class.java]
        viewModelAmaraTunes.fetchRBTURL()

        observeData()
    }

    private fun observeData() {

        viewModelAmaraTunes.urlContent.observe(viewLifecycleOwner) { res ->
            if (res.status == Status.SUCCESS) {

                val redirectUrl: String = res?.data?.data?.redirectUrl.toString()

                val url = when (contentType) {
                    AMR_TUNE_ALL -> res?.data?.data?.pwaUrl
                    AMR_TUNE -> res?.data?.data?.pwatopchartURL
                    else -> null
                }
                if (url != null) {
                    openWebView(url, redirectUrl)
                } else {
                    Toast.makeText(requireActivity(), "URL NULL", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (res.errorCode == 401) {
                    AlertDialog.Builder(requireActivity())
                        .setTitle("Error")
                        .setMessage("Unauthorized")
                        .setPositiveButton("Close", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                requireActivity().onBackPressed()
                            }
                        })
                        .show()
                    // Toast.makeText(requireActivity(), "Unauthorized", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "Something is wrong", Toast.LENGTH_SHORT)
                        .show();
                }

            }
        }
    }

    private fun openWebView(url: String, redirectUrl: String) {
        val mWebview: WebView = requireView().findViewById(R.id.webview)
        //  mWebview.settings.javaScriptEnabled =true
        mWebview.getSettings().setLoadsImagesAutomatically(true)
        mWebview.getSettings().setJavaScriptEnabled(true)
        mWebview.getSettings().setAllowContentAccess(true)

//        mWebview.loadUrl("http://amartune.banglalink.net/pwa/home")

        mWebview.getSettings().setUseWideViewPort(true)
        mWebview.getSettings().setLoadWithOverviewMode(true)
        mWebview.getSettings().setDomStorageEnabled(true)
        mWebview.clearView()
        mWebview.setHorizontalScrollBarEnabled(false)
       // mWebview.getSettings().setAppCacheEnabled(true)
        mWebview.getSettings().setDatabaseEnabled(true)
        mWebview.setVerticalScrollBarEnabled(false)
        mWebview.getSettings().setBuiltInZoomControls(true)
        mWebview.getSettings().setDisplayZoomControls(false)
        mWebview.getSettings().setAllowFileAccess(true)
        mWebview.getSettings().setPluginState(WebSettings.PluginState.OFF)
        mWebview.setScrollbarFadingEnabled(false)
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
        mWebview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR)
        mWebview.setWebViewClient(WebViewClient())
        mWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        mWebview.setInitialScale(1)
        mWebview.loadUrl(url)
        //Log.e("TAG", "URL: " + pwatopchartURL)
        mWebview.setWebViewClient(MyWebViewClient(redirectUrl))
    }

    inner class MyWebViewClient(val redirectUrl: String) : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val mWebview: WebView? = view?.findViewById(R.id.webview)
            //  view.loadUrl(url)

            if (url.equals(redirectUrl)) {
                /*  // Toast.makeText(mWebview?.context,"CLOSE",Toast.LENGTH_SHORT).show()
                      (view.context as AppCompatActivity).supportFragmentManager.popBackStack()*/

                requireActivity().onBackPressed()
            }
            // view.canGoBack()
            return true
        }

//        override fun onPageFinished(view: WebView?, url: String?) {
//            val mWebview:WebView ?= view?.findViewById(R.id.webview)
//            if(url.equals("http://amartune.banglalink.net/pwa/home")){
//                mWebview?.goBack()
////                getActivity().getFragmentManager().beginTransaction().remove(con).commit();
////                val manager: FragmentManager =
////                    (con as AppCompatActivity).supportFragmentManager
////                manager?.popBackStack("Fragment", 0)
//              // view?.destroy()
//                Log.e("TAG","URL123: "+ url)
//            }
        // Log.e("TAG","URL43: "+ url)
//            view?.loadUrl(url.equals("https://api.shadhinmusic.com/api/v5/mybl/amartunecb").toString())
//            view?.canGoBack()
        //   }
    }

    override fun onResume() {
        super.onResume()
        kotlin.runCatching { (activity as AppCompatActivity?)?.supportActionBar?.hide() }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }
}