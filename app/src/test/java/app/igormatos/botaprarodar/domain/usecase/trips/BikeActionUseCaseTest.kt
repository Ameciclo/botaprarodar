package app.igormatos.botaprarodar.domain.usecase.trips

import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.data.repository.BikeRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class BikeActionUseCaseTest {

    private lateinit var bikeActionUseCase : BikeActionUseCase

    @MockK
    private lateinit var bikeRepository: BikeRepository

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        bikeActionUseCase = BikeActionUseCase(bikeRepository)
    }

    @Test
    fun `when getBikeActions() check size list equals  to enum size ` (){
       val list =  bikeActionUseCase.getBikeActionsList()
        assert(BikeActionsMenuType.values().size == list.size)
    }
}