package ru.skillbranch.devintensive.extentions

fun String.truncate(value: Int = 16) : String {
    val filler : String = "..."
    var text : String = ""

    text = this.trim()
    if (text.length <= value)
        return text
    else {
        text = text.substring(0, value)
        text = text.trimEnd() + filler
        return text
    }
}