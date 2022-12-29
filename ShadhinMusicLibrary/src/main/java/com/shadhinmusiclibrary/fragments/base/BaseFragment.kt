package com.shadhinmusiclibrary.fragments.base

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HistoryModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.di.FragmentEntryPoint
import com.shadhinmusiclibrary.fragments.history.ClientActivityViewModel
import com.shadhinmusiclibrary.library.player.ui.PlayerViewModel
import com.shadhinmusiclibrary.utils.AppConstantUtils
import kotlin.math.log

internal open class BaseFragment : Fragment(),
    FragmentEntryPoint {

    var argHomePatchItem: HomePatchItemModel? = null
    var argHomePatchDetail: HomePatchDetailModel? = null
    lateinit var playerViewModel: PlayerViewModel
    lateinit var clientActViewModel: ClientActivityViewModel

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argHomePatchItem = it.getSerializable(AppConstantUtils.PatchItem) as HomePatchItemModel?
            argHomePatchDetail = it.getSerializable(AppConstantUtils.PatchDetail) as HomePatchDetailModel?
        }
        createPlayerVM()
    }

    private fun createPlayerVM() {
        playerViewModel = ViewModelProvider(
            requireActivity(),
            injector.playerViewModelFactory
        )[PlayerViewModel::class.java]

        clientActViewModel =
            ViewModelProvider(
                this,
                injector.clientActivityVM
            )[ClientActivityViewModel::class.java]
    }

    fun playItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {

        (activity as? SDKMainActivity)?.setMusicPlayerInitData(mSongDetails, clickItemPosition)
    }

    fun playPauseStateRed(playing: Boolean, ivPlayPause: ImageView) {
        if (playing) {
            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_pause_circle_filled)
        } else {
            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_play_linear)
        }
    }

    fun playPauseStateBlack(playing: Boolean, ivPlayPause: ImageView) {
        if (playing) {
            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_baseline_pause_circle_filled_40)
        } else {
            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_baseline_play_circle_filled_40)
        }
    }

    fun patchMonitoring(argo: HomePatchItemModel) {
        clientActViewModel.fetchPatchClickHistory(HistoryModel().apply { patchCode = argo.Code })
    }
}