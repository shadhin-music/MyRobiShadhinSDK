package com.myrobi.shadhinmusiclibrary.fragments.subscription

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.di.FragmentEntryPoint

internal class SubscriptionNotFoundForVideoDialogFragment: BottomSheetDialogFragment() , FragmentEntryPoint {
   private  var bottomSheet:View?=null
    private var parent:View?=null
    private var btnMonthlyPlan:View?=null
    private var btnSeeAll:View?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.SheetDialogV2)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_subscription_dialog,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi(view)

    }

    private fun setupUi(view: View) {
        parent = view.findViewById(R.id.parent)
        btnMonthlyPlan = view.findViewById(R.id.btnMonthlyPlan)
        btnSeeAll = view.findViewById(R.id.btnSeeAll)


        parent?.setOnClickListener { dismiss() }
        btnSeeAll?.setOnClickListener { goToSubscription() }
        btnMonthlyPlan?.setOnClickListener { goToSubscription() }

    }
    private fun goToSubscription(){
        val transaction= parentFragmentManager.beginTransaction();
                transaction.replace(R.id.fcv_navigation_host, SubscriptionFragmentForVideo());
                transaction.addToBackStack(null);
                transaction.commit();
                 dismiss()
      //  findNavController().navigate(R.id.to_subscription)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            bottomSheet = dialog.findViewById(R.id.design_bottom_sheet)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            val view = view
            view?.post {
                val parent = view.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
                bottomSheetBehavior!!.peekHeight = view.measuredHeight
                // Here you can play with the height of the bottom sheet like pass height as- [view.getMeasuredHeight()/2]
                (bottomSheet?.parent as View).setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}