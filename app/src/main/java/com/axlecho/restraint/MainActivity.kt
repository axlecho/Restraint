package com.axlecho.restraint

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.axlecho.restraint.utils.TimeUtils
import com.axlecho.restraint.utils.UsageStatsUtils
import com.google.gson.GsonBuilder
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        if(!UsageStatsUtils.checkpermission(this)){
            startActivity(intent)
            Logger.addLogAdapter(AndroidLogAdapter())
        }

        findViewById<Button>(R.id.test).setOnClickListener {
            Logger.json(GsonBuilder().setPrettyPrinting().create().toJson(
                UsageStatsUtils.getUsageInfo(this,TimeUtils.dayZero(),TimeUtils.now())))
        }
    }
}
