package podongdaeng.homecoming.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
data class GpsCoordinatesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    val name: String="",
    val latitude: Double=0.0,
    val longitude: Double=0.0
)