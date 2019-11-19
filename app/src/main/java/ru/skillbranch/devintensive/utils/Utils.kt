package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?) : Pair<String?, String?> {
        val parts : List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName == "") firstName = null
        if (lastName == "") lastName = null
//        return Pair(firstName, lastName)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        //TODO implement
        return "not implement"
    }

    fun toInitials(firstName: String?, lastName: String?): String {
        //TODO implement
        return "not implement"
    }
}