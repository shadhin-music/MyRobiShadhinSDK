package com.shadhinmusiclibrary.activities.video

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadhinmusiclibrary.data.model.VideoModel
import java.util.ArrayList

internal open class VideoViewModel : ViewModel() {

    private val _progressbarVisibility: MutableLiveData<Int> = MutableLiveData<Int>(View.GONE)
    val progressbarVisibility: LiveData<Int> = _progressbarVisibility

    private val _isListLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val isListLiveData: LiveData<Boolean> = _isListLiveData

    private val _videoListLiveData: MutableLiveData<List<VideoModel>> = MutableLiveData()
    public val videoListLiveData: LiveData<List<VideoModel>> = _videoListLiveData

    private val _currentVideo: MutableLiveData<VideoModel> = MutableLiveData()
    public val currentVideo: LiveData<VideoModel> = _currentVideo

    fun layoutToggle() {
        _isListLiveData.value = isListLiveData.value == false
    }

    fun videos(videoList: ArrayList<VideoModel>?) {
        _videoListLiveData.value = videoList
    }

    fun currentVideo(mediaId: String?) {
        val currentVideo =
            kotlin.runCatching { _videoListLiveData.value?.first { it.contentID == mediaId } }
                .getOrNull()
        if (currentVideo != null) {
            _currentVideo.value = currentVideo
        }
    }

    private fun showLoader() = _progressbarVisibility.postValue(View.VISIBLE)
    private fun hideLoader() = _progressbarVisibility.postValue(View.GONE)
}