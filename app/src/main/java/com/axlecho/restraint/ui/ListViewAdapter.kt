package com.axlecho.restraint.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.axlecho.restraint.R
import com.axlecho.restraint.data.Info
import com.axlecho.restraint.utils.TimeUtils


class ListViewAdapter(private val context:Context,private val dataSet:List<Info>): RecyclerView.Adapter<ListViewAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usage, parent, false) as View
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = dataSet[position].name
        holder.time.text = TimeUtils.readableTime(dataSet[position].time)

        val info = context.packageManager.getApplicationInfo(dataSet[position].name, 0)
        holder.icon.setImageDrawable(info.loadIcon(context.packageManager))
    }

    override fun getItemCount() = dataSet.size

    class Holder( view: View) :RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.item_title)
        val time = view.findViewById<TextView>(R.id.item_time)
        val icon = view.findViewById<ImageView>(R.id.item_avatar)
    }
}