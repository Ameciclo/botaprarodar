package app.igormatos.botaprarodar.presentation.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.user.UserUseCase
import app.igormatos.botaprarodar.presentation.adduser.AddUserViewModel
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddUserViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val userUseCase = mockk<UserUseCase>()
    private val community = mockk<Community>(relaxed = true)
    private lateinit var viewModel: AddUserViewModel

    @Before
    fun setup() {
        viewModel = AddUserViewModel(userUseCase, community)
    }

    @Test
    fun `when 'setProfileImage' should update profile image value`() {
        val expectedValue = "mock"
        viewModel.setProfileImage("mock")
        assertEquals(expectedValue, viewModel.userImageProfile.value)
    }

    @Test
    fun `when 'setDocumentImageFront' should update document image front value`() {
        val expectedValue = "mock"
        viewModel.setDocumentImageFront("mock")
        assertEquals(expectedValue, viewModel.userImageDocumentFront.value)
    }

    @Test
    fun `when 'setDocumentImageBack' should update document image back value`() {
        val expectedValue = "mock"
        viewModel.setDocumentImageBack("mock")
        assertEquals(expectedValue, viewModel.userImageDocumentBack.value)
    }

    @Test
    fun `when 'setResidenceImage' should update residence image value`() {
        val expectedValue = "mock"
        viewModel.setResidenceImage("mock")
        assertEquals(expectedValue, viewModel.userImageDocumentResidence.value)
    }

    @Test
    fun `when 'setUserGender' should update user gender value with correct value`() {
        val expectedGender = 3
        viewModel.setUserGender(R.id.rbGenderNoAnswer)
        assertEquals(expectedGender, viewModel.userGender.value)
    }

    @Test
    fun `when 'setUserGender' should update user gender value with different value`() {
        val expectedGender = 2
        viewModel.setUserGender(R.id.rbGenderNoAnswer)
        assertNotEquals(expectedGender, viewModel.userGender.value)
    }

    @Test
    fun `when 'registerUser' should return success`() {
        viewModel.userDocument.value = "1"
        val userFake = slot<User>()
        coEvery {
            userUseCase.addUser(community.id, capture(userFake))
        } returns SimpleResult.Success("")

        viewModel.registerUser()
        assertTrue(viewModel.status.value is ViewModelStatus.Success)
    }

    @Test
    fun `when 'registerUser' should return error`() {
        viewModel.userDocument.value = "1"
        val userFake = slot<User>()
        coEvery {
            userUseCase.addUser(community.id, capture(userFake))
        } returns SimpleResult.Error(Exception())

        viewModel.registerUser()
        assertTrue(viewModel.status.value is ViewModelStatus.Error)
    }
}