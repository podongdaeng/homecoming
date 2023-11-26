package podongdaeng.homecoming

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import podongdaeng.homecoming.util.readExcelFile
import java.io.File

@SpringBootApplication
@EnableFeignClients
class HomecomingApplication

fun main(args: Array<String>) {

	val csvFile = File("C:/Users/SAMSUNG/Desktop/2023-2/캡스톤디자인1/국토교통부_전국 버스정류장 위치정보.xlsx")
	val csvBusStations = readExcelFile(csvFile)
	runApplication<HomecomingApplication>(*args)
}