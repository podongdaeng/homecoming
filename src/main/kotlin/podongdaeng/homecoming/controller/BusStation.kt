package podongdaeng.homecoming.controller
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Response(
    val response: ResponseData
)

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
    val item: List<BusStation>
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
    val lati: Double,
    val long: Double,
)

fun parseJsonResponse(jsonString: String): Response{
    val gson= Gson()
    return gson.fromJson(jsonString, Response::class.java)
}