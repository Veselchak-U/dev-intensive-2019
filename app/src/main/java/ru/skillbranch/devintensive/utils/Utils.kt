package ru.skillbranch.devintensive.utils

object Utils {

    fun parseFullName(fullName: String?) : Pair<String?, String?> {
        //TODO FIX ME NULL
        val parts : List<String>? = fullName?.split(" ")

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

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