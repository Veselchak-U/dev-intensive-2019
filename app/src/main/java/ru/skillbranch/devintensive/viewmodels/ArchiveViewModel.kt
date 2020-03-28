package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository


class ArchiveViewModel : ViewModel() {

    private val query = mutableLiveData("")
    private val archiveChatRepository = ChatRepository
    private val archiveChatItems =
        Transformations.map(archiveChatRepository.loadChats()) { chatList ->
            chatList.filter { it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
        }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()
        val filterF = {
            val queryStr = query.value!!
            val chats = archiveChatItems.value!!

            result.value = if (queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, true) }
        }

        result.addSource(archiveChatItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun restoreFromArchive(chatId: String) {
        val chat = archiveChatRepository.find(chatId)
        chat ?: return
        archiveChatRepository.update(chat.copy(isArchived = false))
    }

    fun addToArchive(chatId: String) {
        val chat = archiveChatRepository.find(chatId)
        chat ?: return
        archiveChatRepository.update(chat.copy(isArchived = true))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}