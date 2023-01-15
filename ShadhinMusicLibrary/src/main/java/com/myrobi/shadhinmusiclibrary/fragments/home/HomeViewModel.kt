package com.myrobi.shadhinmusiclibrary.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.myrobi.shadhinmusiclibrary.data.model.HomeDataModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.myrobi.shadhinmusiclibrary.data.model.RBTModel
import com.myrobi.shadhinmusiclibrary.data.repository.HomeContentRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiResponse
import com.myrobi.shadhinmusiclibrary.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class HomeViewModel(private val homeContentRepository: HomeContentRepository) :
    ViewModel() {
    private val patchSet: MutableSet<HomePatchItemModel> = LinkedHashSet()
    private val _isLoading:MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading:LiveData<Boolean> = _isLoading

    private val _homeContent: MutableLiveData<ApiResponse<HomeDataModel>> = MutableLiveData()
    val homeContent: LiveData<ApiResponse<HomeDataModel>> = _homeContent

    private val _patchItem: MutableLiveData<HomePatchItemModel> = MutableLiveData()
    val patchItem: LiveData<HomePatchItemModel> = _patchItem


    private val _urlContent: MutableLiveData<ApiResponse<RBTModel>> = MutableLiveData()
    val urlContent: LiveData<ApiResponse<RBTModel>> = _urlContent

    private val _patchList: MutableLiveData<List<HomePatchItemModel>> = MutableLiveData()
    val patchList: LiveData<List<HomePatchItemModel>> = _patchList

    fun fetchHomeData() = viewModelScope.launch(Dispatchers.IO) {
        var total:Int = 0
        var pageNumber:Int = 1
        do{
            val response = homeContentRepository.fetchHomeData(pageNumber, false)
            total = response.data?.total?:1
            response.data?.data?.let { patchs ->

                if(pageNumber == 2){
                    patchs.add(0,HomePatchItemModel("002", "download", listOf(), "download", "download", 0, 0))
                }
                patchSet.addAll(patchs)
                _patchList.postValue(patchSet.toList())
            }
            pageNumber++

        }while (pageNumber<=total)

    }
  /*  fun fetchHomeData(pageNumber: Int?, isPaid: Boolean?) = viewModelScope.launch {
        val response = homeContentRepository.fetchHomeData(pageNumber, isPaid)
        _homeContent.postValue(response)
    }*/


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