package greenter.sunat.validator.model

import com.google.gson.annotations.SerializedName

class ErrorResult {
    var code: String? = null
    var message: String? = null
    var level: ErrorLevel? = null
    @SerializedName("node_path")
    var nodePath: String? = null
    @SerializedName("node_value")
    var nodeValue: String? = null
}