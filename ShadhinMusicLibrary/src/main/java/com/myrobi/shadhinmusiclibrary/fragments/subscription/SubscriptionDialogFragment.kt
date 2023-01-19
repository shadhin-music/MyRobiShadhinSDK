package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint

class SubscriptionDialogFragment:BottomSheetDialogFragment(),FragmentEntryPoint {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_subscription_plan,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}