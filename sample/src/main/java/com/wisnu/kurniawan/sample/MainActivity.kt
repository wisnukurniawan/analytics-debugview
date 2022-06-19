package com.wisnu.kurniawan.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wisnu.kurniawan.debugview.DebugView
import com.wisnu.kurniawan.debugview.Event

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.record).setOnClickListener {
            DebugView.record(
                Event(
                    "Google analytic",
                    "click_button_event_1",
                    mapOf(
                        "prop_1" to "value_1",
                        "prop_2" to "value_2",
                        "prop_3" to "value_3",
                        "prop_4" to "value_4",
                    )
                )
            )
            DebugView.record(
                Event(
                    "Google analytic",
                    "click_button_event_2",
                    mapOf(
                        "prop_1" to "value_1",
                        "prop_2" to "value_2",
                        "prop_3" to "value_3",
                        "prop_4" to "value_4",
                    )
                )
            )
            DebugView.record(
                Event(
                    "Google analytic",
                    "click_button_event_3",
                    mapOf(
                        "prop_1" to "value_1",
                        "prop_2" to "value_2",
                        "prop_3" to "value_3",
                        "prop_4" to "value_4",
                    )
                )
            )
            DebugView.record(
                Event(
                    "Google analytic",
                    "click_button_event_4",
                    mapOf(
                        "prop_1" to "value_1",
                        "prop_2" to "value_2",
                        "prop_3" to "value_3",
                        "prop_4" to "value_4",
                    )
                )
            )
        }
    }
}
