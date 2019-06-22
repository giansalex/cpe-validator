package greenter.sunat.validator.model

enum class DocumentType (val value: String) {
    FACTURA("01"),
    BOLETA("03"),
    NOTA_CREDITO("07"),
    NOTA_DEBITO("08"),
    RESUMEN_DIARIO("RC"),
    COMUNICACION_BAJA("RR"),
    GUIA_REMISION("09"),
    RETENCION("20"),
    PERCEPCION("40"),
}