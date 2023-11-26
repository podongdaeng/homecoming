package podongdaeng.homecoming.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import java.io.File
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.DataFormatter
import java.io.FileInputStream

@Serializable
data class CSVBusStation(
    val busStopNumber: String,
    val busStopName: String,
    val latitude: Double,
    val longitude: Double,
    val cityCode: Int,
    val cityName: String,
)


fun readExcelFile(filePath: File): List<CSVBusStation> {
    val workbook = XSSFWorkbook(FileInputStream(filePath))
    val sheet = workbook.getSheetAt(0) // Assuming data is in the first sheet
    val dataFormatter = DataFormatter()
    val busStations = mutableListOf<CSVBusStation>()

    for (rowIndex in 1 until sheet.physicalNumberOfRows) {
        val row = sheet.getRow(rowIndex)

        val busStationNumber = dataFormatter.formatCellValue(row.getCell(0)).trim()
        val busStationName = dataFormatter.formatCellValue(row.getCell(1)).trim()
        val latitude = dataFormatter.formatCellValue(row.getCell(2)).toDoubleOrNull() ?: 0.0
        val longitude = dataFormatter.formatCellValue(row.getCell(3)).toDoubleOrNull() ?: 0.0
        val cityCode = dataFormatter.formatCellValue(row.getCell(6)).toIntOrNull() ?: 0
        val cityName = dataFormatter.formatCellValue(row.getCell(7)).trim()

        // Now you can use the extracted data as needed
        // For example, you can create instances of your data class and store them in a list
        val busStation = CSVBusStation(
            busStationNumber, busStationName, latitude, longitude, cityCode, cityName
        )

        busStations.add(busStation)
        // Process the bus station data as needed
        println(busStation)
    }

    workbook.close()
    return busStations
}

