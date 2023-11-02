package podongdaeng.homecoming.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestParam
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse



@Controller
class BasicController {
    @GetMapping("/")
    fun home(): String {
        return "index.html"
    }
}

@Service
class AddressService(
    @Value("\${api.key}") private val apiKey: String,
) {
    fun searchNearStationByGps(
        gpsLati: Double,
        gpsLong: Double,
    ): String {
        val apiUrl =
            "http://apis.data.go.kr/1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList?serviceKey=${apiKey}&pageNo=1&numOfRows=30&_type=json&gpsLati=${gpsLati}&gpsLong=${gpsLong}"
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .GET()
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.body()
    }
}

@RestController
class AddressController @Autowired constructor(private val addressService: AddressService) {
    @GetMapping("/near-station")
    fun searchAddress(@RequestParam("gps_lati") gpsLati: String, @RequestParam("gps_long") gpsLong: String): String {
        val jsonString = addressService.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())
        /*
        val BS: List<BusStation> = parseJsonResponse(jsonString)

        val result = StringBuilder()

        for (busStation in BS) {
            result.append("City Code: ${busStation.citycode}, ")
            result.append("Latitude: ${busStation.gpslati}, ")
            result.append("Longitude: ${busStation.gpslong}, ")
            result.append("Node ID: ${busStation.nodeid}, ")
            result.append("Node Name: ${busStation.nodenm}, ")
            result.append("Node No: ${busStation.nodeno}\n")
        }
        */
        return jsonString
    }
}