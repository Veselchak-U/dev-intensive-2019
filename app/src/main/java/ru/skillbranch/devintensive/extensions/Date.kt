package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value:Int) : String = "$value " + declinationOfTime(value.toLong(), this)

}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.add(value:Int, units: TimeUnits = TimeUnits.SECOND): Date {
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

fun Date.humanizeDiff(date: Date = Date()): String {
    var diffTime : Long = date.time - this.time
    val isPastTime : Boolean = diffTime > 0
    var text = ""

    diffTime = abs(diffTime)
    text = when (diffTime) {
        in  0*SECOND..1*SECOND -> "только что"
        in  1*SECOND..45*SECOND -> if (isPastTime) "несколько секунд назад"
                                   else "через несколько секунд"
        in 45*SECOND..75*SECOND -> if (isPastTime) "минуту назад"
                                   else "через минуту"
        in 75*SECOND..45*MINUTE -> if (isPastTime) "${diffTime / MINUTE} " + declinationOfTime(diffTime / MINUTE, TimeUnits.MINUTE) + " назад"
                                   else "через " + "${diffTime / MINUTE} " + declinationOfTime(diffTime / MINUTE, TimeUnits.MINUTE)
        in 45*MINUTE..75*MINUTE -> if (isPastTime) "час назад"
                                   else "через час"
        in 75*MINUTE..22* HOUR -> if (isPastTime) "${diffTime / HOUR} " + declinationOfTime(diffTime / HOUR, TimeUnits.HOUR) + " назад"
                                  else "через " + "${diffTime / HOUR} " + declinationOfTime(diffTime / HOUR, TimeUnits.HOUR)
        in 22*HOUR..26*HOUR -> if (isPastTime) "день назад"
                               else "через день"
        in 26*HOUR..360*DAY -> if (isPastTime) "${diffTime / DAY} " + declinationOfTime(diffTime / DAY, TimeUnits.DAY) + " назад"
                               else "через " + "${diffTime / DAY} " + declinationOfTime(diffTime / DAY, TimeUnits.DAY)
        else -> if (isPastTime) "более года назад"
                else "более чем через год"
    }
    return text
}

fun declinationOfTime(value: Long, unit: TimeUnits): String {
    val absValue = abs(value)
    var text = ""

    if (absValue in 11..19)
        text = when (unit) {
            TimeUnits.SECOND -> "секунд"
            TimeUnits.MINUTE -> "минут"
            TimeUnits.HOUR -> "часов"
            TimeUnits.DAY -> "дней"
        }
    else if (absValue % 10 == 1L)
        text = when (unit) {
            TimeUnits.SECOND -> "секунду"
            TimeUnits.MINUTE -> "минуту"
            TimeUnits.HOUR -> "час"
            TimeUnits.DAY -> "день"
        }
    else if (absValue % 10 in 2..4)
        text = when (unit) {
            TimeUnits.SECOND -> "секунды"
            TimeUnits.MINUTE -> "минуты"
            TimeUnits.HOUR -> "часа"
            TimeUnits.DAY -> "дня"
        }
    else text = when (unit) {
            TimeUnits.SECOND -> "секунд"
            TimeUnits.MINUTE -> "минут"
            TimeUnits.HOUR -> "часов"
            TimeUnits.DAY -> "дней"
        }
    return text
}
