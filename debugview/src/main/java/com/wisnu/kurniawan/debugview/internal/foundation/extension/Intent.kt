package com.wisnu.kurniawan.debugview.internal.foundation.extension

import android.content.Context
import android.content.Intent
import com.wisnu.kurniawan.debugview.internal.runtime.DebugViewActivity

internal fun Context.getLaunchIntent(): Intent {
    return Intent(this, DebugViewActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}
