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
        DebugView.init(
            this.applicationContext,
            "Analytic2",
        )

        findViewById<Button>(R.id.record).setOnClickListener {
            DebugView.record(
                Event(
                    "Analytic2",
                    "Event1",
                    mapOf(
                        "Prop1" to "Value1",
                        "Prop2" to "Value2",
                        "Prop3" to "Value3",
                        "Prop4" to "Value4",
                    )
                )
            )
            DebugView.record(
                Event(
                    "Analytic2",
                    "Event2",
                    mapOf(
                        "Prop1" to "Value1",
                        "Prop2" to "Value2",
                        "Prop3" to "Value3",
                        "Prop4" to "Value4",
                    )
                )
            )
            DebugView.record(
                Event(
                    "Analytic2",
                    "Event3",
                    mapOf(
                        "Prop1" to "Value1",
                        "Prop2" to "Value2",
                        "Prop3" to "Value3",
                        "Prop4" to "Value4",
                    )
                )
            )
            DebugView.record(
                Event(
                    "Analytic2",
                    "Event4",
                    mapOf(
                        "Prop1" to "Value1",
                        "Prop2" to "Value2",
                        "Prop3" to "Value3",
                        "Prop4" to "Value4",
                    )
                )
            )
        }
    }
}
