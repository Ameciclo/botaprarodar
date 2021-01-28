package app.igormatos.botaprarodar.presentation.bikeform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bicycle.BikeFormUseCase
import app.igormatos.botaprarodar.presentation.addbicycle.BikeFormViewModel
import com.brunotmgomes.ui.SimpleResult
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class BikeFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val addNewBikeUseCase = mockk<BikeFormUseCase>()
    private var community = mockk<Community>(relaxed = true)

    lateinit var bikeViewModel: BikeFormViewModel

    @Before
    fun setup() {
        bikeViewModel =
            BikeFormViewModel(addNewBikeUseCase = addNewBikeUseCase, community = community)
        bikeViewModel.orderNumber.postValue(bikeFake.order_number.toString())
    }

    @Test
    fun `When 'BikeForm' is invalid then 'valid' return false`() {

        val observerBikeResultMock = mockk<Observer<Boolean>>(relaxed = true)

        val serialNumber = "S1209"
        val bikeName = ""
        bikeViewModel.valid.observeForever(observerBikeResultMock)
        bikeViewModel.serialNumber.postValue(serialNumber)
        bikeViewModel.bikeName.postValue(bikeName)

        verify {
            observerBikeResultMock.onChanged(false)
        }
    }

    @Test
    fun `When 'BikeForm' is valid then 'valid' return true`() {
        val serialNumber = "S1209"
        val bikeName = "S1209"
        val orderNumber = "12345678"
        val imagePath = "data/img/image.jpeg"
        val observerBikeResultMock = mockk<Observer<Boolean>>(relaxed = true)
        bikeViewModel.valid.observeForever(observerBikeResultMock)
        bikeViewModel.serialNumber.postValue(serialNumber)
        bikeViewModel.bikeName.postValue(bikeName)
        bikeViewModel.orderNumber.postValue(orderNumber)
        bikeViewModel.imagePath.postValue(imagePath)

        verify {
            observerBikeResultMock.onChanged(true)
        }
    }

    @Test
    fun `When 'serialnumber' is empty then 'isSerialNumberValid' return false`() {
        val serialNumber = ""
        bikeViewModel.serialNumber.postValue(serialNumber)
        assertFalse(bikeViewModel.isTextValid(serialNumber))
    }

    @Test
    fun `When 'serialnumber' is not null or empty then 'isSerialNumberValid' return true`() {
        val serialNumber = "S1209"
        bikeViewModel.serialNumber.postValue(serialNumber)
        assertTrue(bikeViewModel.isTextValid(serialNumber))
    }

    @Test
    fun `When 'registerBicycle', then registeredBicycleResult should return Success Status`() {
        val bikeFake = slot<Bike>()
        val result = SimpleResult.Success("")
        coEvery {
            addNewBikeUseCase.addNewBike(community.id, capture(bikeFake))
        } returns result


        bikeViewModel.registerBicycle()
        assertTrue(bikeViewModel.state.value is BikeFormStatus.Success)
    }

    @Test
    fun `When 'registerBicycle', then registeredBicycleResult should return Error Status`() {
        val bikeFake = slot<Bike>()
        val result = SimpleResult.Error(Exception())

        coEvery {
            addNewBikeUseCase.addNewBike(community.id, capture(bikeFake))
        } returns result

        bikeViewModel.registerBicycle()

        assertTrue(bikeViewModel.state.value is BikeFormStatus.Error)
    }

    @Test
    fun `check success status ordering`() {
        val bikeFake = slot<Bike>()
        val observerBikeResultMock = mockk<Observer<BikeFormStatus>>(relaxed = true)
        val simpleResultData = "bicicleta caloi"
        val result = SimpleResult.Success(simpleResultData)

        coEvery {
            addNewBikeUseCase.addNewBike(community.id, capture(bikeFake))
        } returns result
        bikeViewModel.state.observeForever(observerBikeResultMock)
        bikeViewModel.registerBicycle()

        verifyOrder {
            observerBikeResultMock.onChanged(BikeFormStatus.Loading)
            observerBikeResultMock.onChanged(BikeFormStatus.Success(simpleResultData))
        }
    }

    @Test
    fun `when 'updateImagePath', should update imagePath value`() {
        bikeViewModel.imagePath.value = bikeFake.photo_path
        val expected = "new image path"
        bikeViewModel.updateImagePath(expected)

        assertEquals(expected, bikeViewModel.imagePath.value)
    }

    @Test
    fun `when 'isTextValid' with blank string, should return false`() {
        val value = bikeViewModel.isTextValid("")
        assertFalse(value)
    }

    @Test
    fun `when 'isTextValid' with valid string, should return true`() {
        val value = bikeViewModel.isTextValid("valid")
        assertTrue(value)
    }

    @Test
    fun `when 'valid' with one field empty, should return false`() {
        bikeViewModel.bikeName.postValue("")
        bikeViewModel.serialNumber.postValue("mock")
        bikeViewModel.orderNumber.postValue("mock")
        bikeViewModel.imagePath.postValue("mock")

        bikeViewModel.valid.value?.let { assertFalse(it) }
    }

    @Test
    fun `when 'valid' with all field correct, should return true`() {
        bikeViewModel.bikeName.postValue("mock")
        bikeViewModel.serialNumber.postValue("mock")
        bikeViewModel.orderNumber.postValue("mock")
        bikeViewModel.imagePath.postValue("mock")

        bikeViewModel.valid.value?.let { assertTrue(it) }
    }

    private val bikeFake = Bike().apply {
        name = "Monark"
        serial_number = "12345"
        order_number = 0
        photo_path = "path"
    }
}