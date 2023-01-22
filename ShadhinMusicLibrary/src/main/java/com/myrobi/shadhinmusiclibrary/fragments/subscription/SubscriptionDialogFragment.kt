package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.adapter.subscription.SubscriptionPlanAdapter
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionDetails
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint

class SubscriptionDialogFragment:BottomSheetDialogFragment(),FragmentEntryPoint {
    private lateinit var planAdapter: SubscriptionPlanAdapter
    private var recyclerView:RecyclerView?=null
    private var paymentMethod: TextView?=null
    private var icon: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SheetDialogV2)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_subscription_plan,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)
        setupAdapter()
        readData()

    }

    private fun setupUi(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        paymentMethod = view.findViewById(R.id.payment_method)
    }

    private fun setupAdapter() {
        planAdapter = SubscriptionPlanAdapter()
        recyclerView?.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView?.adapter = planAdapter

        planAdapter.onSubscriptionClickListeners {
            Toast.makeText(requireActivity(),it.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData() {
        val details:SubscriptionDetails? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(SubscriptionFragment.SUBSCRIPTION_DETAILS_ARGS,SubscriptionDetails::class.java)
        }else{
            arguments?.getSerializable(SubscriptionFragment.SUBSCRIPTION_DETAILS_ARGS) as SubscriptionDetails
        }
        details?.paymentMethod?.icon?.let { icon?.setImageResource(it) }
        paymentMethod?.text = details?.paymentMethod?.name

        planAdapter.submitList(details?.plans)

    }


}