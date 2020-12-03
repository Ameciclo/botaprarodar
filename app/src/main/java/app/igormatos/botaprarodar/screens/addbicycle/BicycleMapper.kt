package app.igormatos.botaprarodar.screens.addbicycle

data class BicycleMapper(
    var name: String = "",
    var serial_number: String = "",
    var order_number: Long = 0,
    var photo_path: String = "",
    var photo_thumbnail_path: String = "",
    var success: Boolean = false
) {
}