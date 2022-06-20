package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.analytic.data.IAnalyticEnvironment
import com.wisnu.kurniawan.debugview.internal.features.analytic.di.AnalyticModule
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventFragment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui.EventFilterFragment
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import kotlinx.coroutines.launch

internal class AnalyticFragment : Fragment(R.layout.debugview_fragment_analytic) {

    private val adapter: AnalyticAdapter by lazy {
        AnalyticAdapter {
            viewModel.dispatch(AnalyticAction.ClickAnalyticItem(it.tag))
        }
    }

    lateinit var environment: IAnalyticEnvironment
    lateinit var viewModel: AnalyticViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnalyticModule.inject(this, DataModule.localManager)
        AnalyticModule.inject(this, this, environment)

        viewModel.dispatch(AnalyticAction.Launch(activity?.intent?.extras?.getString(EXTRA_TAG).orEmpty()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(view)
        initRecyclerView(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        renderContent(it)
                        renderFilter(it, view)
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is AnalyticEffect.NavigateToEvent -> navigateToEventFragment(it.tag)
                            AnalyticEffect.ShowFilterSheet -> showFilterSheet()
                        }
                    }
                }
            }
        }
    }

    private fun initToolbar(view: View) {
        val toolbar = view.findViewById<MaterialToolbar>(R.id.analytic_toolbar)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_filter -> {
                    viewModel.dispatch(AnalyticAction.ClickFilter)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun initRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.analytic_rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    private fun renderContent(state: AnalyticState) {
        adapter.submitList(state.analytics)
    }

    private fun renderFilter(state: AnalyticState, view: View) {
        val toolbar = view.findViewById<MaterialToolbar>(R.id.analytic_toolbar)
        val actionFilter = toolbar.menu.findItem(R.id.action_filter)

        if (state.isFilterApplied) {
            actionFilter.icon = ContextCompat.getDrawable(requireContext(), R.drawable.debugview_ic_filter_applied)
        } else {
            actionFilter.icon = ContextCompat.getDrawable(requireContext(), R.drawable.debugview_ic_filter_default)
        }
    }

    private fun navigateToEventFragment(tag: String) {
        val bundle = Bundle()
        bundle.putString(EXTRA_TAG, tag)

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.analytic_fragment, EventFragment::class.java, bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
            commit()
        }
    }

    private fun showFilterSheet() {
        val modalBottomSheet = EventFilterFragment()
        modalBottomSheet.show(requireActivity().supportFragmentManager, EventFilterFragment.TAG)
    }

    companion object {
        const val EXTRA_TAG = "EXTRA_TAG"
    }

}
