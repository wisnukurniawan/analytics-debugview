package com.wisnu.kurniawan.debugview.internal.features.eventfilter.ui

import android.content.ClipboardManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.features.event.ui.EventFragment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.data.IEventFilterEnvironment
import com.wisnu.kurniawan.debugview.internal.features.eventfilter.di.EventFilterModule
import com.wisnu.kurniawan.debugview.internal.foundation.di.DataModule
import com.wisnu.kurniawan.debugview.internal.foundation.extension.stringResource
import com.wisnu.kurniawan.debugview.internal.model.FilterType
import kotlinx.coroutines.launch


internal class EventFilterFragment : BottomSheetDialogFragment() {

    lateinit var environment: IEventFilterEnvironment
    lateinit var viewModel: EventFilterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventFilterModule.inject(this, DataModule.localManager)
        EventFilterModule.inject(this, this, environment)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DebugViewThemeOverlay_App_BottomSheetDialog)

        viewModel.dispatch(EventFilterAction.Launch)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.debugview_fragment_event_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        renderChips(it, view)
                        renderPasteArea(it, view)
                    }
                }
                launch {
                    viewModel.effect.collect {
                        when (it) {
                            is EventFilterEffect.Dismiss -> dismissSheet()
                        }
                    }
                }
            }
        }

        initChipGroup(view)
        initPasteArea(view)
        initApplyButton(view)
        initResetButton(view)
    }

    private fun initChipGroup(view: View) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.filter_type_chip)
        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val types = checkedIds.map {
                chipGroup.findViewById<Chip>(it).tag as FilterType
            }
            viewModel.dispatch(EventFilterAction.SeparateChanges(types))
        }
    }

    private fun initPasteArea(view: View) {
        val pasteTv = view.findViewById<AppCompatTextView>(R.id.filter_title_text)
        pasteTv.movementMethod = ScrollingMovementMethod()
        pasteTv.setOnClickListener {
            viewModel.dispatch(EventFilterAction.ClickPaste(getClipboardData()))
        }
    }

    private fun initApplyButton(view: View) {
        view.findViewById<MaterialButton>(R.id.event_apply_filter_button).setOnClickListener {
            viewModel.dispatch(EventFilterAction.ClickApply)
        }
    }

    private fun initResetButton(view: View) {
        view.findViewById<MaterialButton>(R.id.event_reset_filter_button).setOnClickListener {
            viewModel.dispatch(EventFilterAction.ClickReset)
        }
    }

    private fun renderChips(state: EventFilterState, view: View) {
        val chipGroup = view.findViewById<ChipGroup>(R.id.filter_type_chip)
        chipGroup.removeAllViews()
        state.filterItems.forEachIndexed { index, filterItem ->
            chipGroup.addView(
                Chip(requireContext()).apply {
                    text = getString(filterItem.filterType.stringResource())
                    isChecked = filterItem.selected
                    isCheckable = !filterItem.selected
                    isClickable = !filterItem.selected
                    setChipDrawable(
                        ChipDrawable.createFromAttributes(
                            chipGroup.context,
                            null,
                            0,
                            R.style.DebugViewWidget_App_Chip
                        )
                    )
                    tag = filterItem.filterType
                    id = index
                }
            )
        }
    }

    private fun renderPasteArea(state: EventFilterState, view: View) {
        val pasteTv = view.findViewById<AppCompatTextView>(R.id.filter_title_text)
        val pasteIc = view.findViewById<View>(R.id.filter_title_text_hint)
        val pasteHint = view.findViewById<View>(R.id.filter_paste_ic)

        if (state.text.isNotBlank()) {
            pasteIc.visibility = View.GONE
            pasteHint.visibility = View.GONE
            pasteTv.text = state.textDisplay
        } else {
            pasteIc.visibility = View.VISIBLE
            pasteHint.visibility = View.VISIBLE
            pasteTv.text = ""
        }
    }

    private fun getClipboardData(): String {
        val clipBoard = getSystemService(requireContext(), ClipboardManager::class.java)
        val clipData = clipBoard?.primaryClip
        val item = clipData?.getItemAt(0)
        return item?.text?.toString() ?: ""
    }

    private fun dismissSheet() {
        activity?.supportFragmentManager?.setFragmentResult(EventFragment.RC_APPLY_FILTER, Bundle())
        dismiss()
    }

    companion object {
        const val TAG = "EventFilterFragment"
    }

}
