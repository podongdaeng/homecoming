package podongdaeng.homecoming.util
import com.google.gson.Gson

data class Response(
    val response: ResponseData
){
    companion object{
        fun parseJsonResponse(jsonString: String): Response {
            val gson= Gson()
            return gson.fromJson(jsonString, Response::class.java)
        }
    }
}

data class ResponseData(
    val header: Header,
    val body: Body
)
data class Header(
    val resultCode: String,
    val resultMsg: String
)
data class Body(
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)

data class Items(
    val item: List<BusStation>
)

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
    val latitude: Double,
    val longitude: Double,
)

