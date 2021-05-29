package ru.leroymerlin.random.coffee.core.service

import ru.leroymerlin.random.coffee.core.model.Meeting

interface RandomService {

    fun random(meeting: Meeting)
}