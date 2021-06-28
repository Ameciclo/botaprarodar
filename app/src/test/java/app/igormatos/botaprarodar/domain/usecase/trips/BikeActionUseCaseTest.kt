package app.igormatos.botaprarodar.domain.usecase.trips

import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType
import app.igormatos.botaprarodar.utils.tripsItemTypeWithoutTitleList
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BikeActionUseCaseTest {

    private lateinit var bikeActionUseCase: BikeActionUseCase

    @MockK
    private lateinit var bikeRepository: BikeRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        bikeActionUseCase = BikeActionUseCase(bikeRepository)
    }

    @Test
    fun `when getBikeActions() check size list equals  to enum size `() {
        val list = bikeActionUseCase.getBikeActionsList()
        assert(BikeActionsMenuType.values().size == list.size)
    }

    @Test
    fun `should create title trips item according to current date`() {
        var createdTitle = String()

        val createdTitleTripsItemResult = bikeActionUseCase.createTitleTripsItem(
            tripsItemTypeWithoutTitleList)

        createdTitleTripsItemResult.data.forEach {
            if (it is TripsItemType.TitleType) {
                createdTitle = it.title
            }
        }
        assertEquals("11/11/1111", createdTitle)
    }
}