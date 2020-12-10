package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.domain.model.Bicycle

fun buildBicycle(nameBicycle: String): Bicycle {
    return Bicycle().apply {
        name = nameBicycle
        order_number = System.currentTimeMillis()
        serial_number = "123serial"
        photo_path = "http://bla.com"
        photo_thumbnail_path = "http://bla.com"
    }
}

fun buildMapStringAndBicycle(howMuch: Int): Map<String, Bicycle> {
    val map = mutableMapOf<String, Bicycle>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = buildBicycle("bicycle $i")
    }
    return map
}