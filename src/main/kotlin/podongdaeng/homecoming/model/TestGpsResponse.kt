package podongdaeng.homecoming.model

import com.google.gson.annotations.SerializedName

data class TestGpsResponse(
    @SerializedName("name") val name: String,
    @SerializedName("gps_lati") val latitude: Double,
    @SerializedName("gps_long") val longitude: Double,
)