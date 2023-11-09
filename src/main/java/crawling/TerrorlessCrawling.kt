import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Serializable // TODO: MOVE DATA CLASS TO NEW FILE!
data class TerrorlessData(
    val result: ResultData
) {
    @Serializable
    data class ResultData(
        val data: Data1
    ) {
        @Serializable
        data class Data1(
            val json: JsonData
        ) {
            @Serializable
            data class JsonData(
                val threats: List<ThreatData>
            ) {
                @Serializable
                data class ThreatData(
                    val threat: String? // TODO: 얘 확장해서 HTML 응답에 맞게 쭉~
                )
            }
        }
    }
}

@Component
class TerrorlessCrawling {
    fun activateBot() {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.terrorless.01ab.net/trpc/threat.list"))
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val print = response.body()
        println(print)
//        val a = Json.decodeFromString<TerrorlessData>(print)) // TODO: println(a) 가 잘 될지는 모르겠는데 대충 이렇게 객체로 마는데 성공하면 OK
//        println(a)
    }
}