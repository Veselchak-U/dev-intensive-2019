package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = Date(),
    val isOnline: Boolean = false

) {
    fun toUserItem(): UserItem {
        val lastActivity = when {
            lastVisit == null -> "Ещё ни разу не заходил"
            isOnline -> "В сети"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }

        return UserItem(
            id,
            "${firstName.orEmpty()} ${lastName.orEmpty()}",
            Utils.toInitials(firstName, lastName),
            avatar,
            lastActivity,
            false,
            isOnline
        )
    }

    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id: String) : this(id, firstName = "John", lastName = "Doe")

    constructor(builder: Builder) : this(
        id = builder.id,
        firstName = builder.firstName,
        lastName = builder.lastName,
        avatar = builder.avatar,
        rating = builder.rating,
        respect = builder.respect,
        lastVisit = builder.lastVisit,
        isOnline = builder.isOnline
    )

    class Builder {
        var id: String = ""
        var firstName: String? = null
        var lastName: String? = null
        var avatar: String? = null
        var rating: Int = 0
        var respect: Int = 0
        var lastVisit: Date? = Date()
        var isOnline: Boolean = false

        fun id(s: String): Builder {
            this.id = s
            return this
        }

        fun firstName(s: String): Builder {
            this.firstName = s
            return this
        }

        fun lastName(s: String): Builder {
            this.lastName = s
            return this
        }

        fun avatar(s: String): Builder {
            this.avatar = s
            return this
        }

        fun rating(n: Int): Builder {
            this.rating = n
            return this
        }

        fun respect(n: Int): Builder {
            this.respect = n
            return this
        }

        fun lastVisit(d: Date): Builder {
            this.lastVisit = d
            return this
        }

        fun isOnline(b: Boolean): Builder {
            this.isOnline = b
            return this
        }

        fun build(): User {
            return User(this)
        }
    }

    companion object Factory {
        private var lastId: Int = -1

        fun makeUser(fullName: String?): User {
            lastId++

            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(
                id = "$lastId",
                firstName = firstName,
                lastName = lastName
            )
        }
    }

}

