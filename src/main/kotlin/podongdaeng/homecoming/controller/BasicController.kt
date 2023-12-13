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
import java.time.LocalDateTime

@RestController
class BasicController(
    private val getBusStationInfo: GetBusStationInfo,
    private val terrorlessCrawlingService: TerrorlessCrawlingService
) {
    final val mod_lat = -0.0004
    final val mod_lng = 0.0037
    val defaultResponse = listOf(
        BusStation(
            citycode = 23,
            gpslati = 37.5536,
            gpslong = 126.9372,
            nodeid = "ICB113000410",
            nodenm = "1",
            nodeno = 23035,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5517,
            gpslong = 126.9373,
            nodeid = "ICB113000411",
            nodenm = "2",
            nodeno = 23036,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5495,
            gpslong = 126.9386,
            nodeid = "ICB113000412",
            nodenm = "3",
            nodeno = 23037,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5540,
            gpslong = 126.9369,
            nodeid = "ICB113000413",
            nodenm = "4",
            nodeno = 23038,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5544,
            gpslong = 126.9368,
            nodeid = "ICB113000414",
            nodenm = "5",
            nodeno = 23039,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5548,
            gpslong = 126.9360,
            nodeid = "ICB113000415",
            nodenm = "6",
            nodeno = 23039,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5546,
            gpslong = 126.9357,
            nodeid = "ICB113000416",
            nodenm = "7",
            nodeno = 23040,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5555,
            gpslong = 126.9372,
            nodeid = "ICB113000417",
            nodenm = "8",
            nodeno = 23041,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5559,
            gpslong = 126.9387,
            nodeid = "ICB113000418",
            nodenm = "9",
            nodeno = 23042,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5568,
            gpslong = 126.9437,
            nodeid = "ICB113000419",
            nodenm = "10",
            nodeno = 23043,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5569,
            gpslong = 126.9440,
            nodeid = "ICB113000420",
            nodenm = "11",
            nodeno = 23044,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5565,
            gpslong = 126.9448,
            nodeid = "ICB113000421",
            nodenm = "12",
            nodeno = 23045,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5522,
            gpslong = 126.9454,
            nodeid = "ICB113000422",
            nodenm = "13",
            nodeno = 23046,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5506,
            gpslong = 126.9439,
            nodeid = "ICB113000423",
            nodenm = "14",
            nodeno = 23047,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5499,
            gpslong = 126.9439,
            nodeid = "ICB113000424",
            nodenm = "15",
            nodeno = 23048,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5482,
            gpslong = 126.9423,
            nodeid = "ICB113000425",
            nodenm = "16",
            nodeno = 23049,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5477,
            gpslong = 126.9427,
            nodeid = "ICB113000426",
            nodenm = "17",
            nodeno = 23050,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5490,
            gpslong = 126.9397,
            nodeid = "ICB113000427",
            nodenm = "18",
            nodeno = 23051,
        ),
        BusStation(
            citycode = 23,
            gpslati = 37.5481,
            gpslong = 126.9410,
            nodeid = "ICB113000428",
            nodenm = "19",
            nodeno = 23052,
        ),
    )

    @GetMapping("/near-station")
    fun parseBusInfo(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<GpsCoordinates> {
        var lati = gpsLati.toDouble()
        var long = gpsLong.toDouble()
        val center = try {
            Response.parseJsonResponse(getBusStationInfo.searchNearStationByGps(lati, long)).response.body.items.item
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        val up = try {
            Response.parseJsonResponse(
                getBusStationInfo.searchNearStationByGps(
                    lati + 0.005,
                    long
                )
            ).response.body.items.item ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val down = try {
            Response.parseJsonResponse(
                getBusStationInfo.searchNearStationByGps(
                    lati - 0.005,
                    long
                )
            ).response.body.items.item ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val left = try {
            Response.parseJsonResponse(
                getBusStationInfo.searchNearStationByGps(
                    lati,
                    long - 0.005
                )
            ).response.body.items.item ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val right = try {
            Response.parseJsonResponse(
                getBusStationInfo.searchNearStationByGps(
                    lati,
                    long + 0.005
                )
            ).response.body.items.item ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        val jsonResult = (defaultResponse + center + up + down + left + right).distinctBy { it.nodeno }.sortedBy { it.nodeno }

        println("${LocalDateTime.now()} BusStation call final json: ")
        println(jsonResult.joinToString(separator = "\n"))

        return jsonResult.map { busStation ->
            GpsCoordinates(
                busStation.nodenm,
                busStation.gpslati,
                busStation.gpslong
            )
        }
    }

    @GetMapping("/safe-near-station")
    fun parseBusInfoSafe(
        @RequestParam("gps_lati") gpsLati: String,
        @RequestParam("gps_long") gpsLong: String
    ): List<GpsCoordinates> {
        var lati = gpsLati.toDouble()
        var long = gpsLong.toDouble()

        val jsonResult = (defaultResponse).distinctBy { it.nodeid }.sortedBy { it.nodeid }

        println("${LocalDateTime.now()} safe busstation call / $lati / $long")

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