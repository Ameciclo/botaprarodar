package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.domain.model.admin.AdminRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AdminApiService {

    @GET("/admins/{adminId}.json")
    suspend fun getAdminById(@Path("adminId") adminId: String): Response<AdminRequest>
}