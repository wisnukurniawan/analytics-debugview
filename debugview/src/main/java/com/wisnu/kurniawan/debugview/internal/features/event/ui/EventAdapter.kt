package com.wisnu.kurniawan.debugview.internal.features.event.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.foundation.extension.formatDateTime
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toJson
import com.wisnu.kurniawan.debugview.internal.model.Event

internal class EventAdapter(
    private val onItemClicked: (Event) -> Unit
) : ListAdapter<Event, RecyclerView.ViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.debugview_layout_event_item, parent, false)
        return EventViewHolder(itemView) { position ->
            onItemClicked(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != RecyclerView.NO_POSITION) {
            val item = getItem(position)
            if (holder is EventViewHolder) {
                holder.bindData(item)
            }
        }
    }
}

internal class EventDiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(
        oldItem: Event,
        newItem: Event
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Event,
        newItem: Event
    ): Boolean {
        return oldItem == newItem
    }

}

internal class EventViewHolder(
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

    fun bindData(event: Event) {
        itemView.findViewById<TextView>(R.id.event_item_tv).text = event.name
        itemView.findViewById<TextView>(R.id.event_item_prop_tv).text = "${event.createdAt.formatDateTime()} ${toJson(event.properties)}"
    }

}
