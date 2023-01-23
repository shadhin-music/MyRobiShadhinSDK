package com.myrobi.shadhinmusiclibrary.fragments.subscription

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


    private val _error:MutableLiveData<ApiError> = MutableLiveData()
    val error:LiveData<ApiError> = _error

    private val _subscriptionResponse:MutableSharedFlow<SubscriptionResponse> = MutableSharedFlow()
    val subscriptionResponse:Flow<SubscriptionResponse> = _subscriptionResponse.asSharedFlow()

    private val _plans:MutableLiveData<List<Plan>> = MutableLiveData()
    val plans:LiveData<List<Plan>> = _plans






   private val exceptionHandler = Dispatchers.IO+CoroutineExceptionHandler { _, exception ->
       _isLoading.postValue(false)
       _error.postValue(exception.toApiError())
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
    fun requestSubscription(paymentMethod: PaymentMethod) = viewModelScope.launch{
        subscriptionRepository.subscriptionRequest(paymentMethod)?.let { response ->
            delay(1000)
            _subscriptionResponse.emit(response)
        }
    }

    fun cancelSubscription() = viewModelScope.launch{
        subscriptionRepository.cancel(PaymentMethod.Robi(subscriptionRepository.fetchSubscriptionPlan()))
        fetchSubscriptionPlan(true)
    }
}