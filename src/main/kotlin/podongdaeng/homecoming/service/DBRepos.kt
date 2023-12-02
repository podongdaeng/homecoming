package podongdaeng.homecoming.service

import org.springframework.data.jpa.repository.JpaRepository
import podongdaeng.homecoming.model.GpsCoordinatesEntity

interface GpsCoordinatesRepository : JpaRepository<GpsCoordinatesEntity, Long> {
    fun findByLatitudeBetweenAndLongitudeBetween(minLat: Double, maxlat: Double, minLong: Double, maxLong: Double): List<GpsCoordinatesEntity>
}
