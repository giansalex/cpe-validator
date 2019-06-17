package greenter.sunat.validator.parser

import greenter.sunat.validator.model.ErrorLevel
import greenter.sunat.validator.model.ErrorResult

class ErrorResultParser : ResultParser {
    override fun getResult(rawText: String?): ErrorResult? {
        if (rawText == null) {
            return null
        }

        val parts = rawText.split("|")
        val count = parts.count()

        if (count < 3) {
            return null
        }

        val error = ErrorResult()
        error.level = if (parts[0] == "1") ErrorLevel.EXCEPTION else ErrorLevel.OBSERVATION
        error.code = parts[1]
        error.message = parts[2]

        if (count >= 5) {
            error.nodePath = parts[3]
            error.nodeValue = parts[4]
        }

        return error
    }
}