package com.wisnu.kurniawan.debugview.internal.runtime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wisnu.kurniawan.debugview.R

class DebugViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        WindowCompat.setDecorFitsSystemWindows(window, false)

        // todo test landscpe
        setContentView(R.layout.activity_debug_view)
    }

}
