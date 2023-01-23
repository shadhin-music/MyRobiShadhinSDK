package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.subscription.PaymentMethod
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionDetails
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Status
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint
import com.myrobi.shadhinmusiclibrary.utils.px
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.lang.Runnable


class SubscriptionFragment : Fragment(), FragmentEntryPoint {
    private lateinit var viewModel: SubscriptionViewModel
    private var progress: View? = null
    private var planList: View? = null
    private var paymentList: View? = null
    private var planCard: View? = null
    private var planName: TextView? = null
    private var planDuration: TextView? = null
    private var planExtraText: TextView? = null
    private var cancelButton: CardView? = null
    private var price: TextView? = null
    private var errorMessage: TextView? = null
    private var errorLayout: View? = null
    private var parentLayout: View? = null
    private var topImage: ImageView? = null
    private var scrollView: NestedScrollView? = null
    private var robiSubButton: CardView? = null
    private var backButton2: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_subscription_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupViewModel()
        uiAction()
        observeData()
    }

    private fun uiAction() {

        robiSubButton?.setOnClickListener {
            val paymentMethod = PaymentMethod.Robi()
            val bundle =
                bundleOf(
                    SUBSCRIPTION_DETAILS_ARGS
                            to
                            SubscriptionDetails(paymentMethod)
                )
            lifecycleScope.launch {
                viewModel.loadPlans(paymentMethod)
                findNavController().navigate(R.id.to_subscription_dialog, bundle)
            }
        }

        cancelButton?.setOnClickListener {
            viewModel.cancelSubscription()
        }
    }



    private fun observeData() {



        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progressVisibility(isLoading)
        })
        viewModel.activePlan.observe(viewLifecycleOwner, Observer { plan ->
            planUiUpdate(plan)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            errorUiVisibility(!error.message.isNullOrEmpty(), error.message)
        })



        viewModel.fetchSubscriptionPlan(true)

    }

    private fun errorUiVisibility(isError: Boolean, message: String? = null) {
        if (isError) {
            errorLayout?.visibility = View.VISIBLE
            parentLayout?.visibility = View.GONE
            message?.let { errorMessage?.text = it }
        } else {
            errorLayout?.visibility = View.GONE
            parentLayout?.visibility = View.VISIBLE
        }
    }

    private fun progressVisibility(isLoading: Boolean) {
        if (isLoading) {
            progress?.visibility = View.VISIBLE
        } else {
            progress?.visibility = View.GONE
        }
    }


    private fun planUiUpdate(plan: Plan?) {
        if(plan?.type == null){
            cancelButton?.visibility = View.INVISIBLE
            price?.visibility= View.INVISIBLE

        }
        planAndPaymentVisibility(plan?.status == Status.SUBSCRIBED)
        if (plan != null) {
            errorUiVisibility(false)
            planName?.text = "${plan.title} Plan Active"
            planDuration?.text = plan.duration
            price?.text = plan.amountWithCurrency
            planExtraText?.text = plan.extraVatText
        }
    }

    private fun planAndPaymentVisibility(isActive: Boolean) {
        if (isActive) {
            planList?.visibility = View.VISIBLE
            paymentList?.visibility = View.GONE
        } else {
            planList?.visibility = View.GONE
            paymentList?.visibility = View.VISIBLE
        }
    }

    private fun setupUi(view: View) {
        parentLayout = view.findViewById(R.id.parent_layout)
        errorLayout = view.findViewById(R.id.error_layout)
        progress = view.findViewById(R.id.home_progress)
        planList = view.findViewById(R.id.plan_list)
        paymentList = view.findViewById(R.id.payment_list)
        planCard = view.findViewById(R.id.plan_card)
        planName = view.findViewById(R.id.plan_name)
        planDuration = view.findViewById(R.id.plan_duration)
        planExtraText = view.findViewById(R.id.plan_extra_text)
        cancelButton = view.findViewById(R.id.cancel_button_card)
        errorLayout = view.findViewById(R.id.error_layout)
        errorMessage = view.findViewById(R.id.location_error_tv)
        price = view.findViewById(R.id.price)
        topImage = view.findViewById(R.id.imageView10)
        scrollView = view.findViewById(R.id.listscrollview)
        robiSubButton = view.findViewById(R.id.robiAirBtn)
        backButton2 = view.findViewById(R.id.backButton2)


        topImage?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val height = topImage?.measuredHeight ?: 0
                scrollView?.setPadding(0, height - (72.px / 2), 0, 0)
                topImage?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
        backButton2?.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            injector.subscriptionViewModelFactory
        )[SubscriptionViewModel::class.java]
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
        topImage = null
        scrollView = null
        robiSubButton = null
        super.onDestroyView()
    }

    companion object {
        const val SUBSCRIPTION_DETAILS_ARGS = "sub_details_args"
        const val SUBSCRIPTION_PLAN_ARG = "sub_plan_args"
    }

}