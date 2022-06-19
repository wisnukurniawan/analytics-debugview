package com.wisnu.kurniawan.debugview.internal.features.analytic.ui

internal sealed interface AnalyticAction {
    data class Launch(val tag: String) : AnalyticAction
    data class ClickAnalyticItem(val tag: String) : AnalyticAction
}
