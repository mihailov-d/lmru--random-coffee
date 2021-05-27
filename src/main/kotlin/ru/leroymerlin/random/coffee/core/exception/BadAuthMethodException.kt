package ru.leroymerlin.random.coffee.core.exception

class BadAuthMethodException(message: String?, description: String) : AbstractBaseException(message, description) {

    companion object {
        fun create(): BadAuthMethodException =
            BadAuthMethodException("Uses not supported authorization method", "Uses correct authorization method")
    }
}
