package podongdaeng.homecoming.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {
    @GetMapping("/hello")
    fun hello(@RequestParam("name", required = false, defaultValue = "Student") name: String?): String {
        
        return "Hello, $name!"
    }
}