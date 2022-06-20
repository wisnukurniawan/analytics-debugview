package com.wisnu.kurniawan.sample

class AnalyticClient(
    private val googleAnalytic: AnalyticApi,
    private val mixPanelAnalytic: AnalyticApi
) : AnalyticApi {
    override fun track(eventName: String, properties: Map<String, String>) {
        googleAnalytic.track(eventName, properties)
        mixPanelAnalytic.track(eventName, properties)
    }
}
