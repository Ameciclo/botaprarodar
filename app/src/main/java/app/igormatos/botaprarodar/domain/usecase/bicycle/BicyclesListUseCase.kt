package app.igormatos.botaprarodar.domain.usecase.bicycle

import app.igormatos.botaprarodar.data.repository.BicycleRepository
import app.igormatos.botaprarodar.domain.converter.bicycle.ListBicyclesConverter
import app.igormatos.botaprarodar.domain.model.Bicycle
import com.brunotmgomes.ui.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class BicyclesListUseCase(private val bicycleRepository: BicycleRepository) {

    private val listBicyclesConverter = ListBicyclesConverter()

    suspend fun list(communityId: String): SimpleResult<List<Bicycle>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val bicyclesMap = bicycleRepository.getBicycles(communityId)
                val list = listBicyclesConverter.convert(bicyclesMap)
                formatAnswer(list)
            } catch (exception: Exception) {
                return@withContext SimpleResult.Error(exception)
            }
        }
    }

    private fun formatAnswer(list: List<Bicycle>): SimpleResult<List<Bicycle>> {
        return when(list.isNotEmpty()) {
            true -> SimpleResult.Success(list)
            false -> SimpleResult.Error(Exception(""))
        }
    }

}