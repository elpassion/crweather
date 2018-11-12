package com.elpassion.crweather

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chart.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ChartsAdapter : RecyclerView.Adapter<ChartsAdapter.Holder>() {

    var charts = emptyList<Chart>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent.inflate(R.layout.chart))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.chart.chart = charts[position]
    }

    override fun getItemCount() = charts.size

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}