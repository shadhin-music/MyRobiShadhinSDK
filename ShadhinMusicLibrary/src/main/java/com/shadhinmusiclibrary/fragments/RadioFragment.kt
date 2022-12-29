package com.shadhinmusiclibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.shadhinmusiclibrary.adapter.RadioTrackAdapter
import com.shadhinmusiclibrary.callBackService.RadioTrackCallBack
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.fragments.album.AlbumViewModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.library.player.utils.isPlaying
import com.shadhinmusiclibrary.utils.Status

internal class RadioFragment : BaseFragment(),
    RadioTrackCallBack {

    private lateinit var albumVM: AlbumViewModel
    private lateinit var radioTrackAdapter: RadioTrackAdapter
    private var globalRootContentId = ""
    private lateinit var parentRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioTrackAdapter = RadioTrackAdapter(this)
        initialize()

        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initialize() {
        setupViewModel()
        fetchOnlineData()
    }

    private fun setupViewModel() {
        albumVM = ViewModelProvider(this, injector.factoryAlbumVM)[AlbumViewModel::class.java]
    }

    private fun fetchOnlineData() {
        parentRecycler = requireView().findViewById(R.id.recyclerView)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val progressBar: ProgressBar = requireView().findViewById(R.id.progress_bar)
        albumVM.fetchGetAllRadio()
        albumVM.radiosContent.observe(viewLifecycleOwner) { res ->
            if (res.data?.data != null && res.status == Status.SUCCESS) {
                radioTrackAdapter.setRadioTrackData(
                    res.data.data,
                    "",
                    globalRootContentId
                )
                progressBar.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
            }
        }
        parentRecycler.layoutManager = layoutManager
        parentRecycler.adapter = radioTrackAdapter
    }

    override fun onClickItem(currentSong: IMusicModel) {
        globalRootContentId = currentSong.content_Id
        if (playerViewModel.currentMusic != null) {
            if ((globalRootContentId == playerViewModel.currentMusic?.rootId)) {
                playerViewModel.togglePlayPause()
            } else {
                ShadhinMusicSdkCore.openRadio(requireContext(), globalRootContentId)
            }
        } else {
            ShadhinMusicSdkCore.openRadio(requireContext(), globalRootContentId)
        }
    }

    override fun getCurrentVH(
        currentVH: RecyclerView.ViewHolder,
        songDetails: MutableList<IMusicModel>
    ) {
        val radioTrackVH = currentVH as RadioTrackAdapter.RadioTrackVH
        if (songDetails.size > 0 && isAdded) {
            //DO NOT USE requireActivity()
            playerViewModel.currentMusicLiveData.observe(viewLifecycleOwner) { itMusic ->
                if (itMusic != null) {
                    if (globalRootContentId.isEmpty()) {
                        globalRootContentId = itMusic.rootId ?: ""
                    }
                    if (radioTrackVH.rootId == globalRootContentId) {
                        playerViewModel.playbackStateLiveData.observe(viewLifecycleOwner) { itPla ->
                            if (itPla != null && radioTrackVH.rootId == globalRootContentId) {
                                radioTrackVH.ivRadioPlay?.let {
                                    playPauseStateBlack(
                                        itPla.isPlaying,
                                        it
                                    )
                                }
                            }
                        }
                    } else {
                        radioTrackVH.ivRadioPlay?.let {
                            playPauseStateBlack(
                                false,
                                it
                            )
                        }
                    }
                }
            }
        }
    }
}