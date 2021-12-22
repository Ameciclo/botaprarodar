package app.igormatos.botaprarodar.domain.usecase.trips

import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.common.extensions.orderByDate
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeActivity
import app.igormatos.botaprarodar.domain.model.BikeRequest
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class BikeActionUseCase(private val bikeRepository: BikeRepository) {

    fun getBikeActionsList(): List<BikeActionsMenuType> {

        return BikeActionsMenuType.values().toMutableList()
    }

    suspend fun getBikes(communityId: String): SimpleResult<List<BikeRequest>> {
        return when (val result = bikeRepository.getBikesByCommunityId(communityId)) {
            is SimpleResult.Success -> {
                SimpleResult.Success(result.data.values.toList())
            }
            is SimpleResult.Error -> {
                SimpleResult.Error(result.exception)
            }
        }
    }

    fun convertBikesToTripsItem(bikeList: List<Bike>): SimpleResult.Success<List<TripsItemType>> {
        val bikeActivities = mutableListOf<TripsItemType>()

        bikeList.forEach { bike ->
            bike.withdraws?.map { withdraw ->
                bikeActivities.add(
                    TripsItemType.BikeType(BikeActivity().apply {
                        this.id = withdraw.id
                        this.bikeId = bike.id
                        this.name = bike.name
                        this.orderNumber = bike.orderNumber
                        this.serialNumber = bike.serialNumber
                        this.photoThumbnailPath = bike.photoThumbnailPath
                        this.date = withdraw.date?.substring(0, 10)
                        this.status = "EMPRÉSTIMO"
                    })
                )
            }

            bike.devolutions?.map { devolution ->
                bikeActivities.add(
                    TripsItemType.BikeType(BikeActivity().apply {
                        this.id = devolution.id
                        this.bikeId = bike.id
                        this.name = bike.name
                        this.orderNumber = bike.orderNumber
                        this.serialNumber = bike.serialNumber
                        this.photoThumbnailPath = bike.photoThumbnailPath
                        this.date = devolution.date?.substring(0, 10)
                        this.status = "DEVOLUÇÃO"
                    })
                )
            }
        }

        return SimpleResult.Success(bikeActivities)
    }

    fun createTitleTripsItem(trips: MutableList<TripsItemType>): SimpleResult.Success<MutableList<TripsItemType>> {
        val orderingTrips = trips.orderByDate().reversed()
        val tripsToReturn = mutableListOf<TripsItemType>()
        val dates = mutableListOf<String>()
        var currentDate = ""

        orderingTrips.forEachIndexed { index, it ->
            when (it) {
                is TripsItemType.TitleType -> {
                }
                is TripsItemType.BikeType -> {
                    currentDate = it.bikeActivity.date.toString()
                    if (dates.contains(currentDate).not()) {
                        dates.add(currentDate)
                        tripsToReturn.add(TripsItemType.TitleType(currentDate))
                    }
                    tripsToReturn.add(it)
                }
            }
        }

        return SimpleResult.Success(tripsToReturn)
    }
}