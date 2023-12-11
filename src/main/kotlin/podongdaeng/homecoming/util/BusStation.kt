package podongdaeng.homecoming.util
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

@Serializable
data class Response(
    val response: ResponseData
){
    companion object{
        fun parseJsonResponse(jsonString: String): Response {
            val fixedString = jsonString.replace("\"\"", "{}") // TODO: better parsing
            println("${LocalDateTime.now()} / incoming jsonString : $fixedString")
            return Json.decodeFromString<Response>(fixedString)
        }
    }
}

@Serializable
data class ResponseData(
    val header: Header,
    val body: Body
)
@Serializable
data class Header(
    val resultCode: String,
    val resultMsg: String
)
@Serializable
data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

@Serializable
data class Items(
    val item: List<BusStation>? = emptyList()
)

@Serializable
data class BusStation(
    val citycode: Int,
    val gpslati: Double,
    val gpslong: Double,
    val nodeid: String,
    val nodenm: String,
    val nodeno: Int
)
data class GpsCoordinates(
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
)

