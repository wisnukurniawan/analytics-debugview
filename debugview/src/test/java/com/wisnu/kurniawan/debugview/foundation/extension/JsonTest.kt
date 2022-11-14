package com.wisnu.kurniawan.debugview.foundation.extension

import com.wisnu.foundation.corejson.prettyJson
import com.wisnu.foundation.corejson.toJson
import org.junit.Assert
import org.junit.Test


class JsonTest {

    @Test
    fun prettyJson() {
        val map = mapOf(
            "key1" to "value1",
            "key2" to "value2",
            "key3" to "value3",
            "key4" to "value4",
            "key5" to "",
        )
        val json = toJson(map)

        Assert.assertEquals(
            """
                {
                    "key1": "value1",
                    "key2": "value2",
                    "key3": "value3",
                    "key4": "value4",
                    "key5": ""
                }
            """.trimIndent(),
            json.prettyJson()
        )
    }

}
