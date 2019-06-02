package greenter.sunat.validator

import greenter.sunat.validator.process.SaxonXmlValidator

fun main(args: Array<String>) {
    val validator = SaxonXmlValidator()
    validator.validate(args[0], args[1])
}