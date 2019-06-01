package greenter.sunat.validator.process

import greenter.sunat.validator.model.ErrorResult

interface XmlValidator {
    fun validate(xml: String, xsltPath: String): ErrorResult
}