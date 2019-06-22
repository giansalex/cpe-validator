package greenter.sunat.validator.command

import com.google.gson.Gson
import greenter.sunat.validator.parser.ErrorResultParser
import greenter.sunat.validator.process.SaxonXmlValidator
import greenter.sunat.validator.resolver.XmlDocumentTypeResolver
import greenter.sunat.validator.resolver.XsltPathResolver
import picocli.CommandLine.*
import java.io.File

class Validator : Runnable {

    @Option(names = ["-x", "--xsl-dir"], description = ["Path XSL Directory (Sunat Validator)"])
    private var xslDirectory: String? = null

    @Parameters(index = "0", description = ["XML File to validate"])
    var xmlFile: String? = null

    override fun run() {
        val baseXslDir = xslDirectory ?: getJarPath()
        System.setProperty("user.dir", baseXslDir)

        val xslResolver = XsltPathResolver(getValidationDir(baseXslDir))
        val typeResolver = XmlDocumentTypeResolver()
        val type = typeResolver.getType(File(xmlFile!!).inputStream())

        if (type == null) {
            println("Invalid XML or unknow document type")

            return
        }

        val xslFile = xslResolver.getPath(type)

        val validator = SaxonXmlValidator(ErrorResultParser())
        val result = validator.validate(xmlFile!!, xslFile)

        val gson = Gson()

        print(gson.toJson(result))
    }

    private fun getValidationDir(baseDir: String): String {
        return "$baseDir/sunat_archivos/sfs/VALI/commons/xsl/validation"
    }

    private fun getJarPath(): String {
        return File(javaClass.protectionDomain.codeSource.location.path).parent
    }
}