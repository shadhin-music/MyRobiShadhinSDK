package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint


class SubscriptionFragment: Fragment(),FragmentEntryPoint {
    private lateinit var viewModel: SubscriptionViewModel


    private var progress:View? = null
    private var planList:View? = null
    private var paymentList:View? = null
    private var planCard:View?=null
    private var planName:TextView?=null
    private var planDuration:TextView?=null
    private var planExtraText:TextView?=null
    private var cancelButton:CardView?=null
    private var price:TextView?=null
    private var errorMessage:TextView?=null
    private var errorLayout:View?=null
    private var parentLayout:View?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_subscription_fragment,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupViewModel()
        observeData()

        planCard?.setOnClickListener {
            viewModel.haveActiveSubscriptionPlan(false)
            Toast.makeText(requireContext(),"cc",Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            progressVisibility(isLoading)
        }
        viewModel.activePlan.observe(viewLifecycleOwner){ plan ->
            planUiUpdate(plan)
        }
        viewModel.error.observe(viewLifecycleOwner){error ->
            errorUiVisibility(!error.message.isNullOrEmpty(),error.message)
        }

        viewModel.fetchSubscriptionPlan(true)

    }

    private fun errorUiVisibility(isError: Boolean,message:String?=null) {
        if(isError) {
            errorLayout?.visibility = View.VISIBLE
            parentLayout?.visibility = View.GONE
            message?.let { errorMessage?.text = it }
        }else{
            errorLayout?.visibility = View.GONE
            parentLayout?.visibility = View.VISIBLE
        }
    }

    private fun progressVisibility(isLoading: Boolean) {
        if(isLoading){
            progress?.visibility = View.VISIBLE
        }else{
            progress?.visibility = View.GONE
        }
    }


    private fun planUiUpdate(plan: Plan?) {
        planAndPaymentVisibility(plan?.status == Status.SUBSCRIBED)
        if(plan !=null) {
            errorUiVisibility(false)
            planName?.text = "${plan.title} Plan Active"
            planDuration?.text = plan.duration
            price?.text = plan.amountWithCurrency
            planExtraText?.text = plan.extraVatText
        }
    }

    private fun planAndPaymentVisibility(isActive: Boolean) {
        if(isActive){
            planList?.visibility = View.VISIBLE
            paymentList?.visibility = View.GONE
        }else{
            planList?.visibility = View.GONE
            paymentList?.visibility = View.VISIBLE
        }
    }

    private fun setupUi(view: View) {
        parentLayout = view.findViewById(R.id.parent_layout)
        errorLayout = view.findViewById(R.id.error_layout)
        progress = view.findViewById(R.id.home_progress)
        planList  = view.findViewById(R.id.plan_list)
        paymentList  = view.findViewById(R.id.payment_list)
        planCard  = view.findViewById(R.id.plan_card)
        planName  = view.findViewById(R.id.plan_name)
        planDuration  = view.findViewById(R.id.plan_duration)
        planExtraText  = view.findViewById(R.id.plan_extra_text)
        cancelButton  = view.findViewById(R.id.cancel_button_card)
        errorLayout = view.findViewById(R.id.error_layout)
        errorMessage = view.findViewById(R.id.location_error_tv)
        price  = view.findViewById(R.id.price)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,injector.subscriptionViewModelFactory)[SubscriptionViewModel::class.java]
    }

    override fun onDestroyView() {
        progress = null
        planList = null
        paymentList = null
        planCard = null
        planName = null
        planDuration = null
        planExtraText = null
        cancelButton = null
        price = null
        errorLayout = null
        parentLayout = null
        errorMessage = null
        super.onDestroyView()
    }

}