import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class Main {
    private val driver: WebDriver
    private var element: WebElement? = null
    private val url: String

    init {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH)
        val options = ChromeOptions()
        options.addArguments("--start-maximized")
        options.addArguments("--disable-popup-blocking")
        driver = ChromeDriver(options)
        url = "https://terrorless.01ab.net/"
    }

    fun activateBot() {
        try {
            driver.get(url)
            Thread.sleep(2000)
            val element = driver.findElement(By.className("css-1qd5env"))
             element.click()
            Thread.sleep(2000)
                driver.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/div/div/div[2]/div[1]/div[1]/div[2]/span"))
            val count: Int = element.getText().substring(6, element.getText().length).toInt()
            for (i in 0 until count) {
                val data =
                    driver.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/div/div/div[2]/div[" + (i + 2) + "]"))
                System.out.println(data.getText()+"\n")
            }
            /*for(int i=0;i<count;i++)
            {
                element=driver.findElement
                        (By.cssSelector("#react-kakao-maps-sdk-map-container > div:nth-child(1) > div > div:nth-child(6) > div:nth-child("+(i+1)+")"));
                System.out.println("left:" + element.getCssValue("left")
                        + " top:" + element.getCssValue("top") + " display:"
                        + element.getCssValue("display") + "\n");
            }*/
            //좌표관련 내용
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            driver.close()
        }
    }

    companion object {
        // 웹드라이버 설치경로 설정
        var WEB_DRIVER_ID = "webdriver.chrome.driver"
        var WEB_DRIVER_PATH = "C:/Users/박경수/Downloads/chromedriver-win64/chromedriver-win64/chromedriver.exe"
        @JvmStatic
        fun main(args: Array<String>) {
            val bot1 = Main()
            bot1.activateBot()
            System.exit(0)
        }
    }
}