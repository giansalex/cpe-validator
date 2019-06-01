package greenter.sunat.validator.model

class ErrorResult {
    var code: String? = null
    var message: String? = null
    var level: ErrorLevel? = null
    var nodePath: String? = null
    var nodeValue: String? = null
}