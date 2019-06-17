package greenter.sunat.validator

import greenter.sunat.validator.parser.ErrorResultParser
import greenter.sunat.validator.process.SaxonXmlValidator
import com.google.gson.Gson

fun main(args: Array<String>) {
    val validator = SaxonXmlValidator(ErrorResultParser())
    val result = validator.validate(args[0], args[1])

    val gson = Gson()

    print(gson.toJson(result))
}