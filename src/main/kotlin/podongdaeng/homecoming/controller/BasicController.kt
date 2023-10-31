package podongdaeng.homecoming.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.stereotype.Controller



@Controller
class BasicController {
    @GetMapping("/")
    fun home(): String {
        return "index.html"
    }
}
@Service
class AddressService(
    @Value("\${api.key}") private val apiKey: String){
        fun searchAddress(keyword: String): String {
            val apiUrl = "http://openapi.nsdi.go.kr/nsdi/addressLinkAddrSearch?cPage=1&numOfRows=10&address=$keyword&apiKey=$apiKey"
            val restTemplate = RestTemplate()
            return restTemplate.getForObject(apiUrl, String::class.java) ?: ""
        }
    }
@RestController
class AddressController @Autowired constructor(private val addressService: AddressService){
    @GetMapping("/search/address/{keyword}")
    fun searchAddress(@PathVariable keyword: String): String {
        return addressService.searchAddress(keyword)
    }
}