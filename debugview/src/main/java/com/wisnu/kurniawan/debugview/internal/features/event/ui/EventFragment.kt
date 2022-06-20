package com.wisnu.kurniawan.debugview.internal.features.event.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.analytic.ui.AnalyticFragment
import com.wisnu.kurniawan.debugview.internal.features.event.data.IEventEnvironment
import com.wisnu.kurniawan.debugview.internal.features.event.di.EventModule
import com.wisnu.kurniawan.debugview.internal.features.eventdetails.ui.EventDetailsFragment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.EventFilterFragment
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import kotlinx.coroutines.launch

internal class EventFragment : Fragment(R.layout.debugview_fragment_event) {

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

        requireArguments().getString(AnalyticFragment.EXTRA_TAG)?.let {
            viewModel.dispatch(EventAction.Launch(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)
        initRecyclerView(view)
        initRecordingButton(view)

        requireActivity().supportFragmentManager.setFragmentResultListener(RC_APPLY_FILTER, viewLifecycleOwner) { _, _ ->
            viewModel.dispatch(EventAction.ApplyFilter)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        renderContent(it, view)
                        renderRecordingButton(it, view)
                        renderFilter(it, view)
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is EventEffect.NavigateToEventDetails -> navigateToEventDetailsFragment(it.id)
                            is EventEffect.ShowFilterSheet -> showFilterSheet()
                            EventEffect.Cleared -> showToastCleared()
                        }
                    }
                }
            }
        }
    }

    private fun initToolbar(view: View) {
        val toolbar = view.findViewById<MaterialToolbar>(R.id.event_toolbar)

        requireArguments().getString(AnalyticFragment.EXTRA_TAG)?.let {
            toolbar.title = it
        }
        requireArguments().getBoolean(AnalyticFragment.EXTRA_IS_SINGLE).let {
            if (it) {
                toolbar.navigationIcon = null
            }
        }

        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    true
                }
                R.id.action_clear -> {
                    viewModel.dispatch(EventAction.ClickClearAll)
                    true
                }
                R.id.action_filter -> {
                    viewModel.dispatch(EventAction.ClickFilter)
                    true
                }
                else -> {
                    false
                }
            }
        }

        val actionSearch = toolbar.menu.findItem(R.id.action_search)
        val actionFilter = toolbar.menu.findItem(R.id.action_filter)
        val actionClear = toolbar.menu.findItem(R.id.action_clear)

        actionSearch.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                actionFilter.isVisible = false
                actionClear.isVisible = false
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                actionFilter.isVisible = true
                actionClear.isVisible = true
                return true
            }
        })
        (actionSearch.actionView as SearchView).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.dispatch(EventAction.InputSearchEvent(query.orEmpty()))
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.dispatch(EventAction.InputSearchEvent(newText.orEmpty()))
                    return true
                }
            }
        )
        val editText = actionSearch.actionView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        editText.hint = getString(R.string.debugview_event_search_hint)
    }

    private fun initRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.event_rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    private fun renderContent(state: EventState, view: View) {
        val emptyView = view.findViewById<AppCompatTextView>(R.id.event_empty_tv)
        val rv = view.findViewById<RecyclerView>(R.id.event_rv)
        if (state.events.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            rv.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            rv.visibility = View.VISIBLE
            adapter.submitList(state.events)
        }
    }

    private fun renderRecordingButton(state: EventState, view: View) {
        val recordingButton = view.findViewById<MaterialButton>(R.id.event_recording_button)
        val value = TypedValue()
        if (state.analytic.isRecording) {
            requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorError, value, true)
            recordingButton.setText(R.string.debugview_event_stop_recording)
            recordingButton.setBackgroundColor(value.data)
        } else {
            requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, value, true)
            recordingButton.setText(R.string.debugview_event_start_recording)
            recordingButton.setBackgroundColor(value.data)
        }
    }

    private fun initRecordingButton(view: View) {
        view.findViewById<MaterialButton>(R.id.event_recording_button).setOnClickListener {
            viewModel.dispatch(EventAction.ToggleRecording)
        }
    }

    private fun renderFilter(state: EventState, view: View) {
        val toolbar = view.findViewById<MaterialToolbar>(R.id.event_toolbar)
        val actionFilter = toolbar.menu.findItem(R.id.action_filter)

        if (state.isFilterApplied) {
            actionFilter.icon = ContextCompat.getDrawable(requireContext(), R.drawable.debugview_ic_filter_applied)
        } else {
            actionFilter.icon = ContextCompat.getDrawable(requireContext(), R.drawable.debugview_ic_filter_default)
        }
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

    private fun showFilterSheet() {
        val modalBottomSheet = EventFilterFragment()
        modalBottomSheet.show(requireActivity().supportFragmentManager, EventFilterFragment.TAG)
    }

    private fun showToastCleared() {
        Toast.makeText(requireContext(), getString(R.string.debugview_event_cleared), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_EVENT_ID = "EXTRA_EVENT_ID"
        const val RC_APPLY_FILTER = "RC_APPLY_FILTER"
    }

}
