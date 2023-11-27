package podongdaeng.homecoming.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
            @SerialName("json") val json: JsonData // TODO: json 변수명 교체하는게 편할지도?
        ) {
            @Serializable
            data class JsonData(
                val threats: List<ThreatData>
            ) {
                @Serializable
                data class ThreatData(
                    val objectType: String,
                    val updatedAt: Long,
                    val locationName: String,
                    val status: String,
                    val createdAt: Long,
                    val locationLatitude: Double? = null,
                    val locationLongitude: Double? = null,
                    val locationRadius: Double? = null,
                    val description: String,
                    val id: String,
                    val eventTime: Long? = null,
                    val timeline: List<TimelineData>,
                ) {
                    @Serializable
                    data class TimelineData(
                        val eventTime: Long? = null,
                        val createdAt: Long,
                        val source: List<String>,
                        val status: String, // TODO: enum classify, 검거, 허위, 등등...
                    )
                }
            }
        }
    }
}
data class TerrorlessData_Simple(
    val name:String,
    val latitude : Double?,
    val longitude:Double?
)