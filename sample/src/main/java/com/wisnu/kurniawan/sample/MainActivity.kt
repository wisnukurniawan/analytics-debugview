package com.wisnu.kurniawan.sample

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val analyticClient by lazy {
        AnalyticClient(
            GoogleAnalytic(),
            MixPanelAnalytic()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.record).setOnClickListener {
            analyticClient.track(
                "click_button_event_1",
                mapOf(
                    "prop_1" to "value_1",
                    "prop_2" to "value_2",
                    "prop_3" to "value_3",
                    "prop_4" to "value_4",
                )
            )
            analyticClient.track(
                "click_button_event_2",
                mapOf(
                    "prop_1" to "value_1",
                    "prop_2" to "value_2",
                    "prop_3" to "value_3",
                    "prop_4" to "value_4",
                )
            )
            analyticClient.track(
                "click_button_event_3",
                mapOf(
                    "prop_1" to "value_1",
                    "prop_2" to "value_2",
                    "prop_3" to "value_3",
                    "prop_4" to "value_4",
                )
            )
        }
    }
}
