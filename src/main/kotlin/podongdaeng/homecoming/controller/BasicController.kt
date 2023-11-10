package podongdaeng.homecoming.controller

import podongdaeng.homecoming.model.TerrorlessData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.BasicService

@RestController
class BasicController {
    private val addressService = BasicService.AddressService("jCzTbBpMghoBSBPXtV4dcVJEDWZGY6qpotKwLaTw7VU3cSeBk14uXozpSYvGQ7CQSDLM9GVHyqCPhAnNuafLGg%3D%3D")
    private val terrorlessCrawlingService = BasicService.TerrorlessCrawlingService()
    // TODO: application.property 등의 파일로 api.key 옮기기
    @GetMapping("/near-station")
    fun searchAddress(@RequestParam("gps_lati") gpsLati: String, @RequestParam("gps_long") gpsLong: String): List<BusStation> {
        val jsonString = addressService.searchNearStationByGps(gpsLati.toDouble(), gpsLong.toDouble())

        val response = parseJsonResponse(jsonString)
        val jsonResult=response.response.body.items.item

        return jsonResult
    }

    @GetMapping("/terrorless-crawling")
    fun searchAddress(): TerrorlessData {
        return terrorlessCrawlingService.tryCrawling()
    }
}