package ru.leroymerlin.random.coffee.core.tg.ability

enum class CommandList(val command: String) {
    ACQUAINTANCE_INPUT_EMAIL("Почта"),
    ACQUAINTANCE_INPUT_PHONE("Телефон"),
    ACQUAINTANCE_INPUT_TELEGRAM("Телеграм"),
    ACQUAINTANCE_INPUT_NAME("Ввести имя"),
    ACQUAINTANCE_INPUT_SURNAME("Ввести фамилию"),
    ACQUAINTANCE_INPUT_ABOUT_ME("Ввести информацию о себе"),
    ACQUAINTANCE_INPUT_ABOUT_WORK("Ввести информацию о работе"),

    MEETING_CREATE("Создать встречу"),
    MEETING_ABOUT_WORK("О работе"),
    MEETING_ABOUT_SOMETHING("Давай отдахнем, не о работе"),
    MEETING_DATE_TODAY("Сегодня"),
    MEETING_DATE_TOMORROW("Завтра"),
    MEETING_DATE_AFTER_TOMORROW("Послезавтра"),

    MATCH_MEETING_START("Опубликовать анкету для встречи");

    init {
        if (values().toSet().size != values().size) {
            throw IllegalStateException("Not uniq commands. Check 'CommandList'")
        }
    }
}
