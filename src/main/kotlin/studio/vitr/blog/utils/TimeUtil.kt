package studio.vitr.blog.utils

import java.time.Instant
import java.util.Date

class TimeUtil {

    companion object {
        fun now(): Long = Instant.now().toEpochMilli()
        fun add(t: Long, duration: Long): Long = t + duration
        fun getExpirationDate(t: Long, duration: Long) = Date(add(t, duration))
    }
}