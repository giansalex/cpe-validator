package greenter.sunat.validator.resolver

import greenter.sunat.validator.model.DocumentType

interface PathResolver {
    fun getPath(type: DocumentType): String?
}