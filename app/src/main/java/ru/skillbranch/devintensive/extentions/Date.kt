package ru.skillbranch.devintensive.extentions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy") : String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND) : Date {
    var time = this.time

    time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diffTime : Long = date.time - this.time
    var text = ""

    text = when(diffTime) {
        in  0*SECOND..1*SECOND  -> "только что"
        in  1*SECOND..45*SECOND -> "несколько секунд назад"
        in 45*SECOND..75*SECOND -> "минуту назад"
        in 75*SECOND..45*MINUTE -> "${diffTime / MINUTE} " + timeDeclination(diffTime, TimeUnits.MINUTE) + " назад"
        in 45*MINUTE..75*MINUTE -> "час назад"
        in 75*MINUTE..22* HOUR -> "${diffTime / HOUR} " + timeDeclination(diffTime, TimeUnits.HOUR) + " назад"
        in 22*HOUR..26*HOUR -> "день назад"
        in 26*HOUR..360*DAY -> "${diffTime / DAY} " + timeDeclination(diffTime, TimeUnits.DAY) + " назад"
        else -> "более года назад"
    }
    return text
/*
    0с - 1с "только что"
    1с - 45с "несколько секунд назад"
    45с - 75с "минуту назад"
    75с - 45мин "N минут назад"
    45мин - 75мин "час назад"
    75мин 22ч "N часов назад"
    22ч - 26ч "день назад"
    26ч - 360д "N дней назад"
    >360д "более года назад"
*/
}

fun timeDeclination(milliSeconds: Long, unit: TimeUnits): String {
    var text = ""

    if (unit == TimeUnits.MINUTE) {
        val minutes : Long = milliSeconds / MINUTE

        if (minutes in 11..19)
            text = "минут"
        else if (minutes % 10 == 0L)
            text = "минут"
        else if (minutes % 10 == 1L)
            text = "минуту"
        else if(minutes % 10 in 2..4)
            text = "минуты"
        else text = "минут"

    } else if (unit == TimeUnits.HOUR) {
        val hours : Long = milliSeconds / HOUR

        if (hours in 11..19)
            text = "часов"
        else if (hours % 10 == 0L)
            text = "часов"
        else if (hours % 10 == 1L)
            text = "час"
        else if(hours % 10 in 2..4)
            text = "часа"
        else text = "часов"

    } else if (unit == TimeUnits.DAY) {
        val days : Long = milliSeconds / DAY

        if (days in 11..19)
            text = "дней"
        else if (days % 10 == 0L)
            text = "дней"
        else if (days % 10 == 1L)
            text = "день"
        else if(days % 10 in 2..4)
            text = "дня"
        else text = "дней"
    }
    return text
}
