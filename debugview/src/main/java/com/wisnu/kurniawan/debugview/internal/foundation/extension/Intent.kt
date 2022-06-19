package com.wisnu.kurniawan.debugview.internal.foundation.extension

import android.content.Context
import android.content.Intent

private const val DEBUG_VIEW_ACTION = "DEBUG_VIEW_PAGE"

internal fun Context.getLaunchIntent(): Intent {
    return Intent(DEBUG_VIEW_ACTION).apply {
        `package` = packageName
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
}
