package com.wisnu.kurniawan.debugview.internal.foundation.extension

import android.content.Context
import android.content.Intent

private const val ACTIVITY_ALIAS_NAME = "com.wisnu.kurniawan.debugview.internal.runtime.DebugViewLauncherActivity"

internal fun Context.getLaunchIntent(): Intent {
    return Intent().apply {
        setClassName(packageName, ACTIVITY_ALIAS_NAME)
    }
}
