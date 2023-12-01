package podongdaeng.homecoming.controller

import org.springframework.beans.factory.annotation.Value
import podongdaeng.homecoming.model.TerrorlessData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.clients.GetBusStationInfo
import podongdaeng.homecoming.clients.TerrorlessCrawlingService
import podongdaeng.homecoming.BasicService
import podongdaeng.homecoming.model.TerrorlessDataSimple
import podongdaeng.homecoming.model.TestGpsResponse
import podongdaeng.homecoming.util.GpsCoordinates
import podongdaeng.homecoming.util.Response

@RestController
class BasicController(
    private val getBusStationInfo: GetBusStationInfo,
    private val terrorlessCrawlingService: TerrorlessCrawlingService
){

    @GetMapping("/near-station")
    fun parseBusInfo(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<GpsCoordinates> {
        val jsonString = getBusStationInfo.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())

        val response = Response.parseJsonResponse(jsonString)
        val jsonResult = response.response.body.items.item

        return jsonResult.map{busStation -> GpsCoordinates(busStation.nodenm,busStation.gpslati,busStation.gpslong) }

    }

    @GetMapping("/near-threat")
    fun searchTerrorless(
        @RequestParam("gps_lati") gpsLati: Double,
        @RequestParam("gps_long") gpsLong: Double
    ): List<TerrorlessDataSimple> {
        //요청을 받을 때가 DB에 사전 저장 후 DB자료를 기준으로 해당 List만 전달해주도록
        val datas = terrorlessCrawlingService.tryCrawling()
        val threatDatas = mutableListOf<TerrorlessDataSimple>()
        for (data in datas.result.data.json.threats) {
            if (data.locationLatitude != null) //parameter위치를 기준으로 판별을 위한 과정 추가 및 수정해야함.
            {
                val simpleData = TerrorlessDataSimple(data.locationName, data.locationLatitude, data.locationLongitude)
                threatDatas.add(simpleData)
            }
        }
        return threatDatas
    }

    @GetMapping("/front-test/{number}")
    fun frontTestApi(
        @PathVariable("number") testNumber: Int,
        @RequestParam("gps_lati") gpsLati: Double,
        @RequestParam("gps_long") gpsLong: Double,
    ): List<TestGpsResponse> {
        val listOfGps = when (testNumber) {
            1 -> {
                val fixedGpsList = (0..3).map {
                    TestGpsResponse(
                        name = "${it.toString()} 번 핀",
                        latitude = gpsLati - 0.025 + 0.0125 * it,
                        longitude = gpsLong - 0.025 + 0.0125 * it,
                    )
                }
                fixedGpsList
            }

            2 -> {
                val randomGpsList = (1..4).map {
                    TestGpsResponse(
                        name = "${it.toString()} 번 핀",
                        latitude = gpsLati + (0.05) * Math.random(),
                        longitude = gpsLong + (0.05) * Math.random(),
                    )
                }
                randomGpsList
            }

            3 -> {
                val singleGps = TestGpsResponse(
                    name = "1 번 핀",
                    latitude = gpsLati,
                    longitude = gpsLong,
                )
                listOf(singleGps)
            }

            else -> emptyList()
        }
        return listOfGps
    }
}