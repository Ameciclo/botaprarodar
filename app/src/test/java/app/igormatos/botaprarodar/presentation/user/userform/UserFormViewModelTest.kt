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

    private val community = mockk<Community>(relaxed = true)
    private lateinit var formViewModel: UserFormViewModel

    @Before
    fun setup() {
        formViewModel = UserFormViewModel(
            community,
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
    fun `when call navigateToNextStep() then the openSocialData value should be update`() {
        formViewModel.user = validUser
        formViewModel.isEditableAvailable = false

        formViewModel.navigateToNextStep()

        val openSocialData = formViewModel.openUserSocialData.value

        assertNotNull(formViewModel.openUserSocialData.value)
        assertEquals(openSocialData?.peekContent()?.first, validUser)
        assertEquals(openSocialData?.peekContent()?.second, false)
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
