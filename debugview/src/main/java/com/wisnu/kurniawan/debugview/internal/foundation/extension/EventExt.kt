package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.kurniawan.debugview.internal.model.Event

internal fun Event.propertyDisplay() = toJson(properties).prettyJson()
