package ru.leroymerlin.random.coffee.core.util.message

import ru.leroymerlin.random.coffee.core.dto.UserPreferCommunicationEnum
import ru.leroymerlin.random.coffee.core.dto.request.TopicTypeEnum
import ru.leroymerlin.random.coffee.core.model.User

object MessageUtil {
    fun meetingTopicMessageString(topicTypeEnum: TopicTypeEnum): String = when (topicTypeEnum) {
        TopicTypeEnum.ABOUT_OTHER -> "Не о работе"
        TopicTypeEnum.ABOUT_WORK -> "О работе"
    }

    fun communicationChannelString(user: User): String {
        if (user.preferCommunications == null || user.preferCommunications.isEmpty()) {
            return "Нет способов связи"
        }
        return when (user.preferCommunications.first()) {
            UserPreferCommunicationEnum.TELEGRAM -> "@${user.telegramUsername}"
            UserPreferCommunicationEnum.PHONE -> user.phone ?: "нет телофона"
            UserPreferCommunicationEnum.EMAIL -> user.email ?: "нет email"
        }
    }
}
