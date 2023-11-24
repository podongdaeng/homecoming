package podongdaeng.homecoming.controller

import podongdaeng.homecoming.model.TerrorlessData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.BasicService
import podongdaeng.homecoming.model.TestGpsResponse

@RestController
class BasicController {
    private val addressService =
        BasicService.AddressService("jCzTbBpMghoBSBPXtV4dcVJEDWZGY6qpotKwLaTw7VU3cSeBk14uXozpSYvGQ7CQSDLM9GVHyqCPhAnNuafLGg%3D%3D")
    private val terrorlessCrawlingService = BasicService.TerrorlessCrawlingService()

    // TODO: application.property 등의 파일로 api.key 옮기기
    @GetMapping("/near-station")
    fun searchAddress(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<BusStation> {
        val jsonString = addressService.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())

        val response = parseJsonResponse(jsonString)
        val jsonResult = response.response.body.items.item

        return jsonResult
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
    }
}