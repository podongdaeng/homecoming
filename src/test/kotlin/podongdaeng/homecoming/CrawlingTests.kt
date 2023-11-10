package podongdaeng.homecoming

import org.junit.jupiter.api.Test

class CrawlingTests {
    private val sel = BasicService.TerrorlessCrawlingService()
    @Test
    fun test1() {
        sel.tryCrawling()
    }
}
