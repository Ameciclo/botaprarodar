package app.igormatos.botaprarodar.domain.usecase.trips

import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import org.junit.Before
import org.junit.Test

class BikeActionUseCaseTest {

    private lateinit var bikeActionUseCase : BikeActionUseCase

    @Before
    fun setUp(){
        bikeActionUseCase = BikeActionUseCase()
    }

    @Test
    fun `when getBikeActions() check size list equals  to enum size ` (){
       val list =  bikeActionUseCase.getBikeActionsList()
        assert(BikeActionsMenuType.values().size == list.size)
    }
}