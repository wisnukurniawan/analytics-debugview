package com.wisnu.kurniawan.debugview

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import org.junit.Assert
import kotlin.time.ExperimentalTime

@ExperimentalTime
suspend fun <T> Flow<T>.expect(expected: Any) {
    test {
        Assert.assertEquals(
            expected,
            awaitItem()
        )
        cancelAndConsumeRemainingEvents()
    }
}
