package com.axlecho.restraint.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.axlecho.restraint.data.Filter
import com.axlecho.restraint.data.Info
import com.google.gson.Gson

class UsageStatsUtils {

    companion object {
        fun checkPermission(context: Context): Boolean {
            val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val ret = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, TimeUtils.now())
            return ret == null || ret.isEmpty()
        }

        fun getUsageInfo(context: Context, start: Long, end: Long): MutableList<Info> {
            var last = ""
            var lastTime = -1L
            val map = HashMap<String, Info>()
            val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val ret = manager.queryEvents(start, end)
            while (ret.hasNextEvent()) {
                val event = UsageEvents.Event()
                ret.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    if (last != "${event.className}@${event.packageName}") {
                        last = "${event.className}@${event.packageName}"
                        lastTime = event.timeStamp
                    }
                }

                if (event.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    if (last != "${event.className}@${event.packageName}") {
                        continue
                    }

                    val time = event.timeStamp - lastTime
                    val info = map[event.packageName] ?: Info(event.packageName)
                    info.count++
                    info.time += time
                    map[info.name] = info
                }
            }
            return map.values.toMutableList()
        }

        fun loadFilter(context: Context): List<Filter> {
            val spf = context.getSharedPreferences("filter", Context.MODE_PRIVATE)
            val filterMap = spf.all

            val ret = arrayListOf<Filter>()
            for (type in filterMap.keys) {
                val filter = Gson().fromJson<Filter>(filterMap[type] as String, Filter::class.java)
                ret.add(filter)
            }
            return ret
        }

        fun saveFilter(context: Context, info: Info, type: String) {
            val spf = context.getSharedPreferences("filter", Context.MODE_PRIVATE)
            val ret = spf.getString(type, null)
            info.type = type
            val filter: Filter
            if (ret == null) {
                filter = Filter(type)
                filter.app.add(info)
            } else {
                filter = Gson().fromJson(ret, Filter::class.java)
                filter.app.add(info)
            }

            spf.edit().putString(type, Gson().toJson(filter)).apply()
        }

        fun applyFilter(filters: List<Filter>, list: MutableList<Info>) {
            for (filter in filters) {
                for(app in filter.app) {
                    for(info in list) {
                        if(info.name == app.name) {
                            info.type = app.type
                        }
                    }
                }
            }

            val it = list.iterator()
            while(it.hasNext()) {
                val info = it.next()
                if(info.type == Filter.IGNORE) {
                    it.remove()
                }
            }
        }

        fun applyAllFilter(context: Context,list:MutableList<Info>) {
            applyFilter(loadFilter(context),list)
        }
    }
}