package ru.leroymerlin.random.coffee.core.util

import org.telegram.abilitybots.api.util.AbilityUtils.getChatId
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


fun keyboardRow(keyboardButton: KeyboardButton): KeyboardRow {
    val keyboardRow = KeyboardRow()
    keyboardRow.add(keyboardButton)
    return keyboardRow
}

fun Update.chatId(): Long = getChatId(this)
fun Update.stringChatId(): String = getChatId(this).toString()


