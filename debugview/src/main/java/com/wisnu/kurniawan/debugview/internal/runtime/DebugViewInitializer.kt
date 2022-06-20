package com.wisnu.kurniawan.debugview.internal.runtime

import android.content.Context
import androidx.startup.Initializer
import com.wisnu.kurniawan.debugview.DebugView

internal class DebugViewInitializer : Initializer<DebugView> {
    override fun create(context: Context): DebugView {
        DebugView.init(context)
        return DebugView
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
