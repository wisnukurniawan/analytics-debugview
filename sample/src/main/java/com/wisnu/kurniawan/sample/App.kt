package com.wisnu.kurniawan.sample

import android.app.Application
import com.wisnu.kurniawan.debugview.DebugView

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugView.register(ANALYTIC_GA_TAG, ANALYTIC_MIXPANEL_TAG)
    }

    companion object {
        const val ANALYTIC_GA_TAG = "Google analytic"
        const val ANALYTIC_MIXPANEL_TAG = "Mixpanel"
    }

}
