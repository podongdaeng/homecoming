package podongdaeng.homecoming.service

import feign.Feign
import podongdaeng.homecoming.model.TerrorlessData
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import podongdaeng.homecoming.client.AddressFeignClient
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import podongdaeng.homecoming.controller.GpsCoordinates

class BasicService {
    class AddressService(
        @Value("\${api.key}") private val apiKey: String,
    ){
        @Autowired private val addressFeignClient = AddressFeignClient.AddressFeignClientImpl()
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

    class TerrorlessCrawlingService {
        fun tryCrawling(): TerrorlessData {
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.terrorless.01ab.net/trpc/threat.list"))
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val print = response.body()
            val terrorlessData = Json.decodeFromString<TerrorlessData>(print)
            return terrorlessData
        }
    }
}
