package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint


class SubscriptionFragment: Fragment(),FragmentEntryPoint {
    private lateinit var viewModel: SubscriptionViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_subscription_fragment,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()

        view.findViewById<Button>(R.id.test_btn_1).setOnClickListener {
            viewModel.fetchSubscriptionPlan()
        }
        view.findViewById<Button>(R.id.test_btn_2).setOnClickListener {
            viewModel.haveActiveSubscriptionPlan()
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this,injector.subscriptionViewModelFactory)[SubscriptionViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}