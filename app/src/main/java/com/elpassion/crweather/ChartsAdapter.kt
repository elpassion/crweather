package com.elpassion.crweather

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.chart.view.*


class ChartsAdapter() : RecyclerView.Adapter<ChartsAdapter.Holder>() {

    var charts = emptyList<Chart>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent.inflate(R.layout.chart))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val description = StringBuilder()
        for (line in charts[position].lines)
            description += "${line.name}\n"
        holder.itemView.description.text = description.toString()
    }

    override fun getItemCount() = charts.size

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}