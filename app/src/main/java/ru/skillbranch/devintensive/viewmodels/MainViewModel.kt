package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.TimeUnits
import ru.skillbranch.devintensive.extensions.add
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository
import java.util.*

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chatItems =
        Transformations.map(chatRepository.loadChats()) { chatList ->
            chatList.filter { !it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
        }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()
        val filterF = {
            val queryStr = query.value!!
            val chats = createArchiveGroupChatItem() + chatItems.value!!

            result.value = if (queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, true) }
        }

        result.addSource(chatItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    private fun createArchiveGroupChatItem(): List<ChatItem> {
        val result = emptyList<ChatItem>().toMutableList()
        val chats = chatRepository.loadChats().value?.filter { it.isArchived }

        if (chats?.isNotEmpty() ?: false) {
            val lastChat = chats!!.maxBy { it.lastMessageDate() ?: Date().add(-9999, TimeUnits.DAY) }
            val lastAuthor = lastChat?.lastMessageAuthor() ?: ""
            val lastText = lastChat?.lastMessageShort()?.first ?: ""
            val lastDate = lastChat?.lastMessageDate()?.shortFormat() ?: ""
            val messageCount = chats!!.sumBy { it.unreadableMessageCount() }
            val chatItem = ChatItem(
                "0",
                null,
                "АЧ",
                "Архив чатов",
                lastText,
                messageCount,
                lastDate,
                false,
                ChatType.ARCHIVE_GROUP,
                lastAuthor
            )
            result.add(chatItem)
        }

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

//    private fun loadChats(): List<ChatItem> = chatRepository.loadChats().map { it.toChatItem() }
    //private fun loadChats(): List<Chat> = chatRepository.loadChats()

}
