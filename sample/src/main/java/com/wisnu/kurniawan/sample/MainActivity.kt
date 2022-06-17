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
            listOf(
                "Analytic1",
                "Analytic2",
            )
        )

        findViewById<Button>(R.id.record).setOnClickListener {
            DebugView.log(
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
            DebugView.log(
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
            DebugView.log(
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
            DebugView.log(
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
        }
    }
}
