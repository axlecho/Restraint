package com.axlecho.restraint

import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }
}
