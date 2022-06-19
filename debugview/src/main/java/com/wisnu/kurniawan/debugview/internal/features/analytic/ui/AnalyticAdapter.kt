package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.model.Analytic


internal class AnalyticAdapter(
    private val onItemClicked: (Analytic) -> Unit
) : ListAdapter<Analytic, RecyclerView.ViewHolder>(AnalyticDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.debugview_layout_analytic_item, parent, false)
        return AnalyticViewHolder(itemView) { position ->
            onItemClicked(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            if (holder is AnalyticViewHolder) {
                holder.bindData(item)
            }
        }
    }
}

internal class AnalyticDiffCallback : DiffUtil.ItemCallback<Analytic>() {

    override fun areItemsTheSame(
        oldItem: Analytic,
        newItem: Analytic
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Analytic,
        newItem: Analytic
    ): Boolean {
        return oldItem == newItem
    }

}

internal class AnalyticViewHolder(
    view: View,
    onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked.invoke(adapterPosition)
            }
        }
    }

    fun bindData(analytic: Analytic) {
        itemView.findViewById<TextView>(R.id.analytic_item_tv).text = analytic.tag

        val status = itemView.findViewById<TextView>(R.id.analytic_item_status_tv)
        val value = TypedValue()
        if (analytic.isRecording) {
            itemView.context.theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, value, true)
            status.setTextColor(value.data)
            status.alpha = 1.0F
            status.text = itemView.context.getString(R.string.debug_view_analytic_recording)
        } else {
            itemView.context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSurface, value, true)
            status.setTextColor(value.data)
            status.alpha = 0.3F
            status.text = itemView.context.getString(R.string.debug_view_analytic_inactive)
        }
    }

}
