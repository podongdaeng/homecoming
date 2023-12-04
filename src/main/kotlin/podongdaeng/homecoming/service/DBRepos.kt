package podongdaeng.homecoming.service

import com.google.gson.JsonSyntaxException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import podongdaeng.homecoming.clients.GetBusStationInfo
import podongdaeng.homecoming.model.GpsCoordinatesEntity
import podongdaeng.homecoming.util.BusStation
import podongdaeng.homecoming.util.GpsCoordinates
import podongdaeng.homecoming.util.Response

interface GpsCoordinatesRepository : JpaRepository<GpsCoordinatesEntity, Long> {
    fun findByLatitudeBetweenAndLongitudeBetween(minLat: Double, maxlat: Double, minLong: Double, maxLong: Double): List<GpsCoordinatesEntity>
}

fun findGpsCoordinatesInRange(
    gpsLati: String,
    gpsLong: String,
    gpsCoordinatesRepository: GpsCoordinatesRepository
): List<GpsCoordinatesEntity> {
    return gpsCoordinatesRepository.findByLatitudeBetweenAndLongitudeBetween(
        gpsLati.toDouble() - 0.01,
        gpsLati.toDouble() + 0.01,
        gpsLong.toDouble() - 0.01,
        gpsLong.toDouble() + 0.01
    )
}

fun mapBusStationsToEntities(response: Response): List<GpsCoordinatesEntity> {
    return response.response.body.items.item.map { busStation: BusStation ->
        GpsCoordinatesEntity(
            name = busStation.name,
            latitude = busStation.latitude,
            longitude = busStation.longitude
        )
    }
}

fun mapGpsEntitiesToCoordinates(gpsEntities: List<GpsCoordinatesEntity>): List<GpsCoordinates> {
    return gpsEntities.map { busStation ->
        GpsCoordinates(
            busStation.name,
            busStation.latitude,
            busStation.longitude
        )
    }
}


@Scheduled(cron = "0 0 0 1 1 ?")
fun performYearlyTask(
    getBusStationInfo: GetBusStationInfo,
    gpsCoordinatesRepository: GpsCoordinatesRepository
){
    gpsCoordinatesRepository.deleteAll()
    var lati = 34.000
    var long = 126.000
    while(lati<38.5){
        while(long<129.5){
            val jsonString = getBusStationInfo.searchNearStationByGps(lati, long)
            try {
                val response = Response.parseJsonResponse(jsonString)
                val gpsEntities = mapBusStationsToEntities(response)
                gpsCoordinatesRepository.saveAll(gpsEntities)
            }
            catch(e: JsonSyntaxException){}
            long= long + 0.005
        }
        lati = lati + 0.005
    }
}
