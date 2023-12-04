package podongdaeng.homecoming.controller

import org.springframework.beans.factory.annotation.Value
import podongdaeng.homecoming.model.TerrorlessData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.BasicService
import podongdaeng.homecoming.model.TerrorlessDataSimple
import podongdaeng.homecoming.model.TestGpsResponse

@RestController
class BasicController(
    @Value("\${api.key}") private val apiKey: String
) {
    private val addressService = BasicService.AddressService(apiKey)
    private val terrorlessCrawlingService = BasicService.TerrorlessCrawlingService()

    @GetMapping("/near-station")
    fun searchAddress(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<GpsCoordinates> {
        val jsonString = addressService.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())

        val response = parseJsonResponse(jsonString)
        val jsonResult = response.response.body.items.item

        return jsonResult.map { busStation ->
            GpsCoordinates(
                busStation.nodenm,
                busStation.gpslati,
                busStation.gpslong
            )
        }

    }

    @GetMapping("/near-threat")
    fun searchTerrorless(
        @RequestParam("gps_lati") gpsLati: Double,
        @RequestParam("gps_long") gpsLong: Double
    ): List<TerrorlessDataSimple> {
        //요청을 받을 때가 아닌 DB에 사전 저장 후 DB자료를 기준으로 해당 List만 전달해주도록 해야함.
        val datas = terrorlessCrawlingService.tryCrawling()
        val threatDatas = mutableListOf<TerrorlessDataSimple>()
        for (data in datas.result.data.json.threats) {
            if (data.locationLatitude != null && data.locationLongitude != null) //parameter위치를 기준으로 판별을 위한 과정 추가 및 수정해야함.
            {
                if((data.locationLatitude-gpsLati<0.01 && data.locationLatitude-gpsLati>-0.01)&&(data.locationLongitude-gpsLong<0.01 && data.locationLongitude-gpsLong>-0.01))
                {
                    val simpleData = TerrorlessDataSimple(data.locationName, data.locationLatitude, data.locationLongitude)
                    threatDatas.add(simpleData)
                }
            }
        }
        return threatDatas
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