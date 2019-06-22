package greenter.sunat.validator.resolver

import greenter.sunat.validator.model.DocumentType
import java.nio.file.Paths

class XsltPathResolver(private val baseDirectory: String) : PathResolver {
    override fun getPath(type: DocumentType): String? {

        val xslFile = when (type) {
            DocumentType.FACTURA -> "2.x/ValidaExprRegFactura-2.0.1.xsl"
            DocumentType.BOLETA -> "2.x/ValidaExprRegBoleta-2.0.1.xsl"
            DocumentType.NOTA_CREDITO -> "2.x/ValidaExprRegNC-2.0.1.xsl"
            DocumentType.NOTA_DEBITO -> "2.x/ValidaExprRegND-2.0.1.xsl"
            DocumentType.RESUMEN_DIARIO -> "1.x/ValidaExprRegSummary-1.1.0.xsl"
            DocumentType.COMUNICACION_BAJA -> "1.x/ValidaExprRegSummaryVoided-1.0.1.xsl"
            DocumentType.GUIA_REMISION -> "1.x/ValidaExprRegGuiaRemitente-2.0.1.xsl"
            DocumentType.RETENCION -> "1.x/ValidaExprRegRetencion-1.0.2.xsl"
            DocumentType.PERCEPCION -> "1.x/ValidaExprRegPercepcion-1.0.1.xsl"
            DocumentType.REVERSION -> "1.x/ValidaExprRegOtrosVoided-1.0.1.xsl"
        }

        return Paths.get(baseDirectory, xslFile).toString()
    }
}