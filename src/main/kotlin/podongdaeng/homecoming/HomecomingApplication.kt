package podongdaeng.homecoming

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.boot.SpringApplication

@SpringBootApplication
@EnableFeignClients
@ComponentScan("podongdaeng.homecoming","podongdaeng.homecoming.client")
class HomecomingApplication

fun main(args: Array<String>) {
	runApplication<HomecomingApplication>(*args)
}