package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionResponse
import com.myrobi.shadhinmusiclibrary.data.repository.subscription.SubscriptionRepository
import com.myrobi.shadhinmusiclibrary.utils.ApiError
import com.myrobi.shadhinmusiclibrary.utils.toApiError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

const val TAG = "SubscriptionViewModel"
internal class SubscriptionViewModel(private val subscriptionRepository: SubscriptionRepository): ViewModel() {

    private val _activePlan:MutableLiveData<Plan?> = MutableLiveData()
    val activePlan:LiveData<Plan?> = _activePlan

    private val _haveActivePlan:MutableLiveData<Boolean> = MutableLiveData(false)
    val haveActivePlan:LiveData<Boolean> = _haveActivePlan
    private val _isLoading:MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading:LiveData<Boolean> = _isLoading


    private val _error:MutableSharedFlow<ApiError> = MutableSharedFlow()
    val error:Flow<ApiError> = _error.asSharedFlow()

    private val _subscriptionResponse:MutableSharedFlow<SubscriptionResponse> = MutableSharedFlow()
    val subscriptionResponse:Flow<SubscriptionResponse> = _subscriptionResponse.asSharedFlow()

    private val _plans:MutableLiveData<List<Plan>> = MutableLiveData()
    val plans:LiveData<List<Plan>> = _plans

   private val exceptionHandler = Dispatchers.IO+CoroutineExceptionHandler { _, exception ->
       _isLoading.postValue(false)
       viewModelScope.launch {
           _error.emit(exception.toApiError())
       }

    }
    fun haveActiveSubscriptionPlan(reload:Boolean = false) = viewModelScope.launch(exceptionHandler) {
        _isLoading.postValue(true)
        val have = subscriptionRepository.haveActiveSubscriptionPlan(reload)
        _haveActivePlan.postValue(have)
        _isLoading.postValue(false)
    }
    fun fetchSubscriptionPlan(reload:Boolean = false) = viewModelScope.launch(exceptionHandler) {
        _isLoading.postValue(true)

        val plan = subscriptionRepository.fetchSubscriptionPlan(reload)
        Log.i(TAG, "haveActiveSubscriptionPlan: ${plan.toString()}")
        _activePlan.postValue(plan)
        _isLoading.postValue(false)
    }
   suspend fun loadPlans(paymentMethod:PaymentMethod){
        _isLoading.postValue(true)
        subscriptionRepository.plans(paymentMethod)?.let { plans->


            _plans.postValue(plans)
        }
        _isLoading.postValue(false)
    }
   suspend fun requestSubscription(paymentMethod: PaymentMethod): SubscriptionResponse? {
        return subscriptionRepository.subscriptionRequest(paymentMethod)
    }

    fun cancelSubscription() = viewModelScope.launch{
        _isLoading.postValue(true)
        subscriptionRepository.cancel(PaymentMethod.Robi(subscriptionRepository.fetchSubscriptionPlan()))
        fetchSubscriptionPlan(true)

    }
}