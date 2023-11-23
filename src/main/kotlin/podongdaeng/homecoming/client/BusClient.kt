package podongdaeng.homecoming.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import podongdaeng.homecoming.controller.Response

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
}