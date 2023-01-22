package com.myrobi.shadhinmusiclibrary.adapter.subscription

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.subscription.Plan
import com.myrobi.shadhinmusiclibrary.data.model.subscription.SubscriptionPlan

internal typealias SubscriptionFunc = (Plan) ->Unit
internal class SubscriptionPlanAdapter: ListAdapter<Plan,SubscriptionPlanAdapter.ViewHolder>(SubscriptionPlanDiffCallBack()){
    private var subscriptionFunc:SubscriptionFunc?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view  = layoutInflater.inflate(R.layout.row_plan_sub,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }
    public fun onSubscriptionClickListeners(subscriptionFunc:SubscriptionFunc){
        this.subscriptionFunc = subscriptionFunc
    }

    class  SubscriptionPlanDiffCallBack: DiffUtil.ItemCallback<Plan>() {
        override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem.serviceId == newItem.serviceId
        }

        override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
            return oldItem as SubscriptionPlan == newItem as SubscriptionPlan
        }

    }
    inner class ViewHolder (val itemView:View) : RecyclerView.ViewHolder(itemView){
        fun bind(plan: Plan){

            val icon:ImageView = itemView.findViewById(R.id.icon)
            val title:TextView = itemView.findViewById(R.id.title)
            val planAndDuration:TextView = itemView.findViewById(R.id.plan_and_duration)
            val extraText:TextView = itemView.findViewById(R.id.extra_text)
            val card:CardView = itemView.findViewById(R.id.cardView8)
            plan.type?.icon?.let { icon.setImageResource(it) }
            title.text = plan.title
            planAndDuration.text = "${plan.amountWithCurrency} / ${plan.duration}"
            extraText.text = plan.desc

            card.setOnClickListener {
                subscriptionFunc?.invoke(plan)
            }

        }
    }

}
