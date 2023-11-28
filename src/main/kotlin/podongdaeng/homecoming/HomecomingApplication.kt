package podongdaeng.homecoming

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class HomecomingApplication

fun main(args: Array<String>) {
	runApplication<HomecomingApplication>(*args)
}