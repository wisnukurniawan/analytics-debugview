package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.appbar.MaterialToolbar
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventFragment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.data.IEventDetailsEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.di.EventDetailModule
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import com.wisnu.kurniawan.debugview.internal.foundation.extension.formatDateTime
import com.wisnu.kurniawan.debugview.internal.foundation.extension.propertyDisplay
import kotlinx.coroutines.launch


internal class EventDetailsFragment : Fragment(R.layout.debugview_fragment_event_details) {

    lateinit var environment: IEventDetailsEnvironment
    lateinit var viewModel: EventDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventDetailModule.inject(this, DataModule.localManager)
        EventDetailModule.inject(this, this, environment)

        requireArguments().getString(EventFragment.EXTRA_EVENT_ID)?.let {
            viewModel.dispatch(EventDetailsAction.Launch(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        renderEventName(it, view)
                        renderEventProperty(it, view)
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is EventDetailsEffect.Copy -> {
                                copy(it.content)
                            }
                            is EventDetailsEffect.Share -> {
                                share(it.content)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initToolbar(view: View) {
        val toolbar = view.findViewById<MaterialToolbar>(R.id.event_details_toolbar)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_share -> {
                    viewModel.dispatch(EventDetailsAction.Share)
                    true
                }
                R.id.action_copy -> {
                    viewModel.dispatch(EventDetailsAction.Copy)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun renderEventName(state: EventDetailsState, view: View) {
        view.findViewById<TextView>(R.id.event_details_date).text = state.event.createdAt.formatDateTime()

        view.findViewById<TextView>(R.id.event_details_name).text = state.event.name
    }

    private fun renderEventProperty(state: EventDetailsState, view: View) {
        if (state.event.properties.isNotEmpty()) {
            view.findViewById<TextView>(R.id.event_details_empty).visibility = View.GONE
            view.findViewById<TextView>(R.id.event_details_property).text = state.event.propertyDisplay()
        } else {
            view.findViewById<TextView>(R.id.event_details_empty).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.event_details_property).text = ""
        }
    }

    private fun share(content: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, content)
        sendIntent.type = "text/plain"
        startActivity(Intent.createChooser(sendIntent, null))
    }

    private fun copy(content: String) {
        val clipBoard = getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", content)
        clipBoard?.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.debugview_event_copied), Toast.LENGTH_SHORT).show()
    }

}
