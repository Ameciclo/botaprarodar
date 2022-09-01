package app.igormatos.botaprarodar.common.extensions

import app.igormatos.botaprarodar.common.enumType.UserSortFields
import app.igormatos.botaprarodar.domain.model.User

fun List<User>.onlyAvailableUsers(): List<User> {
    return this.filter { user -> user.isAvailable }

}fun List<User>.sortBy(sortField: UserSortFields): List<User> {
    return this.sortedBy { user ->
        when(sortField)  {
            UserSortFields.NAME -> user.name
        }
    }
}