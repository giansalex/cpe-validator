package greenter.sunat.validator.parser

import greenter.sunat.validator.model.ErrorResult

interface ResultParser {
    fun getResult(rawText: String): ErrorResult
}