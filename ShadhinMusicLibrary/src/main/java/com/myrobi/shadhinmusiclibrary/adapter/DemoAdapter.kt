package com.myrobi.shadhinmusiclibrary.adapter

import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View.inflate
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

//class DemoAdapter: ListAdapter<String,DemoAdapter.DemoViewHolder>(DemoDiffCallBack()){
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
//        return DemoViewHolder.from(parent)
//    }
//
//    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//    class DemoViewHolder private constructor(val binding: ScriptGroup.Binding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(item: String){
//            //binding.item = item
//            binding.executePendingBindings()
//        }
//        companion object {
//            fun from(parent:ViewGroup): DemoViewHolder {
//                val layoutInflater = LayoutInflater.from(parent.context)
//                val binding  =  LayoutInflater
//                    .from(parent.context).inflate(layoutInflater,parent,false)
//                return DemoViewHolder(binding)
//            }
//        }
//    }
//}
//class  DemoDiffCallBack: DiffUtil.ItemCallback<String>() {
//    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
//        return oldItem == newItem
//    }
//
//}