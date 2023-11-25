package podongdaeng.homecoming

import org.junit.jupiter.api.Test
import podongdaeng.homecoming.controller.BasicController
import podongdaeng.homecoming.service.BasicService

class CrawlingTests {
    private val sel = BasicService.TerrorlessCrawlingService()
    private val stationGpsController = BasicController()
    @Test
    fun test1() {
        sel.tryCrawling()
    }

    @Test
    fun test2() {
        stationGpsController.searchAddress(gpsLati = 37.3.toString(), gpsLong = 126.5.toString())
    }
}
