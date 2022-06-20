package com.wisnu.kurniawan.sample

import android.app.Application
import com.wisnu.kurniawan.debugview.DebugView

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugView.init("Google analytic")
    }

}
