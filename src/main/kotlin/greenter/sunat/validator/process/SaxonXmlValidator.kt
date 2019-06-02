package greenter.sunat.validator.process

import greenter.sunat.validator.model.ErrorResult
import net.sf.saxon.jaxp.TransformerImpl
import net.sf.saxon.serialize.MessageEmitter
import net.sf.saxon.trans.XPathException
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

class SaxonXmlValidator : XmlValidator {
    override fun validate(xml: String, xsltPath: String): ErrorResult {

        try {
            val warning = transform(xml, xsltPath)
        } catch (e: TransformerException) {
            e.printStackTrace()

            print(e.message)
        }

        print("Finished")

        return ErrorResult()
    }

    @Throws(TransformerException::class)
    private fun transform(XmlFullPath: String, XsltFullPath: String): List<ErrorResult> {

        val archivoXML = File(XmlFullPath)

        val xsltStreamSource = getStreamSource(XsltFullPath)
        val xmlStreamSource = getStreamSource(XmlFullPath)
        val transformerFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null)
        val transformer = transformerFactory.newTransformer(xsltStreamSource) as TransformerImpl

        val listResult = ArrayList<ErrorResult>()

        transformer.setParameter("nombreArchivoEnviado", archivoXML.name)
        transformer.underlyingController.recoveryPolicy = 2
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
                val error = ErrorResult()

                if (this.abort) {
                    throw XPathException(message)
                }
                listResult.add(error)

                super.endDocument()
            }

            override fun close() {}
        }

        println("Ejecutandose transformerFactory.newTransformer. Despues")

        println("Ejecutandose transformer.transform")
        try {
            val fos = ByteArrayOutputStream()
            val out = OutputStreamWriter(fos, "ISO8859_1")
            transformer.setOutputProperty("encoding", "ISO-8859-1")
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