package ru.leroymerlin.random.coffee.core.exception

class OAuthServiceException(message: String?, val description: String) : RuntimeException(message) {

    companion object {
        fun createNotSuccess(): OAuthServiceException =
            OAuthServiceException("Can not get success request from OAuth service", "Check application configuration")

        fun createEmptyResponse(): OAuthServiceException =
            OAuthServiceException(
                "Can not get object from response",
                "Check user which you use for access to application"
            )

        fun createNotAvailable(): OAuthServiceException =
            OAuthServiceException("Can not send request to OAuth service", "Check health of OAuth service")
    }
}
