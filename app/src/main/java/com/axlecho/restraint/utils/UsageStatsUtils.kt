package com.axlecho.restraint.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.axlecho.restraint.Info

class UsageStatsUtils{

    companion object {
        fun checkpermission(context: Context) :Boolean {
            val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val ret = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, TimeUtils.now())
            return ret == null || ret.isEmpty()
        }

        fun getUsageInfo(context:Context,start:Long,end:Long):List<Info> {
            var last = ""
            var lastTime = -1L
            val map = HashMap<String,Info>()
            val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val ret = manager.queryEvents(start,end)
            while(ret.hasNextEvent()) {
                val event = UsageEvents.Event()
                ret.getNextEvent(event)
                if(event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    if(last != "${event.className}@${event.packageName}") {
                        last = "${event.className}@${event.packageName}"
                        lastTime = event.timeStamp
                    }
                }

                if(event.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    if(last != "${event.className}@${event.packageName}") {
                        continue
                    }

                    val time = event.timeStamp - lastTime
                    val info = map[event.packageName]?: Info(event.packageName)
                    info.count ++
                    info.time += time
                    map[info.name] = info
                }
            }

            return map.values.toList().sortedByDescending { it.time }
        }
    }
}