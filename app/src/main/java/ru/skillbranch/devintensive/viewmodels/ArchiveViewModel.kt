package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository


class ArchiveViewModel : ViewModel() {

    private val archiveChatRepository = ChatRepository
    private val archiveChatItems =
        Transformations.map(archiveChatRepository.loadChats()) { chatList ->
            chatList.filter { it.isArchived }
                .map { it.toChatItem() }
                .sortedBy { it.id.toInt() }
        }

    fun getChatData(): LiveData<List<ChatItem>> {
        return archiveChatItems
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
}