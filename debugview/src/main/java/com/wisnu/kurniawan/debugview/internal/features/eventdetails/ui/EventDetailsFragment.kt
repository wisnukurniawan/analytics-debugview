package com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventFragment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.data.IEventDetailsEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.di.EventDetailModule
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import kotlinx.coroutines.launch


internal class EventDetailsFragment : Fragment(R.layout.debugview_fragment_event_details) {

    lateinit var environment: IEventDetailsEnvironment
    lateinit var viewModel: EventDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventDetailModule.inject(this, DataModule.localManager)
        EventDetailModule.inject(this, this, environment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // date
        // event name
        // prop

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        // todo empty prop
                        view.findViewById<TextView>(R.id.event_details_tv).text = it.event.name
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is EventDetailsEffect.Copy -> {

                            }
                            is EventDetailsEffect.Share -> {

                            }
                        }
                    }
                }
            }
        }

        requireArguments().getString(EventFragment.EXTRA_EVENT_ID)?.let {
            viewModel.dispatch(EventDetailsAction.Launch(it))
        }
    }

}
