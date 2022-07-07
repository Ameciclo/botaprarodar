package app.igormatos.botaprarodar.presentation.user.userform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.R
import app.igormatos.botaprarodar.common.enumType.StepConfigType
import app.igormatos.botaprarodar.common.extensions.getIndexFromList
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

    private val stepper = spyk(RegisterUserStepper(StepConfigType.USER_FORM))
    private val community = mockk<Community>(relaxed = true)
    private lateinit var formViewModel: UserFormViewModel
    private val mapOptions: Map<String, List<String>> = mapOf("racialOptions" to racialOptions, "incomeOptions" to incomeOptions,
        "schoolingOptions" to schoolingOptions, "schoolingStatusOptions" to schoolingStatusOptions,
        "genderOptions" to genderOptions )


    @Before
    fun setup() {
        formViewModel = UserFormViewModel(
            community,
            stepper,
            arrayListOf(validUser),
            mapOptions)
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
        assertEquals(validUser.gender, formViewModel.userGender.value)
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

        assertEquals(formViewModel.stepper.currentStep.value, StepConfigType.USER_QUIZ)
    }

    @Test
    fun `when call navigateToNextStep() then the openQuiz value should be update`() {
        val testUser = createTestValidUser()
        every { stepper.navigateToNext() } answers { formViewModel.user = testUser }
        formViewModel.navigateToNextStep()

        val openQuiz = formViewModel.openQuiz.value

        assertNotNull(formViewModel.openQuiz.value)
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

    @Test
    fun `when call setSelectGenderIndex() then user gender value should be updated`() {
        val index = 1

        formViewModel.setSelectGenderIndex(index)
        assertEquals(index, formViewModel.selectedGenderIndex)
    }

    @Test
    fun `when call setSelectSchoolingIndex() then user schooling value should be updated`() {
        val index = 2

        formViewModel.setSelectSchoolingIndex(index)
        assertEquals(index, formViewModel.selectedSchoolingIndex)
    }

    @Test
    fun `when call setSelectSchoolingStatusIndex() then user schoolingStatus value should be updated`() {
        val index = R.id.schoolingStatusIncomplete

        formViewModel.setSelectSchoolingStatusIndex(index)
        formViewModel.confirmUserSchoolingStatus()
        val indexExpected = formViewModel.getSelectedSchoolingStatusListIndex()
        assertEquals(indexExpected, formViewModel.selectedSchoolingStatusIndex.value)
    }

    @Test
    fun `when call confirmUserSchoolingStatus() then user schoolingStatus value should be updated`() {
        val index = R.id.schoolingStatusIncomplete

        formViewModel.setSelectSchoolingStatusIndex(index)
        formViewModel.confirmUserSchoolingStatus()

        val indexExpected = formViewModel.mapOptions.getIndexFromList("schoolingStatusOptions", formViewModel.userSchoolingStatus.value.toString())
        assertEquals(schoolingStatusOptions[indexExpected], formViewModel.userSchoolingStatus.value)
    }

    @Test
    fun `when call setSelectRacialIndex() then user racial value should be updated`() {
        val index = 2

        formViewModel.setSelectRacialIndex(index)
        assertEquals(index, formViewModel.selectedRacialIndex)
    }

    @Test
    fun `when call setSelectIncomeIndex() then user income value should be updated`() {
        val index = 2

        formViewModel.setSelectIncomeIndex(index)
        assertEquals(index, formViewModel.selectedIncomeIndex)
    }

    @Test
    fun `when call confirmUserGender() then user gender value should be updated`() {
        val index = 1

        formViewModel.setSelectGenderIndex(index)
        formViewModel.confirmUserGender()
        assertEquals(genderOptions[index], formViewModel.userGender.value)
    }

    @Test
    fun `when call confirmUserSchooling() then user schooling value should be updated`() {
        val index = 2

        formViewModel.setSelectSchoolingIndex(index)
        formViewModel.confirmUserSchooling()
        assertEquals(schoolingOptions[index], formViewModel.userSchooling.value)
    }

    @Test
    fun `when call confirmUserRace() then user racial value should be updated`() {
        val index = 2

        formViewModel.setSelectRacialIndex(index)
        formViewModel.confirmUserRace()
        assertEquals(racialOptions[index], formViewModel.userRacial.value)
    }

    @Test
    fun `when call confirmUserIncome() then user income value should be updated`() {
        val index = 2

        formViewModel.setSelectIncomeIndex(index)
        formViewModel.confirmUserIncome()
        assertEquals(incomeOptions[index], formViewModel.userIncome.value)
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
            userGender.value = testValidUser.gender.orEmpty()
            userRacial.value = testValidUser.racial.orEmpty()
            userSchooling.value = testValidUser.schooling.orEmpty()
            userSchoolingStatus.value = testValidUser.schoolingStatus.orEmpty()
            userIncome.value = testValidUser.income.orEmpty()
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