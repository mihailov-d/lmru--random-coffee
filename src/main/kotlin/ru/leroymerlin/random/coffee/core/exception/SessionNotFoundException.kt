package ru.leroymerlin.random.coffee.core.exception

import ru.leroymerlin.random.coffee.core.util.TgChatId

class SessionNotFoundException(tgChatId: TgChatId) : RuntimeException("Can't find session by chatId: $tgChatId")
