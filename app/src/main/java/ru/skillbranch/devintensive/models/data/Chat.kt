package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        var filter = messages.filter { !it.isReaded }
        return filter.size
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        var lastMessage = messages.lastOrNull()
        return lastMessage?.date
    }

    fun lastMessageAuthor(): String? {
        var lastMessage = messages.lastOrNull()
        var result = lastMessage?.from?.firstName ?: "" + " " + lastMessage?.from?.lastName ?: ""
//        return "${lastMessage?.from?.firstName ?: ""} ${lastMessage?.from?.lastName}"
        return result.trim()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> = when (val lastMessage = messages.lastOrNull()) {
        is TextMessage -> "${lastMessage.text}" to "@${lastMessage.from.firstName}" // ${lastMessage.from.lastName}
        is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to "@${lastMessage.from.firstName}" // ${lastMessage.from.lastName}
        else -> "Сообщений ещё нет" to "@John_Doe"
    }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
        } else {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate()?.shortFormat(),
                false,
                ChatType.GROUP,
                lastMessageShort().second
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE/*,
    ARCHIVE_GROUP*/
}



