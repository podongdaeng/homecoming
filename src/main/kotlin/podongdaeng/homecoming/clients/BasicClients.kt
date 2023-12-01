package podongdaeng.homecoming.clients

import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import podongdaeng.homecoming.model.TerrorlessData
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@Configuration
class BeanConfig {

    @Bean
    fun busStationInfo(): GetBusStationInfo {
        return GetBusStationInfo()
    }

    @Bean
    fun terrorlessCrawlingService(): TerrorlessCrawlingService {
        return TerrorlessCrawlingService()
    }
}

@Component
class GetBusStationInfo{
    @Value("\${api.key}") private val apiKey: String = ""
    fun searchNearStationByGps(
        gpsLati: Double,
        gpsLong: Double,
    ): String {
        val apiUrl =
            "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey=${apiKey}&pageNo=1&numOfRows=30&_type=json&gpsLati=${gpsLati}&gpsLong=${gpsLong}"
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .GET()
            .build()

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
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
