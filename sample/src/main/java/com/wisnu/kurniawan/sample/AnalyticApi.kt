package com.wisnu.kurniawan.sample

interface AnalyticApi {
    fun track(eventName: String, properties: Map<String, String>)
}
