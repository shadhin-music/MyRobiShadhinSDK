package com.myrobi.shadhinmusiclibrary.bottom_sheet

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.adapter.MusicPlayAdapter
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.library.discretescrollview.DSVOrientation
import com.myrobi.shadhinmusiclibrary.library.discretescrollview.DiscreteScrollView

internal class MusicPlayBS : Fragment(),
    DiscreteScrollView.OnItemChangedListener<MusicPlayAdapter.MusicPlayVH>,
    DiscreteScrollView.ScrollStateChangeListener<MusicPlayAdapter.MusicPlayVH> {

    private lateinit var clMainMusicPlayerParent: ConstraintLayout
    private lateinit var acivMinimizePlayerBtn: AppCompatImageView
    private lateinit var tvTitle: TextView
    private lateinit var acivMenu: AppCompatImageView
    private lateinit var dsvCurrentPlaySongsThumb: DiscreteScrollView
    private lateinit var tvSongName: TextView
    private lateinit var tvSingerName: TextView
    private lateinit var ivFavoriteBtn: ImageView
    private lateinit var sbCurrentPlaySongStatus: SeekBar
    private lateinit var tvCurrentPlayDuration: TextView
    private lateinit var tvTotalPlayDuration: TextView
    private lateinit var ibtnShuffle: ImageButton
    private lateinit var ibtnSkipPrevious: ImageButton
    private lateinit var ibtnPlayPause: ImageButton
    private lateinit var ibtnSkipNext: ImageButton
    private lateinit var ibtnRepeatSong: ImageButton
    private lateinit var ibtnVolume: ImageButton
    private lateinit var ibtnLibraryAdd: ImageButton
    private lateinit var ibtnQueueMusic: ImageButton
    private lateinit var ibtnDownload: ImageButton

    private lateinit var listData: MutableList<HomePatchDetailModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.my_bl_sdk_main_music_player2, container, false)
        initUI(view)

        setMusicBannerAdapter()
        uiFunctionality()

        return view
    }

    private fun initUI(viewRef: View) {
        clMainMusicPlayerParent = viewRef.findViewById(R.id.cl_main_music_player)
        acivMinimizePlayerBtn = viewRef.findViewById(R.id.aciv_minimize_player_btn)
        tvTitle = viewRef.findViewById(R.id.tv_title)
        acivMenu = viewRef.findViewById(R.id.aciv_menu)
        dsvCurrentPlaySongsThumb = viewRef.findViewById(R.id.dsv_current_play_songs_thumb)
        tvSongName = viewRef.findViewById(R.id.tv_song_name)
        tvSingerName = viewRef.findViewById(R.id.tv_singer_name)
        ivFavoriteBtn = viewRef.findViewById(R.id.iv_favorite_btn)
        sbCurrentPlaySongStatus = viewRef.findViewById(R.id.sb_current_play_song_status)
        tvCurrentPlayDuration = viewRef.findViewById(R.id.tv_current_play_duration)
        tvTotalPlayDuration = viewRef.findViewById(R.id.tv_total_play_duration)
        ibtnShuffle = viewRef.findViewById(R.id.ibtn_shuffle)
        ibtnSkipPrevious = viewRef.findViewById(R.id.ibtn_skip_previous)
        ibtnPlayPause = viewRef.findViewById(R.id.ibtn_play_pause)
        ibtnSkipNext = viewRef.findViewById(R.id.ibtn_skip_next)
        ibtnRepeatSong = viewRef.findViewById(R.id.ibtn_repeat_song)
        ibtnVolume = viewRef.findViewById(R.id.ibtn_volume)
        ibtnLibraryAdd = viewRef.findViewById(R.id.ibtn_library_add)
        ibtnQueueMusic = viewRef.findViewById(R.id.ibtn_queue_music)
        ibtnDownload = viewRef.findViewById(R.id.ibtn_download)
    }

    private fun setMusicBannerAdapter() {
        val adapter = MusicPlayAdapter(requireContext())
        listData = mutableListOf()

        dsvCurrentPlaySongsThumb.adapter = adapter
        dsvCurrentPlaySongsThumb.setOrientation(DSVOrientation.HORIZONTAL)
        dsvCurrentPlaySongsThumb.setSlideOnFling(true)
        dsvCurrentPlaySongsThumb.setOffscreenItems(2)
        dsvCurrentPlaySongsThumb.setItemTransitionTimeMillis(150)
        dsvCurrentPlaySongsThumb.itemAnimator = null

        dsvCurrentPlaySongsThumb.addScrollStateChangeListener(this)
        dsvCurrentPlaySongsThumb.addOnItemChangedListener(this)
    }

    private fun uiFunctionality() {
        ibtnShuffle.setOnClickListener {
            setControlColor(ibtnShuffle)
//            R.color.vector_image_control.set
        }

        ibtnSkipPrevious.setOnClickListener {

        }

        ibtnPlayPause.setOnClickListener {
            setControlColor(ibtnPlayPause)
        }

        ibtnSkipNext.setOnClickListener { }

        ibtnRepeatSong.setOnClickListener {
            setControlColor(ibtnRepeatSong)
        }

        ibtnVolume.setOnClickListener {
            setControlColor(ibtnVolume)
        }

        ibtnLibraryAdd.setOnClickListener {
            setControlColor(ibtnLibraryAdd)
        }

        ibtnQueueMusic.setOnClickListener {
            setControlColor(ibtnQueueMusic)
        }

        ibtnDownload.setOnClickListener {
            setControlColor(ibtnDownload)
        }
    }

    private fun setControlColor(ibtnControl: ImageButton) {
        ibtnControl.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.my_sdk_color_accent
            ), PorterDuff.Mode.SRC_IN
        )
    }

    override fun onCurrentItemChanged(
        viewHolder: MusicPlayAdapter.MusicPlayVH?,
        adapterPosition: Int
    ) {
        if (viewHolder != null) {
//            viewHolder.sMusicData.Artist
//            tvSongName.text = viewHolder.sMusicData.AlbumName.toString()
//            tvSingerName.text = viewHolder.sMusicData.Artist
//            setPaletteGrdientColor(getBitmapFromVH(viewHolder))
        }
    }

    override fun onScrollStart(
        currentItemHolder: MusicPlayAdapter.MusicPlayVH,
        adapterPosition: Int
    ) {
    }

    override fun onScrollEnd(
        currentItemHolder: MusicPlayAdapter.MusicPlayVH,
        adapterPosition: Int
    ) {
    }

    override fun onScroll(
        scrollPosition: Float,
        currentPosition: Int,
        newPosition: Int,
        currentHolder: MusicPlayAdapter.MusicPlayVH?,
        newCurrent: MusicPlayAdapter.MusicPlayVH?
    ) {
        if (currentHolder != null) {
            setPaletteGrdientColor(getBitmapFromVH(currentHolder))
        }
    }

    private fun setPaletteGrdientColor(imBitmapData: Bitmap) {
        val palette: Palette = Palette.from(imBitmapData).generate()
        val vibrant: Swatch = palette.vibrantSwatch!!

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.my_sdk_shadin_required_color),
                vibrant.rgb
            )
        );
        gradientDrawable.cornerRadius = 0f;
//        clMainMusicPlayerParent.setBackgroundColor(vibrant.rgb)
        clMainMusicPlayerParent.background = gradientDrawable
    }

    private fun getBitmapFromVH(currentItemHolder: MusicPlayAdapter.MusicPlayVH): Bitmap {
        val imageV = currentItemHolder.ivCurrentPlayImage
        val traDaw = imageV.drawable
        return (traDaw.toBitmap())
    }
}