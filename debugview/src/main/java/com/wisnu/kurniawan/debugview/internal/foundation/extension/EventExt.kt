package com.wisnu.kurniawan.debugview.internal.foundation.extension

import com.wisnu.foundation.corejson.prettyJson
import com.wisnu.kurniawan.debugview.internal.model.Event

internal fun Event.propertyDisplay() = com.wisnu.foundation.corejson.toJson(properties).prettyJson()
