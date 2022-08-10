package app.igormatos.botaprarodar.presentation.user.userform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.domain.model.User
import app.igormatos.botaprarodar.domain.model.community.Community
import app.igormatos.botaprarodar.utils.validUser
import io.mockk.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions

class UserFormViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val community = mockk<Community>(relaxed = true)
    private lateinit var formViewModel: UserFormViewModel

    private val observerUserResultMock = mockk<Observer<Boolean>>(relaxed = true)

    @Before
    fun setup() {
        formViewModel = UserFormViewModel(
            community,
            arrayListOf(validUser)
        )

        formViewModel.isButtonEnabled.observeForever(observerUserResultMock)

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

        val slotButtonEnabled = slot<Boolean>()

        every { observerUserResultMock.onChanged(capture(slotButtonEnabled)) } just Runs

        createUserValues(validUser.copy())

        assertTrue(slotButtonEnabled.captured)

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
}
