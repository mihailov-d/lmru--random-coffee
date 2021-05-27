package ru.leroymerlin.random.coffee.core.exception

abstract class AbstractBaseException : Exception {
    val description: String

    constructor(description: String) : super() {
        this.description = description
    }

    constructor(message: String?, description: String) : super(message) {
        this.description = description
    }

    constructor(message: String?, cause: Throwable?, description: String) : super(message, cause) {
        this.description = description
    }

    constructor(cause: Throwable?, description: String) : super(cause) {
        this.description = description
    }

    constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
        description: String
    ) : super(message, cause, enableSuppression, writableStackTrace) {
        this.description = description
    }
}
