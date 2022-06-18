package com.wisnu.kurniawan.debugview.internal.features.event.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.analytic.ui.AnalyticFragment
import com.wisnu.kurniawan.debugview.internal.features.event.data.IEventEnvironment
import com.wisnu.kurniawan.debugview.internal.features.event.di.EventModule
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui.EventDetailsFragment
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import kotlinx.coroutines.launch

internal class EventFragment : Fragment(R.layout.fragment_event) {

    private val adapter: EventAdapter by lazy {
        EventAdapter {
            viewModel.dispatch(EventAction.ClickEventItem(it.id))
        }
    }

    lateinit var environment: IEventEnvironment
    lateinit var viewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventModule.inject(this, DataModule.localManager)
        EventModule.inject(this, this, environment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // todo delete all event

        initRecyclerView(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        // todo empty state
                        adapter.submitList(it.events)
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is EventEffect.NavigateToEventDetails -> navigateToEventDetailsFragment(it.id)
                        }
                    }
                }
            }
        }

        requireArguments().getString(AnalyticFragment.EXTRA_TAG)?.let {
            viewModel.dispatch(EventAction.Launch(it))
        }
    }

    private fun initRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.event_rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    private fun navigateToEventDetailsFragment(id: String) {
        val bundle = Bundle()
        bundle.putString(EXTRA_EVENT_ID, id)

        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.beginTransaction()
            ?.replace(R.id.analytic_fragment, EventDetailsFragment::class.java, bundle)
            ?.setReorderingAllowed(true)
            ?.addToBackStack(null)
            ?.commit()
    }

    companion object {
        const val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
    }

}
