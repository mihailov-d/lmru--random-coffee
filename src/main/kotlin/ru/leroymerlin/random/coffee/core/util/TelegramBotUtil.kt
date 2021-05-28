package ru.leroymerlin.random.coffee.core.util

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow


fun keyboardRow(keyboardButton: KeyboardButton): KeyboardRow {
    val keyboardRow = KeyboardRow()
    keyboardRow.add(keyboardButton)
    return keyboardRow
}
