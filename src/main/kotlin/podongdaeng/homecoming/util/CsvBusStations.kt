package podongdaeng.homecoming.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import java.io.File
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.DataFormatter
import podongdaeng.homecoming.controller.GpsCoordinates
import java.io.BufferedWriter
import java.io.FileInputStream
import java.io.FileWriter

fun readExcelFile(filePath: File): List<GpsCoordinates> {
    val workbook = XSSFWorkbook(FileInputStream(filePath))
    val sheet = workbook.getSheetAt(0) // Assuming data is in the first sheet
    val dataFormatter = DataFormatter()
    val busStations = mutableListOf<GpsCoordinates>()

    for (rowIndex in 1 until sheet.physicalNumberOfRows) {
        val row = sheet.getRow(rowIndex)

        val busStationName = dataFormatter.formatCellValue(row.getCell(1)).trim()
        val latitude = dataFormatter.formatCellValue(row.getCell(2)).toDoubleOrNull() ?: 0.0
        val longitude = dataFormatter.formatCellValue(row.getCell(3)).toDoubleOrNull() ?: 0.0

        // Now you can use the extracted data as needed
        // For example, you can create instances of your data class and store them in a list
        val busStation = GpsCoordinates(
            busStationName, latitude, longitude
        )

        busStations.add(busStation)
        // Process the bus station data as needed
    }

    workbook.close()
    return busStations
}

fun writeBStoFile(busStations:List<GpsCoordinates>,outputFile: File){
    BufferedWriter(FileWriter(outputFile)).use { writer ->
        writer.write("object BusStationData {\n")
        writer.write("    val busStations = listOf(\n")

        busStations.forEach { busStation ->
            writer.write(
                "        GpsCoordinates(\"${busStation.name}\",\"${busStation.lati}\",\"${busStation.long}\"\n"
            )
        }

        writer.write("    )\n")
        writer.write("}\n")
    }
}
