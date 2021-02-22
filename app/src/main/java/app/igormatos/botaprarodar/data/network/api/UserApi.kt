package app.igormatos.botaprarodar.data.network.api

import app.igormatos.botaprarodar.data.model.BicycleRequest
import app.igormatos.botaprarodar.data.model.UserRequest
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("/communities/{communityId}/users.json")
    suspend fun addUser(
        @Path("communityId") communityId: String,
        @Body user: UserRequest
    ): AddDataResponse

    @PATCH("/communities/{communityId}/users/{userId}.json")
    suspend fun updateUser(
        @Path("communityId") communityId: String,
        @Path("userId") userId: String,
        @Body user: UserRequest
    ): AddDataResponse
}