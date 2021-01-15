package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.domain.model.Bike

fun buildBicycle(nameBicycle: String): Bike {
    return Bike().apply {
        name = nameBicycle
        order_number = System.currentTimeMillis()
        serial_number = "123serial"
        photo_path = "http://bla.com"
        photo_thumbnail_path = "http://bla.com"
    }
}

fun buildMapStringAndBicycle(howMuch: Int): Map<String, Bike> {
    val map = mutableMapOf<String, Bike>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = buildBicycle("bicycle $i")
    }
    return map
}