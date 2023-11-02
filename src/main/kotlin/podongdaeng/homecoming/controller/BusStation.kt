package podongdaeng.homecoming.controller

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class BusStation(
    val citycode: Int,
    val gpslati: Double,
    val gpslong: Double,
    val nodeid: String,
    val nodenm: String,
    val nodeno: Int
)

@Serializable
data class ResponseBody(
    val body: Body
)

@Serializable
data class Body(
    val items: Items
)

@Serializable
data class Items(
    val item: List<BusStation>
)
fun parseJsonResponse(jsonString: String): List<BusStation> {
    val response = Json.decodeFromString<ResponseBody>(jsonString)
    return response.body.items.item.map{
        BusStation(
            citycode= it.citycode,
            gpslati= it.gpslati,
            gpslong= it.gpslong,
            nodeid= it.nodeid,
            nodenm= it.nodenm,
            nodeno= it.nodeno
        )
    }
}