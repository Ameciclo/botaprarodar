package app.igormatos.botaprarodar.presentation.user.userform

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import app.igormatos.botaprarodar.utils.userFake
import com.brunotmgomes.ui.ViewEvent
import com.firebase.ui.auth.data.model.User
import io.mockk.MockKSettings.relaxed
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.Matcher

import junit.framework.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val stepper = spyk(RegisterUserStepper(StepConfigType.USER_FORM))
    private val community = mockk<Community>(relaxed = true)
    private lateinit var formViewModel: UserFormViewModel

    @Before
    fun setup() {
        formViewModel = UserFormViewModel(community, stepper)
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
    fun `when 'updateUser' should update the liveDatas values`() {
        formViewModel.updateUserValues(userFake)

        assertEquals(userFake.name, formViewModel.userCompleteName.value)
        assertEquals(userFake.address, formViewModel.userAddress.value)
        assertEquals(userFake.docNumber.toString(), formViewModel.userDocument.value)
        assertEquals(userFake.profilePicture, formViewModel.userImageProfile.value)
        assertEquals(
            userFake.residenceProofPicture,
            formViewModel.userImageDocumentResidence.value
        )
        assertEquals(userFake.docPicture, formViewModel.userImageDocumentFront.value)
        assertEquals(userFake.docPictureBack, formViewModel.userImageDocumentBack.value)
        assertEquals(userFake.gender, formViewModel.userGender.value)
        assertTrue(formViewModel.isEditableAvailable)
    }

    @Test
    fun `when 'updateUser' should update 'isEditableAvailable' to true`() {
        formViewModel.updateUserValues(userFake)
        assertTrue(formViewModel.isEditableAvailable)
    }

    @Test
    fun `when call navigateToNextStep() then the stepperAdapter should be update with the new value`() {
        formViewModel.userDocument.value = "1"
        formViewModel.navigateToNextStep()

        verify { stepper.navigateToNext() }

        assertEquals(formViewModel.stepper.currentStep.value, StepConfigType.USER_QUIZ)
    }

    @Test
    fun `when call navigateToNextStep() then the openQuiz value should be update`() {
        every { stepper.navigateToNext() } answers { formViewModel.user = userFake }
        formViewModel.userDocument.value = "1"
        formViewModel.navigateToNextStep()

        val openQuiz = formViewModel.openQuiz.value

        assertNotNull(formViewModel.openQuiz.value)
        assertEquals(openQuiz?.peekContent()?.first, userFake)
        assertEquals(openQuiz?.peekContent()?.second, false)
    }

}