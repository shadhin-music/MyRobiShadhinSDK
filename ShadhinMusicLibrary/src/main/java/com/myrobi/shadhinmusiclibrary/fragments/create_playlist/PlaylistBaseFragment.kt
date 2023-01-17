package com.myrobi.shadhinmusiclibrary.fragments.create_playlist

import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.activities.SDKMainActivity
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.library.player.ui.PlayerViewModel
import com.myrobi.shadhinmusiclibrary.utils.AppConstantUtils

internal open class PlaylistBaseFragment : BaseFragment() {
    var playlistId: String? = null
    var playlistName: String? = null
    var gradientDrawable: Int? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlistId = it.getSerializable(AppConstantUtils.PlaylistId) as String
            playlistName = it.getSerializable(AppConstantUtils.PlaylistName) as String
            argHomePatchDetail =
                it.getSerializable(AppConstantUtils.PatchDetail) as HomePatchDetailModel?
            argHomePatchItem = it.getSerializable(AppConstantUtils.PatchItem) as HomePatchItemModel
            gradientDrawable = it.getSerializable(AppConstantUtils.PlaylistGradientId) as Int
        }
//        createPlayerVM()
    }

//    private fun createPlayerVM() {
//        playerViewModel = ViewModelProvider(
//            requireActivity(),
//            injector.playerViewModelFactory
//        )[PlayerViewModel::class.java]
//    }

//    fun playItem(mSongDetails: MutableList<IMusicModel>, clickItemPosition: Int) {
//        (activity as? SDKMainActivity)?.setMusicPlayerInitData(mSongDetails, clickItemPosition)
//    }
//
//    fun playPauseState(playing: Boolean, ivPlayPause: ImageView) {
//        if (playing) {
//            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_pause_circle_filled)
//        } else {
//            ivPlayPause.setImageResource(R.drawable.my_bl_sdk_ic_play_linear)
//        }
//    }
}