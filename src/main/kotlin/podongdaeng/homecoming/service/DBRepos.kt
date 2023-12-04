package podongdaeng.homecoming.service

import org.springframework.data.jpa.repository.JpaRepository
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