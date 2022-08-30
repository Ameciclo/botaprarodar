package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.domain.model.User

fun List<User>.onlyAvailableUsers(): List<User> {
    return this.filter { user -> user.isAvailable }

}fun List<User>.sort(): List<User> {
    return this.sortedBy { user -> user.name }
}