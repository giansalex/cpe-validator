package greenter.sunat.validator.process

import greenter.sunat.validator.model.ErrorResult
import greenter.sunat.validator.parser.ResultParser
import net.sf.saxon.jaxp.TransformerImpl
import net.sf.saxon.serialize.MessageEmitter
import net.sf.saxon.trans.XPathException
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import javax.xml.transform.ErrorListener
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class SaxonXmlValidator (val resultParser: ResultParser) : XmlValidator {

    override fun validate(xml: String, xsltPath: String): List<ErrorResult> {

        return try {
            transform(xml, xsltPath)
        } catch (e: TransformerException) {
            val result = ArrayList<ErrorResult>()
            val error = resultParser.getResult(e.message)

            if (error != null) {
                result.add(error)
            }

            result
        }
    }

    @Throws(TransformerException::class)
    private fun transform(XmlFullPath: String, XsltFullPath: String): List<ErrorResult> {

        val xmlFile = File(XmlFullPath)

        val xsltStreamSource = getStreamSource(XsltFullPath)
        val xmlStreamSource = getStreamSource(XmlFullPath)
        val transformerFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null)
        val transformer = transformerFactory.newTransformer(xsltStreamSource) as TransformerImpl

        val listResult = ArrayList<ErrorResult>()

        transformer.setParameter("nombreArchivoEnviado", xmlFile.name)
        transformer.underlyingController.recoveryPolicy = 2
        transformer.errorListener = object : ErrorListener {
            override fun warning(exception: TransformerException?) {}
            override fun error(exception: TransformerException?) {}
            override fun fatalError(exception: TransformerException?) {}
        }
        transformer.underlyingController.messageEmitter = object : MessageEmitter() {
            var abort = false

            @Throws(XPathException::class)
            override fun startDocument(properties: Int) {
                setWriter(StringWriter())
                this.abort = properties and 0x4000 != 0
                super.startDocument(properties)
            }

            @Throws(XPathException::class)
            override fun endDocument() {
                val message = getWriter().toString()

                if (this.abort) {
                    throw XPathException(message)
                }

                val error = resultParser.getResult(message)

                if (error != null) {
                    listResult.add(error)
                }

                super.endDocument()
            }

            override fun close() {}
        }

        try {
            val fos = ByteArrayOutputStream()
            val out = OutputStreamWriter(fos, "ISO8859_1")
            transformer.setOutputProperty("encoding", StandardCharsets.ISO_8859_1.name())
            transformer.transform(xmlStreamSource, StreamResult(out))

            fos.close()
            out.close()
        } catch (e: IOException) {
            println("No se pudo cerrar la salida de la transformacion")
            e.printStackTrace()
        }

        return listResult
    }

    private fun getStreamSource(filePath: String): StreamSource {
        val source: StreamSource?
        try {
            source = StreamSource(InputStreamReader(FileInputStream(filePath), StandardCharsets.UTF_8))
        } catch (e1: UnsupportedEncodingException) {
            throw RuntimeException("Archivo no soporta codificación de caracteres", e1)
        } catch (e1: FileNotFoundException) {
            throw RuntimeException("No existe el archivo:$filePath")
        }

        return source
    }
}