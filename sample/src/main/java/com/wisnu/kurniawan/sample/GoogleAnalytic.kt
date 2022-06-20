package com.wisnu.kurniawan.sample

import com.wisnu.kurniawan.debugview.DebugView
import com.wisnu.kurniawan.debugview.Event

class GoogleAnalytic : AnalyticApi {
    override fun track(eventName: String, properties: Map<String, String>) {
        DebugView.record(Event(TAG, eventName, properties))
        // Track google analytic...
    }

    companion object {
        const val TAG = "Google analytic"
    }
}
