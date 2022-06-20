package com.wisnu.kurniawan.sample

import com.wisnu.kurniawan.debugview.DebugView
import com.wisnu.kurniawan.debugview.Event

class MixPanelAnalytic : AnalyticApi {
    override fun track(eventName: String, properties: Map<String, String>) {
        DebugView.record(Event(TAG, eventName, properties))
        // Track mixpanel analytic...
    }

    companion object {
        const val TAG = "Mixpanel"
    }
}
