package com.myrobi.shadhinmusiclibrary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.data.model.comments.CommentData


internal class PodcastCommentsHeaderAdapter(val data: List<CommentData>) : RecyclerView.Adapter<PodcastCommentsHeaderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_bl_sdk_layout_podcast_comment_header, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()


    }
    override fun getItemViewType(position: Int) = VIEW_TYPE
    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val  context = itemView.getContext()
        fun bindItems() {

//            val recyclerView: RecyclerView = itemView.findViewById(R.id.comment_rv)
//            recyclerView.layoutManager =
//                LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false )
//            recyclerView.adapter = CommentsAdapter()


        }

    }
    companion object {
        const val VIEW_TYPE = 4
    }
}