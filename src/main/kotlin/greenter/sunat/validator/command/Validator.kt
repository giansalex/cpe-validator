package greenter.sunat.validator.command

import com.google.gson.Gson
import greenter.sunat.validator.parser.ErrorResultParser
import greenter.sunat.validator.process.SaxonXmlValidator
import picocli.CommandLine.*

class Validator : Runnable {

    @Option(names = ["-x", "--xsl-file"], required = true, description = ["Path XSL Sunat Validator"])
    private var xslFile: String? = null

    @Parameters(index = "0", description = ["XML File to validate"])
    var xmlFile: String? = null

    override fun run() {
        val validator = SaxonXmlValidator(ErrorResultParser())
        val result = validator.validate(xmlFile!!, xslFile!!)

        val gson = Gson()

        print(gson.toJson(result))
    }
}