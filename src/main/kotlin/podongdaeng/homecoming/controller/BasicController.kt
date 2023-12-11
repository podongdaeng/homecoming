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
import podongdaeng.homecoming.util.BusStation
import podongdaeng.homecoming.util.GpsCoordinates
import podongdaeng.homecoming.util.Response

@RestController
class BasicController(
    private val getBusStationInfo: GetBusStationInfo,
    private val terrorlessCrawlingService: TerrorlessCrawlingService
) {

    @GetMapping("/near-station")
    fun parseBusInfo(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<GpsCoordinates> {
        var lati = gpsLati.toDouble()
        var long = gpsLong.toDouble()
        val center =
            Response.parseJsonResponse(getBusStationInfo.searchNearStationByGps(lati, long)).response.body.items.item
                ?: emptyList()
        val up = Response.parseJsonResponse(
            getBusStationInfo.searchNearStationByGps(
                lati + 0.01,
                long
            )
        ).response.body.items.item ?: emptyList()
        val down = Response.parseJsonResponse(
            getBusStationInfo.searchNearStationByGps(
                lati - 0.01,
                long
            )
        ).response.body.items.item ?: emptyList()
        val left = Response.parseJsonResponse(
            getBusStationInfo.searchNearStationByGps(
                lati,
                long - 0.01
            )
        ).response.body.items.item ?: emptyList()
        val right = Response.parseJsonResponse(
            getBusStationInfo.searchNearStationByGps(
                lati,
                long + 0.01
            )
        ).response.body.items.item ?: emptyList()

        val defaultResponse = listOf(
            BusStation(
                citycode = 23,
                gpslati = 37.556008,
                gpslong = 126.939948,
                nodeid = "ICB113000419",
                nodenm = "신촌오거리.2호선신촌역",
                nodeno = 23035,
            ),
            BusStation(
                citycode = 23,
                gpslati = 37.556198,
                gpslong = 126.939844,
                nodeid = "ICB112900226",
                nodenm = "신촌역2호선",
                nodeno = 22052,
            ),
            BusStation(
                citycode = 23,
                gpslati = 37.555732,
                gpslong = 126.938536,
                nodeid = "ICB113000417",
                nodenm = "신촌오거리.2호선신촌역",
                nodeno = 22014,
            ),
            BusStation(
                citycode = 23,
                gpslati = 37.556621,
                gpslong = 126.944551,
                nodeid = "ICB113000418",
                nodenm = "이대역",
                nodeno = 23036,
            ),
            BusStation(
                citycode = 23,
                gpslati = 37.555551,
                gpslong = 126.935581,
                nodeid = "ICB113000420",
                nodenm = "신촌오거리.현대백화점",
                nodeno = 23034,
            ),
        )

        val jsonResult = (defaultResponse + center + up + down + left + right).distinctBy { it.nodeid }

        return jsonResult.map { busStation ->
            GpsCoordinates(
                busStation.nodenm,
                busStation.gpslati,
                busStation.gpslong
            )
        } ?: emptyList()

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
                if ((data.locationLatitude - gpsLati < 0.01 && data.locationLatitude - gpsLati > -0.01) && (data.locationLongitude - gpsLong < 0.01 && data.locationLongitude - gpsLong > -0.01)) {
                    val simpleData =
                        TerrorlessDataSimple(data.locationName, data.locationLatitude, data.locationLongitude)
                    threatDatas.add(simpleData)
                }
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