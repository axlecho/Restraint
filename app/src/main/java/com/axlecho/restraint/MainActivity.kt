package com.axlecho.restraint

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axlecho.restraint.ui.ListViewAdapter
import com.axlecho.restraint.utils.TimeUtils
import com.axlecho.restraint.utils.UsageStatsUtils


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (UsageStatsUtils.checkPermission(this)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        if (UsageStatsUtils.checkPermission(this)) {
            findViewById<View>(R.id.left).visibility = View.GONE
            findViewById<View>(R.id.error_tip).visibility = View.VISIBLE
            findViewById<View>(R.id.error_tip).setOnClickListener {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivity(intent)
            }
        } else {
            val view = findViewById<RecyclerView>(R.id.list)
            view.layoutManager = LinearLayoutManager(this)
            view.adapter =
                ListViewAdapter(this, UsageStatsUtils.getUsageInfo(this, TimeUtils.dayZero(), TimeUtils.now()))
        }
    }
}
