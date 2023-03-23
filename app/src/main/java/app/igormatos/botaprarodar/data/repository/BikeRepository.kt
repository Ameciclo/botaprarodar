package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.common.utils.formatAsJSONValidType
import app.igormatos.botaprarodar.data.network.api.BicycleApi
import app.igormatos.botaprarodar.data.network.safeApiCall
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.util.*

@ExperimentalCoroutinesApi
class BikeRepository(
    private val bicycleApi: BicycleApi,
) {

    suspend fun getBicycles(): SimpleResult<Map<String, BikeRequest>> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.getBicycles().await()
            }
        }
    }

    suspend fun addNewBike(bikeRequest: BikeRequest): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bikeRequest.id = UUID.randomUUID().toString()
                bicycleApi.addNewBike(bikeRequest, bikeRequest.id.orEmpty())
            }
        }
    }

    suspend fun updateBike(bikeRequest: BikeRequest): SimpleResult<AddDataResponse> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.updateBike(bikeRequest.id.orEmpty(), bikeRequest)
            }
        }
    }

    suspend fun getBikesByCommunityId(communityId: String): SimpleResult<Map<String, BikeRequest>> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.getBikesByCommunityId(formatAsJSONValidType(communityId))
            }
        }
    }

    suspend fun getBikeWithWithdrawByUser(userId: String): SimpleResult<Map<String, BikeRequest>> {
        return withContext(Dispatchers.IO) {
            safeApiCall {
                bicycleApi.getBikeWithWithdrawByUserId(formatAsJSONValidType(userId))
            }
        }
    }

    //TODO  Is need get list from Firebase
    fun getBicycleReturnUseMap() =
        mapOf(
            0 to "Para realizar entregas de aplicativos.",
            1 to "Deslocar para o local de trabalho.",
            2 to "Deslocar para o local de estudo.",
            3 to "Levar crianças para escola ou creche.",
            4 to "Resolver coisas do dia a dia. Ex: Mercado, lotéricas, banco.",
            5 to "Para passear, lazer."
        )
}
