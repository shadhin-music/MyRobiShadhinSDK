package com.shadhinmusiclibrary.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shadhinmusiclibrary.data.model.HomeDataModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.data.model.RBTModel
import com.shadhinmusiclibrary.data.repository.HomeContentRepository
import com.shadhinmusiclibrary.utils.ApiResponse
import com.shadhinmusiclibrary.utils.Status
import kotlinx.coroutines.launch

internal class HomeViewModel(private val homeContentRepository: HomeContentRepository) :
    ViewModel() {

    private val _isLoading:MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading:LiveData<Boolean> = _isLoading

    private val _homeContent: MutableLiveData<ApiResponse<HomeDataModel>> = MutableLiveData()
    val homeContent: LiveData<ApiResponse<HomeDataModel>> = _homeContent

    private val _patchItem: MutableLiveData<HomePatchItemModel> = MutableLiveData()
    val patchItem: LiveData<HomePatchItemModel> = _patchItem


    private val _urlContent: MutableLiveData<ApiResponse<RBTModel>> = MutableLiveData()
    val urlContent: LiveData<ApiResponse<RBTModel>> = _urlContent

    fun fetchHomeData(pageNumber: Int?, isPaid: Boolean?) = viewModelScope.launch {
        val response = homeContentRepository.fetchHomeData(pageNumber, isPaid)
        _homeContent.postValue(response)
    }


    fun fetchPatchData(patchCode: String) = viewModelScope.launch {
        _isLoading.postValue(true)
        val response = homeContentRepository.fetchPatchData(patchCode)
        if(response.status == Status.SUCCESS && response.data?.data !=null){
            _patchItem.postValue(response.data.data)
        }
        _isLoading.postValue(false)
    }


    fun fetchRBTURL() = viewModelScope.launch {
        val response = homeContentRepository.rbtURL()
        _urlContent.postValue(response)
    }
}