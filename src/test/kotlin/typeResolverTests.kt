import greenter.sunat.validator.model.DocumentType
import greenter.sunat.validator.resolver.XmlDocumentTypeResolver
import org.junit.Assert
import org.junit.Test

class TypeResolverTests {

    @Test
    fun getTypeInvoice() {
        val xml =
            """
<?xml version="1.0" encoding="utf-8"?>
<Invoice xmlns="urn:oasis:names:specification:ubl:schema:xsd:Invoice-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2">
    <cbc:ID>F001-123</cbc:ID>
    <cbc:InvoiceTypeCode listID="0101">01</cbc:InvoiceTypeCode>
</Invoice>
        """.trimIndent()
        val resolver = XmlDocumentTypeResolver()

        val type = resolver.getType(xml.byteInputStream())

        Assert.assertEquals(DocumentType.FACTURA, type)
    }

    @Test
    fun getTypeCreditNote() {
        val xml =
            """
<?xml version="1.0" encoding="utf-8"?>
<CreditNote xmlns="urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2">
    <cbc:ID>FF01-124</cbc:ID>
</CreditNote>
        """.trimIndent()
        val resolver = XmlDocumentTypeResolver()

        val type = resolver.getType(xml.byteInputStream())

        Assert.assertEquals(DocumentType.NOTA_CREDITO, type)
    }
}