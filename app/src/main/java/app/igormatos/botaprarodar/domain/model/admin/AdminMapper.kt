package app.igormatos.botaprarodar.domain.model.admin

import app.igormatos.botaprarodar.common.extensions.convertToList
import app.igormatos.botaprarodar.data.model.Admin

class AdminMapper {

    fun adminRequestMapToAdminList(adminRequestMap: Map<String, AdminRequest>): List<Admin> {
        val adminRequestList: MutableList<AdminRequest> = adminRequestMap.convertToList()
        return adminRequestList.map {
            adminRequestToAdmin(it)
        }
    }

    fun adminRequestToAdmin(it: AdminRequest) = Admin(
        password = "",
        email = it.email ?: "",
        id = it.uid ?: ""
    )
}