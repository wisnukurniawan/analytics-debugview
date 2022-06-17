package com.wisnu.kurniawan.debugview.internal.foundation.wrapper

import java.util.*

internal interface IdProvider {
    fun generate(): String
}

internal class IdProviderImpl : IdProvider {
    override fun generate(): String {
        return UUID.randomUUID().toString()
    }
}
