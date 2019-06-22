package greenter.sunat.validator.resolver

import greenter.sunat.validator.model.DocumentType
import org.w3c.dom.Document
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
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
            "VoidedDocuments" -> DocumentType.COMUNICACION_BAJA
            "DespatchAdvice" -> DocumentType.GUIA_REMISION
            "Retention" -> DocumentType.RETENCION
            "Perception" -> DocumentType.PERCEPCION

            else -> null
        }
    }

    private fun getTypeFromInvoice(doc: Document?): DocumentType {
        val factory = XPathFactory.newInstance()
        val xpath = factory.newXPath()

        val type = xpath.evaluate("//*[local-name()='InvoiceTypeCode']/text()", doc, XPathConstants.STRING) as String

        return if (type == "01") DocumentType.FACTURA else DocumentType.BOLETA
    }
}