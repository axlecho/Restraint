package com.axlecho.restraint.utils

import java.util.*

class TimeUtils {

    companion object {
        fun now(): Long {
            val calendar = Calendar.getInstance(Locale.CHINA)
            return calendar.timeInMillis
        }

        fun dayZero(): Long {
            val calendar = Calendar.getInstance(Locale.CHINA)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }

        fun readableTime(time: Long): String {
            return when {
                time > 1000 * 60 * 60 -> String.format("%.2f",time / 1000.0 / (60 * 60)) +  " h"
                time > 1000 * 60 -> String.format("%.2f",time / 1000.0f / 60) + " min"
                time > 1000 -> String.format("%.2f",time / 1000.0f) + " second"
                else -> "$time"
            }
        }
    }
}