package podongdaeng.homecoming.client

import kotlinx.serialization.json.Json
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.controller.Response
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@FeignClient(name = "address-service", url = "http://apis.data.go.kr/1613000/BusSttnInfoInqireService")
interface AddressFeignClient {
    @GetMapping("/getCrdntPrxmtSttnList")
    fun searchNearStationByGps(
        @RequestParam("serviceKey") apiKey: String,
        @RequestParam("pageNo") pageNo: Int = 1,
        @RequestParam("numOfRows") numOfRows: Int = 30,
        @RequestParam("gpsLati") gpsLati: Double,
        @RequestParam("gpsLong") gpsLong: Double,
        @RequestParam("_type") responseType: String = "json"
    ): Response

    @Component
    class AddressFeignClientImpl: AddressFeignClient {
        override fun searchNearStationByGps(
            apiKey: String,
            pageNo: Int,
            numOfRows: Int,
            gpsLati: Double,
            gpsLong: Double,
            responseType: String
        ): Response {
            val apiUrl =
                "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey=${apiKey}&pageNo=1&numOfRows=30&_type=json&gpsLati=${gpsLati}&gpsLong=${gpsLong}"
            val client = HttpClient.newBuilder().build();
            val request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            return Json.decodeFromString(response.body())
        }
    }
}