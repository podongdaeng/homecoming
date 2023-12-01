package podongdaeng.homecoming

import org.junit.jupiter.api.Test
import podongdaeng.homecoming.clients.TerrorlessCrawlingService
import podongdaeng.homecoming.controller.BasicController

class CrawlingTests {
    private val sel = TerrorlessCrawlingService()
    private val stationGpsController = BasicController()
class CrawlingTests() {
    private val sel = BasicService.TerrorlessCrawlingService()
    private val stationGpsController = BasicController("asdf")
    @Test
    fun test1() {
        sel.tryCrawling()
    }

    @Test
    fun test2() {
        stationGpsController.parseBusInfo(gpsLati = 37.3.toString(), gpsLong = 126.5.toString())
    }
}
