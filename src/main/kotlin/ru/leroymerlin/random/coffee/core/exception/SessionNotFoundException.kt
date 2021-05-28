package ru.leroymerlin.random.coffee.core.exception

import ru.leroymerlin.random.coffee.core.util.ChatId

class SessionNotFoundException(chatId: ChatId) : RuntimeException("Can't find session by chatId: $chatId")
