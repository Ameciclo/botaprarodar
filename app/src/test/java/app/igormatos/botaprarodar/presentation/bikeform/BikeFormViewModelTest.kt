package app.igormatos.botaprarodar.presentation.bikeform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.BikeFormStatus
import app.igormatos.botaprarodar.domain.model.AddDataResponse
import app.igormatos.botaprarodar.domain.model.Bike
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.bikeForm.BikeFormUseCase
import app.igormatos.botaprarodar.presentation.bikeForm.BikeFormViewModel
import app.igormatos.botaprarodar.utils.bike
import com.brunotmgomes.ui.SimpleResult
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue

class BikeFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val bikeFormUseCase = mockk<BikeFormUseCase>()
    private var community = mockk<Community>(relaxed = true)

    private lateinit var bikeViewModel: BikeFormViewModel

    @Before
    fun setup() {
        bikeViewModel =
            BikeFormViewModel(bikeFormUseCase = bikeFormUseCase,
                community = community,
                communityBikes = arrayListOf(bike))
        bikeViewModel.orderNumber.postValue(bikeFake.orderNumber.toString())
    }

    @Test
    fun `when 'BikeForm' is invalid then 'valid' return false`() {

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
    fun `when 'BikeForm' is valid then 'valid' return true`() {
        val observerBikeResultMock = fillAndObserveValidBike()

        verify {
            observerBikeResultMock.onChanged(true)
        }
    }

    @Test
    fun `when 'serialnumber' is empty then 'isTextValid' return false`() {
        val serialNumber = ""
        bikeViewModel.serialNumber.postValue(serialNumber)
        assertFalse(bikeViewModel.isTextValid(serialNumber))
    }

    @Test
    fun `when 'serialnumber' is not null or empty then 'isTextValid' return true`() {
        val serialNumber = "S1209"
        bikeViewModel.serialNumber.postValue(serialNumber)
        assertTrue(bikeViewModel.isTextValid(serialNumber))
    }

    @Test
    fun `when serial number is already registered in the community then it should be invalid`() {
        fillAndObserveValidBike()
        val registeredSerialNumber = bike.serialNumber

        bikeViewModel.serialNumber.value = registeredSerialNumber
        val isValid = bikeViewModel.valid.value

        assertNotNull(isValid)
        if (isValid != null) {
            assertFalse(isValid)
        }
    }

    @Test
    fun `when 'saveBike' to register and 'isEditModeAvailable' is false, should return Success Status`() {
        val bikeFake = slot<Bike>()
        val result = SimpleResult.Success(AddDataResponse(""))
        coEvery {
            bikeFormUseCase.addNewBike(capture(bikeFake))
        } returns result

        bikeViewModel.saveBike()
        assertTrue(bikeViewModel.state.value is BikeFormStatus.Success)
    }

    @Test
    fun `when 'saveBike' to register and 'isEditModeAvailable' is false, should return Error Status`() {
        val bikeFake = slot<Bike>()
        val result = SimpleResult.Error(Exception())

        coEvery {
            bikeFormUseCase.addNewBike(capture(bikeFake))
        } returns result

        bikeViewModel.saveBike()
        assertTrue(bikeViewModel.state.value is BikeFormStatus.Error)
    }

    @Test
    fun `check success status ordering`() {
        val bikeFake = slot<Bike>()
        val observerBikeResultMock = mockk<Observer<BikeFormStatus>>(relaxed = true)
        val simpleResultData = "bicicleta caloi"
        val result = SimpleResult.Success(AddDataResponse(simpleResultData))

        coEvery {
            bikeFormUseCase.addNewBike(capture(bikeFake))
        } returns result
        bikeViewModel.state.observeForever(observerBikeResultMock)
        bikeViewModel.saveBike()

        verifyOrder {
            observerBikeResultMock.onChanged(BikeFormStatus.Loading)
            observerBikeResultMock.onChanged(BikeFormStatus.Success(simpleResultData))
        }
    }

    @Test
    fun `when 'updateImagePath', should update imagePath value`() {
        bikeViewModel.imagePath.value = bikeFake.photoPath
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

    @Test
    fun `when 'updateBikeValues' should update Bike value to edit`() {
        bikeViewModel.updateBikeValues(bikeFake)

        assertEquals(bikeFake.name, bikeViewModel.bike.name)
        assertEquals(bikeFake.serialNumber, bikeViewModel.bike.serialNumber)
        assertEquals(bikeFake.orderNumber, bikeViewModel.bike.orderNumber)
        assertEquals(bikeFake.photoPath, bikeViewModel.bike.photoPath)
    }

    @Test
    fun `when start viewModel should 'isEditModeAvailable' false`() {
        assertFalse(bikeViewModel.isEditModeAvailable)
    }

    @Test
    fun `when 'updateBikeValues' should update 'isEditModeAvailable' to true`() {
        bikeViewModel.updateBikeValues(bikeFake)
        assertTrue(bikeViewModel.isEditModeAvailable)
    }

    @Test
    fun `when 'saveBike' to edit and 'isEditModeAvailable' is true, should return Success Status`() {
        bikeViewModel.isEditModeAvailable = true
        val bikeFake = slot<Bike>()
        val result = SimpleResult.Success(AddDataResponse(""))
        coEvery {
            bikeFormUseCase.startUpdateBike(capture(bikeFake))
        } returns result

        bikeViewModel.saveBike()
        assertTrue(bikeViewModel.state.value is BikeFormStatus.Success)
    }

    @Test
    fun `when 'saveBike' to edit and 'isEditModeAvailable' is true, should return Error Status`() {
        bikeViewModel.isEditModeAvailable = true
        val bikeFake = slot<Bike>()
        val result = SimpleResult.Error(Exception())
        coEvery {
            bikeFormUseCase.startUpdateBike(capture(bikeFake))
        } returns result

        bikeViewModel.saveBike()
        assertTrue(bikeViewModel.state.value is BikeFormStatus.Error)
    }

    private val bikeFake = Bike().apply {
        name = "Monark"
        serialNumber = "12345"
        orderNumber = 0
        photoPath = "path"
    }

    private fun fillAndObserveValidBike(): Observer<Boolean> {
        val serialNumber = "S1209"
        val bikeName = "S1209"
        val orderNumber = "12345678"
        val imagePath = "data/img/image.jpeg"
        val observerBikeSerialNumberErrorValidation =
            mockk<Observer<MutableMap<Int, Boolean>>>(relaxed = true)
        bikeViewModel.serialNumberErrorValidationMap
            .observeForever(observerBikeSerialNumberErrorValidation)
        val observerBikeResultMock = mockk<Observer<Boolean>>(relaxed = true)
        bikeViewModel.valid.observeForever(observerBikeResultMock)
        bikeViewModel.serialNumber.postValue(serialNumber)
        bikeViewModel.bikeName.postValue(bikeName)
        bikeViewModel.orderNumber.postValue(orderNumber)
        bikeViewModel.imagePath.postValue(imagePath)
        return observerBikeResultMock
    }
}