package com.wisnu.kurniawan.debugview.internal.foundation.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

internal class VisibilityTracker(
    private val listener: (Boolean) -> Unit
) : Application.ActivityLifecycleCallbacks {

    private var startedActivityCount = 0

    private var hasVisibleActivities: Boolean = false

    private var lastUpdate: Boolean = false

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        startedActivityCount++
        if (!hasVisibleActivities && startedActivityCount == 1) {
            hasVisibleActivities = true
            updateVisible()
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        if (startedActivityCount > 0) {
            startedActivityCount--
        }
        if (hasVisibleActivities && startedActivityCount == 0 && !activity.isChangingConfigurations) {
            hasVisibleActivities = false
            updateVisible()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    private fun updateVisible() {
        val visible = hasVisibleActivities
        if (hasVisibleActivities != lastUpdate) {
            lastUpdate = visible
            listener.invoke(visible)
        }
    }
}

internal fun Application.registerVisibilityListener(listener: (Boolean) -> Unit) {
    val visibilityTracker = VisibilityTracker(listener)
    registerActivityLifecycleCallbacks(visibilityTracker)
}
