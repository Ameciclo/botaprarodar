package app.igormatos.botaprarodar.domain.usecase.trips

import androidx.lifecycle.viewModelScope
import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.common.extensions.convertToBikeList
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.*
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
class BikeActionUseCase(private val bikeRepository: BikeRepository) {

    private var result: SimpleResult<MutableList<TripsItemType>> =
        SimpleResult.Error(Exception(""))

    var a = mutableListOf<Bike>()

    fun getBikeActionsList(): List<BikeActionsMenuType> {
        return BikeActionsMenuType.values().toMutableList()
    }

    suspend fun getBikes(communityId: String): Flow<SimpleResult<List<BikeRequest>>> {
        return bikeRepository.getBikes(communityId)
    }

//    bikeRepository.getBikes(communityId)
//    .collect {
//        when (it) {
//            is SimpleResult.Success -> {
//                val bikeList = it.data.convertToBikeList()
//                a = bikeList
////                        teste(bikeList)
//            }
//            is SimpleResult.Error -> {
//                result = it
//            }
//        }
//    }

    fun teste(bikeList: List<Bike>): SimpleResult<List<TripsItemType>> {
        val bikeActivities = mutableListOf<TripsItemType>()

        bikeList.forEach { bike ->
            bike.withdraws?.map { withdraw ->
                bikeActivities.add(
                    TripsItemType.BikeType(BikeActivity().apply {
                        this.id = bike.id
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
                        this.id = bike.id
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

        result = SimpleResult.Success(bikeActivities)
        return result
    }
}