package app.igormatos.botaprarodar.domain.usecase.bikes

import androidx.lifecycle.asLiveData
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.TesteConverter
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeRequest
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class BikesUseCase(private val bikeRepository: BikeRepository) {

    private val convertMapper = TesteConverter()

//    suspend fun list(communityId: String): SimpleResult<List<Bike>> {
//        val bicyclesMap = bikeRepository.getBicycles(communityId)
//        return when (bicyclesMap) {
//            is SimpleResult.Success -> {
//                val list = listBicyclesConverter.convert(bicyclesMap.data)
//                formatAnswer(list)
//            }
//            is SimpleResult.Error -> {
//                bicyclesMap
//            }
//        }
//    }

    private fun formatAnswer(list: List<Bike>): SimpleResult<List<Bike>> {
        return when (list.isNotEmpty()) {
            true -> SimpleResult.Success(list)
            false -> SimpleResult.Error(Exception(""))
        }
    }

    suspend fun getBikes(communityId: String): Flow<SimpleResult<List<BikeRequest>>> {
//        var a: SimpleResult<List<Bike>> = SimpleResult.Error(java.lang.Exception())
//        bikeRepository.getBikes(communityId).collect {
//            when (it) {
//                is SimpleResult.Success -> {
//                    val list = convertMapper.convert(it.data)
//                    a = formatAnswer(list)
//                }
//                is SimpleResult.Error -> {
//                    a = SimpleResult.Error(Exception(""))
//                }
//            }
//        }
//        return a
        return bikeRepository.getBikes(communityId)
    }
}