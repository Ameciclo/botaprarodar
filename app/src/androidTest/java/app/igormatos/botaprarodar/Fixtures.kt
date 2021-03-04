package app.igormatos.botaprarodar

import app.igormatos.botaprarodar.data.model.Admin
import app.igormatos.botaprarodar.domain.model.Bike

object Fixtures {

    val adminUser = Admin(email = "admin@admin.com", password = "admin", id = "admin001")

    val bike = Bike().apply {
        name = "name mock"
        order_number = 123
        serial_number = "serial mock"
        photo_path = "photo mock"
    }
}