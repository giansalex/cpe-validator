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

    @Test
    fun getTypeVoided() {
        val xml =
            """ 
<?xml version="1.0" encoding="utf-8"?>
<VoidedDocuments xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2">
    <cbc:ID>RA-20190621-001</cbc:ID>
     <cac:Signature>
        <cbc:ID>20123456789</cbc:ID>
        <cac:SignatoryParty>
            <cac:PartyIdentification>
                <cbc:ID>20123456789</cbc:ID>
            </cac:PartyIdentification>
        </cac:SignatoryParty>
    </cac:Signature>
</VoidedDocuments>
            """.trimIndent()

        val resolver = XmlDocumentTypeResolver()

        val type = resolver.getType(xml.byteInputStream())

        Assert.assertEquals(DocumentType.COMUNICACION_BAJA, type)
    }

    @Test
    fun getTypeReversion() {
        val xml =
            """ 
<?xml version="1.0" encoding="utf-8"?>
<VoidedDocuments xmlns="urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2">
    <cbc:ID>RR-20190621-001</cbc:ID>
</VoidedDocuments>
            """.trimIndent()

        val resolver = XmlDocumentTypeResolver()

        val type = resolver.getType(xml.byteInputStream())

        Assert.assertEquals(DocumentType.REVERSION, type)
    }
}