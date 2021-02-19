package app.igormatos.botaprarodar.presentation.user_form

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.ViewModelStatus
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.domain.usecase.user.UserUseCase
import app.igormatos.botaprarodar.presentation.userForm.UserFormViewModel
import app.igormatos.botaprarodar.utils.userFake
import com.brunotmgomes.ui.SimpleResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val userUseCase = mockk<UserUseCase>()
    private val community = mockk<Community>(relaxed = true)
    private lateinit var formViewModel: UserFormViewModel

    @Before
    fun setup() {
        formViewModel = UserFormViewModel(userUseCase, community)
    }

    @Test
    fun `when 'setProfileImage' should update profile image value`() {
        val expectedValue = "mock"
        formViewModel.setProfileImage("mock")
        assertEquals(expectedValue, formViewModel.userImageProfile.value)
    }

    @Test
    fun `when 'setDocumentImageFront' should update document image front value`() {
        val expectedValue = "mock"
        formViewModel.setDocumentImageFront("mock")
        assertEquals(expectedValue, formViewModel.userImageDocumentFront.value)
    }

    @Test
    fun `when 'setDocumentImageBack' should update document image back value`() {
        val expectedValue = "mock"
        formViewModel.setDocumentImageBack("mock")
        assertEquals(expectedValue, formViewModel.userImageDocumentBack.value)
    }

    @Test
    fun `when 'setResidenceImage' should update residence image value`() {
        val expectedValue = "mock"
        formViewModel.setResidenceImage("mock")
        assertEquals(expectedValue, formViewModel.userImageDocumentResidence.value)
    }

    @Test
    fun `when 'setUserGender' should update user gender value with correct value`() {
        val expectedGender = 3
        formViewModel.setUserGender(R.id.rbGenderNoAnswer)
        assertEquals(expectedGender, formViewModel.userGender.value)
    }

    @Test
    fun `when 'setUserGender' should update user gender value with different value`() {
        val expectedGender = 2
        formViewModel.setUserGender(R.id.rbGenderNoAnswer)
        assertNotEquals(expectedGender, formViewModel.userGender.value)
    }

    @Test
    fun `when 'registerUser' should return success`() {
        formViewModel.userDocument.value = "1"
        val userFake = slot<User>()
        coEvery {
            userUseCase.addUser(community.id, capture(userFake))
        } returns SimpleResult.Success("")

        formViewModel.registerUser()
        assertTrue(formViewModel.status.value is ViewModelStatus.Success)
    }

    @Test
    fun `when 'registerUser' should return error`() {
        formViewModel.userDocument.value = "1"
        val userFake = slot<User>()
        coEvery {
            userUseCase.addUser(community.id, capture(userFake))
        } returns SimpleResult.Error(Exception())

        formViewModel.registerUser()
        assertTrue(formViewModel.status.value is ViewModelStatus.Error)
    }

    @Test
    fun `when 'updateUser' should update the liveDatas values`() {
        formViewModel.updateUserValues(userFake)

        assertEquals(userFake.name, formViewModel.userCompleteName.value)
        assertEquals(userFake.address, formViewModel.userAddress.value)
        assertEquals(userFake.doc_number.toString(), formViewModel.userDocument.value)
        assertEquals(userFake.profile_picture, formViewModel.userImageProfile.value)
        assertEquals(
            userFake.residence_proof_picture,
            formViewModel.userImageDocumentResidence.value
        )
        assertEquals(userFake.doc_picture, formViewModel.userImageDocumentFront.value)
        assertEquals(userFake.doc_picture_back, formViewModel.userImageDocumentBack.value)
        assertEquals(userFake.gender, formViewModel.userGender.value)
    }

    @Test
    fun `when 'updateUser' should update 'isEditableAvailable' to true`() {
        formViewModel.updateUserValues(userFake)
        assertTrue(formViewModel.isEditableAvailable)
    }

    @Test
    fun `when 'registerUser' and 'isEditableAvailable' is true should return success`() {
        formViewModel.userDocument.value = "1"
        formViewModel.isEditableAvailable = true
        val userFake = slot<User>()
        coEvery {
            userUseCase.updateUser(community.id, capture(userFake))
        } returns SimpleResult.Success("")

        formViewModel.registerUser()
        assertTrue(formViewModel.status.value is ViewModelStatus.Success)
    }

    @Test
    fun `when 'registerUser' and 'IsEditableAvailable' is true should return error`() {
        formViewModel.userDocument.value = "1"
        formViewModel.isEditableAvailable = true
        val userFake = slot<User>()
        coEvery {
            userUseCase.updateUser(community.id, capture(userFake))
        } returns SimpleResult.Error(Exception())

        formViewModel.registerUser()
        assertTrue(formViewModel.status.value is ViewModelStatus.Error)
    }
}