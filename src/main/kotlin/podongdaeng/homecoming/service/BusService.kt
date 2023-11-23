package podongdaeng.homecoming.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import podongdaeng.homecoming.client.AddressFeignClient
import podongdaeng.homecoming.controller.GpsCoordinates

@Service
class AddressService(
    private val addressFeignClient: AddressFeignClient
){
    @Value("\${api.key}")
    private val apiKey: String = ""

    fun searchNearStationByGps(
        gpsLati: Double,
        gpsLong: Double,
    ): List<GpsCoordinates> {
        val response = addressFeignClient.searchNearStationByGps(apiKey, gpsLati = gpsLati, gpsLong = gpsLong)
        return response.response.body.items.item.map { busStation ->
            GpsCoordinates(busStation.nodenm, busStation.gpslati, busStation.gpslong)
        }
    }
}
