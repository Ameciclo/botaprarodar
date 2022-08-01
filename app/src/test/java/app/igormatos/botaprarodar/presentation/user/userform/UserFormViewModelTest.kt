package app.igormatos.botaprarodar.presentation.user.userform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.presentation.user.RegisterUserStepper
import app.igormatos.botaprarodar.utils.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val stepper = spyk(RegisterUserStepper(StepConfigType.USER_PERSONAl_INFO))
    private val community = mockk<Community>(relaxed = true)
    private lateinit var formViewModel: UserFormViewModel

    @Before
    fun setup() {
        formViewModel = UserFormViewModel(
            community,
            stepper,
            arrayListOf(validUser))
    }

    @Test
    fun `when 'setProfileImage' should update profile image value`() {
        val expectedValue = "mock"
        formViewModel.setProfileImage("mock")
        assertEquals(expectedValue, formViewModel.userImageProfile.value)
    }

    @Test
    fun `when 'updateUser' should update the liveDatas values`() {
        formViewModel.updateUserValues(validUser)

        assertEquals(validUser.name, formViewModel.userCompleteName.value)
        assertEquals(validUser.address, formViewModel.userAddress.value)
        assertEquals(validUser.profilePicture, formViewModel.userImageProfile.value)
        assertTrue(formViewModel.isEditableAvailable)
    }

    @Test
    fun `when 'updateUser' should update 'isEditableAvailable' to true`() {
        formViewModel.updateUserValues(validUser)
        assertTrue(formViewModel.isEditableAvailable)
    }

    @Test
    fun `when call navigateToNextStep() then the stepperAdapter should be update with the new value`() {
        formViewModel.navigateToNextStep()

        verify { stepper.navigateToNext() }

        assertEquals(formViewModel.stepper.currentStep.value, StepConfigType.USER_SOCIAL_INFO)
    }

    @Test
    fun `when call navigateToNextStep() then the openQuiz value should be update`() {
        val testUser = createTestValidUser()
        every { stepper.navigateToNext() } answers { formViewModel.user = testUser }
        formViewModel.navigateToNextStep()

        val openQuiz = formViewModel.openUserSocialData.value

        assertNotNull(formViewModel.openUserSocialData.value)
        assertEquals(openQuiz?.peekContent()?.first, testUser)
        assertEquals(openQuiz?.peekContent()?.second, false)
    }

    @Test
    fun `when user is valid then button should be enabled`() {
        val testValidUser = createTestValidUser()
        createUserValues(testValidUser)
        observeValidationResultFields()

        doRegisterButtonAssertions(true)
    }

    private fun createTestValidUser(): User {
        val testValidUser = validUser.copy()
        testValidUser.docNumber = 11111111111
        return testValidUser
    }

    private fun createUserValues(testValidUser: User) {
        with(formViewModel) {
            userCompleteName.value = testValidUser.name.orEmpty()
            userAddress.value = testValidUser.address.orEmpty()
            userImageProfile.value = testValidUser.profilePicture.orEmpty()
            userBirthday.value = testValidUser.birthday.orEmpty()
            userTelephone.value = testValidUser.telephone.orEmpty()
        }
    }

    private fun observeValidationResultFields() {
        val observerUserResultMock = mockk<Observer<Boolean>>(relaxed = true)
        formViewModel.isButtonEnabled.observeForever(observerUserResultMock)
    }

    private fun doRegisterButtonAssertions(enableStateExpected: Boolean) {
        val isButtonEnabled = formViewModel.isButtonEnabled.value
        assertNotNull(isButtonEnabled)
        if (isButtonEnabled != null) {
            assertThat(isButtonEnabled, equalTo(enableStateExpected))
        }
    }
}
