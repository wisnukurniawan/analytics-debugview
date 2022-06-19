package com.wisnu.kurniawan.debugview.internal.foundation.extension

import android.graphics.Color
import android.os.Build
import android.view.Window
import androidx.core.view.WindowCompat

private val translucentBarColor = Color.parseColor("#80000000")

internal fun Window.toggleLightStatusBars(light: Boolean? = null) {
    WindowCompat.getInsetsController(this, decorView)?.let {
        val l = light ?: !it.isAppearanceLightStatusBars
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            it.isAppearanceLightStatusBars = false
            statusBarColor = if (l) translucentBarColor else Color.TRANSPARENT
        } else
            it.isAppearanceLightStatusBars = l
    }
}
