package hackathon.co.kr.util.network.model

import com.google.gson.JsonObject

data class ResponseVO (
        var code: String = " ",
        var result: JsonObject = JsonObject()
)