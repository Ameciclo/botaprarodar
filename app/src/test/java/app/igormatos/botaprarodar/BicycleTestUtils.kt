package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.domain.model.Bike

fun getBikeFixture(nameBicycle: String): Bike {
    return Bike().apply {
        name = nameBicycle
        orderNumber = System.currentTimeMillis()
        serialNumber = "123serial"
        photoPath = "http://bla.com"
        photoThumbnailPath = "http://bla.com"
    }
}

fun buildMapStringAndBicycle(howMuch: Int): Map<String, Bike> {
    val map = mutableMapOf<String, Bike>()
    for (i in howMuch downTo 1) {
        map[i.toString()] = getBikeFixture("bicycle $i")
    }
    return map
}