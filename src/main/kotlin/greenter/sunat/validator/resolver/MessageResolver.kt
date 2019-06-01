package greenter.sunat.validator.resolver

interface MessageResolver {
    fun getMessage(code: String): String?
}