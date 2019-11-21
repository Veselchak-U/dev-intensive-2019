package ru.skillbranch.devintensive.extensions

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

fun String.stripHtml() : String {
    var text : String = ""

    text = this.replace("<.*?>".toRegex(), "") // replace words kind of <tag>
    text = text.replace("&.*?;".toRegex(), "") // replace words kind of &amp;
    text = text.replace("^ +| +\$|( )+".toRegex() , " ") // replace multiple spaces in the middle of the line
    text = text.trim() // replace spaces at the edge of the line

    return text
}