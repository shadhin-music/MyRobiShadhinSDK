package com.co.shadhinmusicsdk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.myrobi.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.myrobi.shadhinmusiclibrary.utils.share.ShareRC


internal class FeaturedHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_featured_home,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnrelease: Button = requireView().findViewById(R.id.btnLatestRelease)
        val btnPopularArtist: Button = requireView().findViewById(R.id.btnPopularArtists)
        val btnFeaturedPodcast: Button = requireView().findViewById(R.id.btnFeaturedPodcast)
        val btnMusicVideos: Button = requireView().findViewById(R.id.btnMusicVideos)
        val btnAmartunes: Button = requireView().findViewById(R.id.btnWebview)
        val btnAmartunesAll: Button = requireView().findViewById(R.id.btnWebview2)
        val btnMusic: Button = requireView().findViewById(R.id.btnMusic)
        val cvRadioButton: CardView = requireView().findViewById(R.id.include_radio_layout)
        val btnRadioSeeAll: TextView = requireView().findViewById(R.id.btn_radio_see_all)
        val btnShare: Button = requireView().findViewById(R.id.btnshare)
        val btnPatch: Button = requireView().findViewById(R.id.homePatch)
        val autoCompleteTextView: AutoCompleteTextView = requireView().findViewById(R.id.patch_code)

        btnPopularArtist.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "RC203")
        }
        btnrelease.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "RC201")
        }
        btnFeaturedPodcast.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "RC202")
        }

        btnAmartunes.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "BNMAIN01")
        }
        btnAmartunesAll.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "BNALL01") // for all
        }
        btnMusicVideos.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "RC204")
        }

        cvRadioButton.setOnClickListener {
            ShadhinMusicSdkCore.openRadio(requireContext(), "20148")
        }

        btnRadioSeeAll.setOnClickListener {
            ShadhinMusicSdkCore.openPatch(requireContext(), "RADIO")
        }

        btnMusic.setOnClickListener {
            ShadhinMusicSdkCore.openShadhin(requireContext(), "01855290762")
        }
        var count:Int = 0
        btnShare.setOnClickListener {

            val list = listOf(
                ShareRC.generate("20141","P"),
                ShareRC.generate("20139","P"),
                ShareRC.generate("70","A"),
                ShareRC.generate("71","A"),
                ShareRC.generate("72","A"),
                ShareRC.generate("73","A"),
                ShareRC.generate("16321","R"),
                ShareRC.generate("15787","R"),
                ShareRC.generate("22087","R"),
                ShareRC.generate("21902","R"),
               // ShareRC.generate("20605","R"),
                ShareRC.generate("98729","S"),
                ShareRC.generate("91119","S"),
                ShareRC.generate(null,"PDBC"),
                ShareRC.generate(null,"PDJC"),
                ShareRC.generate(null,"PDJG")
            )
            val share = list[count]
            ShadhinMusicSdkCore.openFromRC(requireContext(),share.code)
            if(count>=(list.size-1)){
                count = 0
            }
            count ++

        }

        val testArray = arrayOf("P030","P050","P081","P030","P073","P050")
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.select_dialog_item, testArray)
        autoCompleteTextView.setAdapter(adapter)

        btnPatch.setOnClickListener {
            val code = autoCompleteTextView.text.toString().uppercase()
            val share = ShareRC.generate(autoCompleteTextView.text.toString().uppercase())
            ShadhinMusicSdkCore.openFromRC(requireContext(),share.code)
            Log.i("routeHomePatch", "onViewCreated: $share")
            Toast.makeText(requireContext(),code.toString(),Toast.LENGTH_SHORT).show()
        }
//    private fun observeData() {
//        //  val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
//
//        viewModel!!.homeContent.observe(viewLifecycleOwner) { res ->
//            if (res.status == Status.SUCCESS) {
//                Log.d("TaG","Message: "+ res.data)
//                var homePatchItem = res.data?.data!![0]
//
//
//                // progressBar.visibility = View.GONE
//                //   viewDataInRecyclerView(res.data)
//            } else {
//                // progressBar.visibility = View.VISIBLE
//            }
//            //isLoading = false
//        }
//    }
//    override fun getViewModel(): Class<HomeViewModel> {
//        return HomeViewModel::class.java
//    }
//
//    override fun getViewModelFactory(): HomeViewModelFactory {
//        return injector.factoryHomeVM
//    }
//    }
    }
}