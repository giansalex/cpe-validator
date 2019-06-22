package greenter.sunat.validator.resolver

import greenter.sunat.validator.model.DocumentType
import org.w3c.dom.Document
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class XmlDocumentTypeResolver : DocumentTypeResolver {
    override fun getType(xmlContent: InputStream): DocumentType? {

        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val doc = dBuilder.parse(xmlContent)
        doc.documentElement.normalize()

        return when (doc.documentElement.localName ?: doc.documentElement.nodeName) {
            "Invoice" -> getTypeFromInvoice(doc)
            "CreditNote" -> DocumentType.NOTA_CREDITO
            "DebitNote" -> DocumentType.NOTA_DEBITO
            "SummaryDocuments" -> DocumentType.RESUMEN_DIARIO
            "VoidedDocuments" -> getTypeFromVoided(doc)
            "DespatchAdvice" -> DocumentType.GUIA_REMISION
            "Retention" -> DocumentType.RETENCION
            "Perception" -> DocumentType.PERCEPCION

            else -> null
        }
    }

    private fun getTypeFromInvoice(doc: Document?): DocumentType {
        val type = getTextValue(doc, "//*[local-name()='InvoiceTypeCode']/text()")

        return if (type == "01") DocumentType.FACTURA else DocumentType.BOLETA
    }

    private fun getTypeFromVoided(doc: Document?): DocumentType {
        val id = getTextValue(doc, "//*[local-name()='ID']/text()")

        return if (id.startsWith("RR")) DocumentType.REVERSION else DocumentType.COMUNICACION_BAJA
    }

    private fun getTextValue(doc: Document?, query: String): String {
        val xpath = createXpath()

        return xpath.evaluate(query, doc, XPathConstants.STRING) as String
    }

    private fun createXpath(): XPath {
        val factory = XPathFactory.newInstance()

        return factory.newXPath()
    }
}