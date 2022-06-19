package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.analytic.data.IAnalyticEnvironment
import com.wisnu.kurniawan.debugview.internal.features.analytic.di.AnalyticModule
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventFragment
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        adapter.submitList(it.analytics)
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is AnalyticEffect.NavigateToEvent -> navigateToEventFragment(it.tag, it.isSingle)
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView(view: View) {
        val rv = view.findViewById<RecyclerView>(R.id.analytic_rv)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
    }

    private fun navigateToEventFragment(tag: String, isSingle: Boolean) {
        val bundle = Bundle()
        bundle.putString(EXTRA_TAG, tag)
        bundle.putBoolean(EXTRA_IS_SINGLE, isSingle)

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.analytic_fragment, EventFragment::class.java, bundle)
            setReorderingAllowed(true)
            if (!isSingle) addToBackStack(null)
            commit()
        }
    }

    companion object {
        const val EXTRA_TAG = "EXTRA_TAG"
        const val EXTRA_IS_SINGLE = "EXTRA_IS_SINGLE"
    }

}
