package greenter.sunat.validator.resolver

import greenter.sunat.validator.model.DocumentType
import java.io.InputStream

interface DocumentTypeResolver {
    fun getType(xmlContent: InputStream): DocumentType?
}