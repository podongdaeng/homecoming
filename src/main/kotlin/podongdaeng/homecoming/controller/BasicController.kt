package podongdaeng.homecoming.controller

<<<<<<< Updated upstream
import org.springframework.beans.factory.annotation.Value
import podongdaeng.homecoming.model.TerrorlessData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.BasicService
import podongdaeng.homecoming.model.TestGpsResponse
import podongdaeng.homecoming.service.AddressService

@RestController
class BasicController(
    private val addressService : AddressService
){
    private val terrorlessCrawlingService = BasicService.TerrorlessCrawlingService()

    @GetMapping("/near-station")
    fun searchAddress(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<GpsCoordinates> {
        return addressService.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())
    }

    @GetMapping("/terrorless-crawling")
    fun searchTerrorless(): TerrorlessData {
        return terrorlessCrawlingService.tryCrawling()
    }

    @GetMapping("/front-test/{number}")
    fun frontTestApi(
        @PathVariable("number") testNumber: Int,
        @RequestParam("gps_lati_lower_left") gpsLatiLowerLeft: Double,
        @RequestParam("gps_lati_upper_right") gpsLatiUpperRight: Double,
        @RequestParam("gps_long_lower_left") gpsLongLowerLeft: Double,
        @RequestParam("gps_long_upper_right") gpsLongUpperRight: Double,
    ): List<TestGpsResponse> {
        if (gpsLongUpperRight < gpsLongLowerLeft || gpsLatiUpperRight < gpsLatiLowerLeft) { // Assume UpperRight is always higher value
            throw Exception("upperright/lowerleft 순서가 잘못됐습니다")
        }
        val listOfGps = when (testNumber) {
            1 -> {
                val fixedGpsList = (0..3).map {
                    TestGpsResponse(
                        latitude = gpsLatiLowerLeft + (gpsLatiUpperRight - gpsLatiLowerLeft) * it,
                        longitude = gpsLongLowerLeft + (gpsLongUpperRight - gpsLongLowerLeft) * it
                    )
                }
                fixedGpsList
            }
            2 -> {
                val randomGpsList = (1..4).map {
                    TestGpsResponse(
                        latitude = gpsLatiLowerLeft + (gpsLatiUpperRight - gpsLatiLowerLeft) * Math.random(),
                        longitude = gpsLongLowerLeft + (gpsLongUpperRight - gpsLongLowerLeft) * Math.random(),
                    )
                }
                randomGpsList
            }
            3 -> {
                val singleGps = TestGpsResponse(
                    latitude = gpsLatiLowerLeft + (gpsLatiUpperRight - gpsLatiLowerLeft) / 2,
                    longitude = gpsLongLowerLeft + (gpsLongUpperRight - gpsLongLowerLeft) / 2
                )
                listOf(singleGps)
            }
            else -> emptyList()
        }
        return listOfGps
=======
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import podongdaeng.homecoming.client.AddressFeignClient


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
class AddressController @Autowired constructor(private val addressFeignClient: AddressFeignClient) {
    @GetMapping("/near-station")
    fun searchAddress(@RequestParam("gps_lati") gpsLati: String, @RequestParam("gps_long") gpsLong: String): List<GpsCoordinates> {
        val jsonString = addressService.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())

        val response = parseJsonResponse(jsonString)
        val jsonResult=response.response.body.items.item

        return jsonResult.map{busStation -> GpsCoordinates(busStation.nodenm,busStation.gpslati,busStation.gpslong)}

>>>>>>> Stashed changes
    }
}