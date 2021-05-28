package ru.leroymerlin.random.coffee.core.util

import org.telegram.telegrambots.meta.api.objects.Update
import java.util.function.Predicate

fun textEquals(text: String): Predicate<Update> =
        Predicate { update: Update -> update.hasMessage() && update.message.hasText() && text == update.message.text }
