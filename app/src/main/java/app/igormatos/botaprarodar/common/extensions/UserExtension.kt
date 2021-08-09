package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.User

fun List<User>.onlyAvailableUsers(): List<User> {
    return this.filter { bike -> bike.isAvailable }
}