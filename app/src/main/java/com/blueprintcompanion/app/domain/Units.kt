package com.blueprintcompanion.app.domain

enum class LengthUnit(val label: String, val symbol: String, val toFeet: Double) {
    FEET("Feet", "ft", 1.0),
    INCHES("Inches", "in", 1.0 / 12.0),
    METERS("Meters", "m", 3.280839895),
    CENTIMETERS("Centimeters", "cm", 0.03280839895);

    companion object {
        fun fromSymbol(symbol: String): LengthUnit =
            entries.firstOrNull { it.symbol == symbol } ?: FEET
    }
}

data class MeasurementResult(
    val areaSqFt: Double?,
    val perimeterFt: Double?,
    val volumeCuFt: Double?
)

fun toFeet(value: Double, unit: LengthUnit): Double = value * unit.toFeet

fun fromFeet(feet: Double, unit: LengthUnit): Double = feet / unit.toFeet

fun areaUnitLabel(unit: LengthUnit): String = when (unit) {
    LengthUnit.FEET -> "sq ft"
    LengthUnit.INCHES -> "sq in"
    LengthUnit.METERS -> "sq m"
    LengthUnit.CENTIMETERS -> "sq cm"
}

fun volumeUnitLabel(unit: LengthUnit): String = when (unit) {
    LengthUnit.FEET -> "cu ft"
    LengthUnit.INCHES -> "cu in"
    LengthUnit.METERS -> "cu m"
    LengthUnit.CENTIMETERS -> "cu cm"
}

fun linearUnitLabel(unit: LengthUnit): String = unit.symbol

/**
 * Area, perimeter, and volume from optional length / width / height.
 * Values are stored internally in feet. Missing dimensions omit the results that need them.
 */
fun calculate(lengthFt: Double?, widthFt: Double?, heightFt: Double?): MeasurementResult {
    val area = if (lengthFt != null && widthFt != null) lengthFt * widthFt else null
    val perimeter = if (lengthFt != null && widthFt != null) 2 * (lengthFt + widthFt) else null
    val volume = if (area != null && heightFt != null) area * heightFt else null
    return MeasurementResult(area, perimeter, volume)
}

fun formatNumber(value: Double): String {
    val rounded = kotlin.math.round(value * 100.0) / 100.0
    return if (rounded % 1.0 == 0.0) rounded.toLong().toString() else "%.2f".format(rounded)
}

fun dimensionLine(length: Double?, width: Double?, height: Double?, unit: LengthUnit): String {
    val parts = listOfNotNull(length, width, height).map { formatNumber(it) }
    if (parts.isEmpty()) return "No dimensions"
    return parts.joinToString(" × ") + " ${unit.symbol}"
}
