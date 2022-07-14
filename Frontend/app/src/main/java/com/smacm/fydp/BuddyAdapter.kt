package com.smacm.fydp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.buddy.view.*
import kotlinx.android.synthetic.main.simple_expander.view.*

class BuddyAdapter(val buddyList: ArrayList<String>) :
    RecyclerView.Adapter<BuddyAdapter.BuddyViewHolder>() {

    class BuddyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuddyAdapter.BuddyViewHolder {
        return BuddyAdapter.BuddyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate( // takes 4 arguments, all optional: resources, parser, resources, parser
                    R.layout.buddy, // R is to access resources, layout is a resource
                    parent,
                    false // don't wanna attach this view to the root layout, set to false
                )
        )
    }

    override fun onBindViewHolder(holder: BuddyViewHolder, position: Int) {
        val currBuddy: String = buddyList[position]

        holder.itemView.apply {
            tv_first_name.text = currBuddy
        }
    }

    override fun getItemCount() = buddyList.size

}