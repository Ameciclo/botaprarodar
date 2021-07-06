package app.igormatos.botaprarodar.domain.usecase.trips

import app.igormatos.botaprarodar.common.enumType.BikeActionsMenuType
import app.igormatos.botaprarodar.data.repository.BikeRepository
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.BikeActivity
import app.igormatos.botaprarodar.domain.model.Devolution
import app.igormatos.botaprarodar.domain.model.Withdraws
import app.igormatos.botaprarodar.presentation.main.trips.TripsItemType
import app.igormatos.botaprarodar.utils.bikeActivityDate
import app.igormatos.botaprarodar.utils.bikeWithOnlyOneDevolution
import app.igormatos.botaprarodar.utils.bikeWithOnlyOneWithdraw
import app.igormatos.botaprarodar.utils.tripsItemTypeWithoutTitleList
import com.brunotmgomes.ui.SimpleResult
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
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
            tripsItemTypeWithoutTitleList
        )

        createdTitleTripsItemResult.data.forEach {
            if (it is TripsItemType.TitleType) {
                createdTitle = it.title
            }
        }
        assertEquals(bikeActivityDate, createdTitle)
    }

    @Test
    fun `should create trips item type with withdraw`() {
        val bikeList = listOf(bikeWithOnlyOneWithdraw)
        val bikeWithdraw = bikeWithOnlyOneWithdraw.withdraws?.get(0)

        val bikeTripsItemResult = bikeActionUseCase.convertBikesToTripsItem(bikeList)
        val bikeActivityWithdrawResult = getBikeActivityFromTripsItemType(bikeTripsItemResult)
        compareAndDoBikeAsserts(bikeWithOnlyOneWithdraw, bikeActivityWithdrawResult)
        compareAndDoWithdrawAsserts(bikeWithdraw, bikeActivityWithdrawResult)
    }

    @Test
    fun `should create trips item type with devolution`() {
        val bikeList = listOf(bikeWithOnlyOneDevolution)
        val bikeDevolution = bikeWithOnlyOneDevolution.devolutions?.get(0)

        val bikeTripsItemResult = bikeActionUseCase.convertBikesToTripsItem(bikeList)
        val bikeActivityDevolutionResult = getBikeActivityFromTripsItemType(bikeTripsItemResult)
        compareAndDoBikeAsserts(bikeWithOnlyOneDevolution, bikeActivityDevolutionResult)
        compareAndDoDevolutionAsserts(bikeDevolution, bikeActivityDevolutionResult)
    }

    private fun getBikeActivityFromTripsItemType(bikeTripsItemResult: SimpleResult.Success<List<TripsItemType>>): BikeActivity {
        val bikeTypesResult = bikeTripsItemResult.data.filterIsInstance<TripsItemType.BikeType>()
        assertThat(bikeTypesResult.size, not(0))
        return bikeTypesResult[0].bikeActivity
    }

    private fun compareAndDoBikeAsserts(bike: Bike, bikeActivityResult: BikeActivity) {
        with(bikeActivityResult) {
            assertThat(bikeId, equalTo(bike.id))
            assertThat(name, equalTo(bike.name))
            assertThat(orderNumber, equalTo(bike.orderNumber))
            assertThat(serialNumber, equalTo(bike.serialNumber))
            assertThat(photoThumbnailPath, equalTo(bike.photoThumbnailPath))
        }
    }

    private fun compareAndDoWithdrawAsserts(
        bikeWithdraw: Withdraws?,
        bikeActivityWithdrawResult: BikeActivity
    ) {
        val withdrawStatus = "EMPRÉSTIMO"
        assertThat(bikeActivityWithdrawResult.id, equalTo(bikeWithdraw?.id))

        val bikeActivityDateResult = bikeActivityWithdrawResult.date.toString()
        val bikeWithdrawDate = bikeWithdraw?.date.toString()
        val bikeActivityResultHasPartialDate = bikeWithdrawDate.contains(bikeActivityDateResult)
        assertTrue(bikeActivityResultHasPartialDate)

        assertThat(bikeActivityWithdrawResult.status, equalTo(withdrawStatus))
    }

    private fun compareAndDoDevolutionAsserts(
        bikeDevolution: Devolution?,
        bikeActivityDevolutionResult: BikeActivity
    ) {
        val devolutionStatus = "DEVOLUÇÃO"
        assertThat(bikeActivityDevolutionResult.id, equalTo(bikeDevolution?.id))

        val bikeActivityDateResult = bikeActivityDevolutionResult.date.toString()
        val bikeDevolutionDate = bikeDevolution?.date.toString()
        val bikeActivityResultHasPartialDate = bikeDevolutionDate.contains(bikeActivityDateResult)
        assertTrue(bikeActivityResultHasPartialDate)

        assertThat(bikeActivityDevolutionResult.status, equalTo(devolutionStatus))
    }
}