package com.wisnu.kurniawan.debugview.internal.runtime

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.wisnu.kurniawan.debugview.R
import com.wisnu.kurniawan.debugview.internal.foundation.extension.doOnApplyWindowInsets
import com.wisnu.kurniawan.debugview.internal.foundation.extension.toggleLightStatusBars

internal class DebugViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.toggleLightStatusBars(false)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.debugview_activity_debug_view)

        findViewById<View>(R.id.dv_container).doOnApplyWindowInsets { view, windowInsets, initialPadding ->
            val insets = windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                initialPadding.left,
                initialPadding.top + insets.top,
                initialPadding.right,
                initialPadding.bottom + insets.bottom,
            )
        }
    }
}
